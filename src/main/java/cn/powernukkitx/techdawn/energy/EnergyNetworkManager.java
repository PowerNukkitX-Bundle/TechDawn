package cn.powernukkitx.techdawn.energy;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.powernukkitx.techdawn.blockentity.wire.BaseWireBlockEntity;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EnergyNetworkManager {
    public static final Map<String, Long2ObjectMap<List<EnergyNetworkStore>>> networks = new ConcurrentHashMap<>();

    public static EnergyNetworkStore findAt(int x, int y, int z, @NotNull Level level) {
        return findAt(x, y, z, level, true);
    }

    @Nullable
    public static EnergyNetworkStore findAt(int x, int y, int z, @NotNull Level level, boolean tryRebuild) {
        var sub = networks.get(level.getName());
        if (sub == null) {
            return tryRebuild ? tryRebuildAt(x, y, z, level) : null;
        }
        var list = sub.get(Level.chunkHash(x >> 4, z >> 4));
        if (list == null) {
            return tryRebuild ? tryRebuildAt(x, y, z, level) : null;
        }
        for (EnergyNetworkStore network : list) {
            if (network.hasPoint(x, y, z)) {
                return network;
            }
        }
        return tryRebuild ? tryRebuildAt(x, y, z, level) : null;
    }

    /**
     * 尝试在指定位置重建电网并将其添加到电网列表中
     */
    @Nullable
    public static EnergyNetworkStore tryRebuildAt(int x, int y, int z, @NotNull Level level) {
        return tryRebuildAt(x, y, z, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, level);
    }

    /**
     * 尝试在指定位置重建电网并将其添加到电网列表中
     */
    @Nullable
    public static EnergyNetworkStore tryRebuildAt(int x, int y, int z, int excludeX, int excludeY, int excludeZ, @NotNull Level level) {
        var result = rebuildNetworkAt(x, y, z, excludeX, excludeY, excludeZ, level);
        if (result != null) {
            var sub = networks.computeIfAbsent(level.getName(), k -> new Long2ObjectOpenHashMap<>());
            for (long each : result.chunkHashes()) {
                var list = sub.computeIfAbsent(each, k -> new ArrayList<>());
                list.removeIf(energyNetworkStore -> energyNetworkStore.hasPoint(x, y, z));
                list.add(result);
            }
        }
        return result;
    }

    /**
     * @return 与目标处相邻的所有电网
     */
    @NotNull
    public static Set<EnergyNetworkStore> findNetworkNearBy(int x, int y, int z, @NotNull Level level) {
        var networkNearBy = new HashSet<EnergyNetworkStore>();
        for (var face : BlockFace.values()) {
            var network = findAt(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset(), level);
            if (network != null) {
                networkNearBy.add(network);
            }
        }
        return networkNearBy;
    }

    /**
     * 检查并保证这个电网与全局电网缓存Map中的电网是同步的。如果电网的线缆发生的变化，请调用这个方法来保证电网缓存的正确性。<br>
     * 这可以避免某个电网确实延申到了某个区块，但是区块缓存中并没有这个电网导致重复构建电网的问题。
     */
    public static void ensureNetworkSync(@NotNull EnergyNetworkStore network) {
        var sub = networks.get(network.getLevel().getName());
        if (sub == null) {
            networks.put(network.getLevel().getName(), sub = new Long2ObjectOpenHashMap<>());
        }
        for (long each : network.chunkHashes()) {
            var list = sub.computeIfAbsent(each, k -> new ArrayList<>());
            if (network.isNoWirePointsInChunk(each)) {
                list.remove(network);
            } else if (!list.contains(network))
                list.add(network);
        }
    }

    private static void checkAndConnectMachineAt(int x, int y, int z, @NotNull EnergyNetworkStore network, @NotNull Level level) {
        for (var face : BlockFace.values()) {
            var targetX = x + face.getXOffset();
            var targetY = y + face.getYOffset();
            var targetZ = z + face.getZOffset();
            var bVec3 = new BlockVector3(targetX, targetY, targetZ);
            var neighborType = canBeNeighbor(level, bVec3, face.getOpposite());
            if (neighborType == Neighbor.MACHINE && canConnectMachineAtItsFace(level, bVec3, face.getOpposite())) {
                network.putMachinePoint(targetX, targetY, targetZ);
                network.setConnectAt(x, y, z, face);
            }
        }
    }

    public static void putWireAt(int x, int y, int z, @NotNull Level level) {
        // 找到与此处新线缆相邻的所有电网
        var networkNearBy = findNetworkNearBy(x, y, z, level);
        // 如果没有任何电网，那就就地新建一个
        if (networkNearBy.size() == 0) {
            var network = new EnergyNetworkStore(level);
            network.putPoint(x, y, z);
            var sub = networks.computeIfAbsent(level.getName(), k -> new Long2ObjectOpenHashMap<>());
            for (long each : network.chunkHashes()) {
                var list = sub.computeIfAbsent(each, k -> new ArrayList<>());
                list.add(network);
            }
            // 查找此点周围的可连接机器并连接
            checkAndConnectMachineAt(x, y, z, network, level);
        } else {
            EnergyNetworkStore network;
            if (networkNearBy.size() == 1) { // 如果附近只有一个电网，那么只需要将新线缆加入到这个电网中即可
                network = networkNearBy.iterator().next();
            } else { // 如果有多个电网与新线缆相邻，需要将多个电网合并为一个，删除原有的多个电网，将新电网加入到电网列表中
                network = EnergyNetworkStore.merge(networkNearBy.toArray(EnergyNetworkStore[]::new));
                var sub = networks.computeIfAbsent(level.getName(), k -> new Long2ObjectOpenHashMap<>());
                for (EnergyNetworkStore each : networkNearBy) {
                    for (long hash : each.chunkHashes()) {
                        var list = sub.get(hash);
                        if (list != null) {
                            list.remove(each);
                        }
                    }
                }
                for (long hash : network.chunkHashes()) {
                    var list = sub.computeIfAbsent(hash, k -> new ArrayList<>());
                    list.add(network);
                }
            }
            if (network != null) {
                // 然后设置新线缆与周围线缆的连接状态
                var enumSet = new ArrayList<BlockFace>(2);
                for (var face : BlockFace.values()) {
                    var targetX = x + face.getXOffset();
                    var targetY = y + face.getYOffset();
                    var targetZ = z + face.getZOffset();
                    if (network.hasPoint(targetX, targetY, targetZ)) {
                        enumSet.add(face);
                        network.setConnectAt(targetX, targetY, targetZ, face.getOpposite());
                    }
                }
                network.putPoint(x, y, z, enumSet.toArray(BlockFace[]::new));
                // 查找此点周围的可连接机器并连接
                checkAndConnectMachineAt(x, y, z, network, level);
                // 最后保证电网同步，因为新加入的点可能改变了电网占据的区块
                ensureNetworkSync(network);
            }
        }
    }

    public static void putMachineAt(int x, int y, int z, @NotNull Level level, @NotNull EnergyHolder holder) {
        // 找到与此处新机器相邻的所有电网
        var networkNearBy = findNetworkNearBy(x, y, z, level);
        // 在每个电网中都添加这个机器
        for (var network : networkNearBy) {
            boolean canConnect = false;
            // 顺便查找周围的线缆，如果有的话，就将这个机器与线缆连接起来
            for (var face : BlockFace.values()) {
                var targetX = x + face.getXOffset();
                var targetY = y + face.getYOffset();
                var targetZ = z + face.getZOffset();
                if (network.hasPoint(targetX, targetY, targetZ) && (
                        holder.canAcceptInput(RF.getInstance(), face) || holder.canProvideOutput(RF.getInstance(), face)
                )) {
                    canConnect = true;
                    network.setConnectAt(targetX, targetY, targetZ, face.getOpposite());
                }
            }
            if (canConnect) network.putMachinePoint(x, y, z);
        }
    }

    public static void removeWireAt(int x, int y, int z, @NotNull Level level) {
        // 找到有重复的相邻电网
        var networkNearByList = new ArrayList<EnergyNetworkStore>(6);
        var neighborNetworkFaces = new ArrayList<BlockFace>(6);
        for (var face : BlockFace.values()) {
            var network = findAt(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset(), level);
            if (network != null) {
                networkNearByList.add(network);
                neighborNetworkFaces.add(face);
            }
        }
        // 如果没有面相接其他电网，即该线缆是孤立线缆，则直接删除此电网即可
        if (networkNearByList.isEmpty()) {
            var network = findAt(x, y, z, level, false);
            if (network != null) {
                removeNetwork(level, network);
            }
        }
        // 如果只有一个面相接，直接从电网中移除这个线缆即可
        else if (networkNearByList.size() == 1) {
            var network = networkNearByList.get(0);
            network.removePoint(x, y, z);
            // 顺便查找周围的线缆，如果有的话，就将这个线缆与周围的线缆断开
            // 如果有连接的机器的话，也将机器与周围的线缆断开
            // 如果一个机器的六个面都与此电网没有连接，那么就把这个机器从电网中清除
            for (var face : BlockFace.values()) {
                var targetX = x + face.getXOffset();
                var targetY = y + face.getYOffset();
                var targetZ = z + face.getZOffset();
                if (network.hasPoint(targetX, targetY, targetZ)) {
                    network.disconnectAt(targetX, targetY, targetZ, face);
                } else if (network.hasMachinePoint(targetX, targetY, targetZ)) { // 一个点要么是线缆要么是机器
                    // 查看机器是否六个面都没有保持连接
                    boolean connected = false;
                    for (var f2 : BlockFace.values()) {
                        var machineTargetX = targetX + f2.getXOffset();
                        var machineTargetY = targetY + f2.getYOffset();
                        var machineTargetZ = targetZ + f2.getZOffset();
                        if (machineTargetX == x && machineTargetY == y && machineTargetZ == z) continue;
                        if (network.isConnectAt(machineTargetX, machineTargetY, machineTargetX, f2.getOpposite())) {
                            connected = true;
                            break;
                        }
                    }
                    // 如果没有保持连接，那么就从电网中移除这个机器
                    if (!connected) {
                        network.removeMachinePoint(targetX, targetY, targetZ);
                    }
                }
            }
            // 最后保证电网同步，因为去除掉的点可能改变了电网占据的区块
            ensureNetworkSync(network);
        }
        // 如果有多个面相接，这种最坏情况就不得不重新构建网络了
        // P.S. 肯定有更好的算法可以免于这么做，但是赶时间，没必要那么扣优化啦
        else {
            // 电网去重
            var networkNearBySet = new HashSet<>(networkNearByList);
            // 将这些电网都从电网列表中移除，然后重新构建
            for (var each : networkNearBySet) {
                removeNetwork(level, each);
                // 这里尝试从电网中移除要去除的点，但是这个操作不一定会成功，因为有可能这个点不在电网中
                each.removePoint(x, y, z);
            }
            // 重新构建所有相邻面的电网
            var rebuiltNetworks = new ArrayList<EnergyNetworkStore>(neighborNetworkFaces.size());
            for (var face : neighborNetworkFaces) {
                var targetX = x + face.getXOffset();
                var targetY = y + face.getYOffset();
                var targetZ = z + face.getZOffset();
                // 检查重构过的电网中是否已经包含了现在要重构电网的位置
                var contains = false;
                for (var network : rebuiltNetworks) {
                    if (network.hasPoint(targetX, targetY, targetZ)) {
                        contains = true;
                        break;
                    }
                }
                // 如果没有包含，就重构这个电网
                if (!contains) {
                    var network = tryRebuildAt(targetX, targetY, targetZ, x, y, z, level);
                    if (network != null) rebuiltNetworks.add(network);
                }
            }
        }
    }

    private static void removeNetwork(@NotNull Level level, EnergyNetworkStore each) {
        var sub = networks.get(level.getName());
        if (sub != null) {
            for (long hash : each.chunkHashes()) {
                var list = sub.get(hash);
                if (list != null) {
                    list.remove(each);
                }
            }
        }
    }

    public static void removeMachineAt(int x, int y, int z, @NotNull Level level) {
        // 找到与此处机器相邻的所有电网
        var networkNearBy = findNetworkNearBy(x, y, z, level);
        // 在每个电网中都移除这个机器
        for (var network : networkNearBy) {
            network.removeMachinePoint(x, y, z);
            // 顺便查找周围的线缆，如果有的话，就将这个机器与线缆断开连接
            for (var face : BlockFace.values()) {
                var targetX = x + face.getXOffset();
                var targetY = y + face.getYOffset();
                var targetZ = z + face.getZOffset();
                if (network.hasPoint(targetX, targetY, targetZ)) {
                    network.disconnectAt(targetX, targetY, targetZ, face.getOpposite());
                }
            }
        }
    }

    enum Neighbor {
        WIRE, MACHINE, NONE
    }

    private static Neighbor canBeNeighbor(@NotNull Level level, BlockVector3 neighborPos, @Nullable BlockFace neighborFace) {
        if (neighborFace == null) return canBeNeighbor(level, neighborPos);
        var blockEntity = level.getBlockEntity(neighborPos);
        if (blockEntity instanceof BaseWireBlockEntity wire) {
            return wire.isClosing() ? Neighbor.NONE : Neighbor.WIRE;
        } else if (blockEntity instanceof EnergyHolder holder && (holder.canAcceptInput(RF.getInstance(), neighborFace) || holder.canProvideOutput(RF.getInstance(), neighborFace))) {
            return Neighbor.MACHINE;
        } else {
            return Neighbor.NONE;
        }
    }

    private static Neighbor canBeNeighbor(@NotNull Level level, BlockVector3 neighborPos) {
        var blockEntity = level.getBlockEntity(neighborPos);
        if (blockEntity instanceof BaseWireBlockEntity wire) {
            return wire.isClosing() ? Neighbor.NONE : Neighbor.WIRE;
        } else if (blockEntity instanceof EnergyHolder holder && (holder.canAcceptInput(RF.getInstance()) || holder.canProvideOutput(RF.getInstance()))) {
            return Neighbor.MACHINE;
        } else {
            return Neighbor.NONE;
        }
    }

    record SearchPoint(BlockVector3 pos, BlockFace face) {
    }

    private static boolean canConnectMachineAtItsFace(@NotNull Level level, BlockVector3 machinePos, BlockFace blockFace) {
        var blockEntity = level.getBlockEntity(machinePos);
        return blockEntity instanceof EnergyHolder holder && (holder.canAcceptInput(RF.getInstance(), blockFace) || holder.canProvideOutput(RF.getInstance(), blockFace));
    }

    @Nullable
    public static EnergyNetworkStore rebuildNetworkAt(int x, int y, int z, int excludeX, int excludeY, int excludeZ, @NotNull Level level) {
        var bVec = new BlockVector3(x, y, z);
        var thisType = canBeNeighbor(level, bVec);
        if (thisType == Neighbor.WIRE) {
            var network = new EnergyNetworkStore(level);
            var searchedPos = new HashSet<BlockVector3>();
            Queue<SearchPoint> toSearchPos = new LinkedList<>();
            // 将起始点放入待搜索队列
            toSearchPos.add(new SearchPoint(bVec, BlockFace.UP));
            // 只有线才需要加入搜索队列，因为线才有可能连接到其他线

            while (!toSearchPos.isEmpty()) {
                var currentSearch = toSearchPos.poll();
                var currentPos = currentSearch.pos;
                if (searchedPos.contains(currentPos)) {
                    continue;
                }
                // 标记此点已经搜索过了
                searchedPos.add(currentPos);
                // 处理此点
                var currentType = (currentPos.getX() == excludeX &&
                        currentPos.getY() == excludeY &&
                        currentPos.getZ() == excludeZ
                ) ? Neighbor.NONE : canBeNeighbor(level, currentPos, currentSearch.face);
                if (currentType == Neighbor.WIRE) {
                    // 如果是线，就加入网络
                    network.putPoint(currentPos.x, currentPos.y, currentPos.z);
                    // 将周围的点加入搜索队列
                    for (BlockFace face : BlockFace.values()) {
                        var neighborPos = currentPos.getSide(face);
                        if (searchedPos.contains(neighborPos)) {
                            continue;
                        }
                        toSearchPos.add(new SearchPoint(neighborPos, face.getOpposite()));
                    }
                    // 连接此点和周围
                    for (BlockFace face : BlockFace.values()) {
                        var targetX = currentPos.x + face.getXOffset();
                        var targetY = currentPos.y + face.getYOffset();
                        var targetZ = currentPos.z + face.getZOffset();
                        if (network.hasPoint(targetX, targetY, targetZ)) {
                            network.setConnectAt(currentPos.x, currentPos.y, currentPos.z, face);
                            network.setConnectAt(targetX, targetY, targetZ, face.getOpposite());
                        } else if (network.hasMachinePoint(targetX, targetY, targetZ) && canConnectMachineAtItsFace(level, new BlockVector3(targetX, targetY, targetZ), face.getOpposite())) {
                            network.setConnectAt(currentPos.x, currentPos.y, currentPos.z, face);
                        }
                    }
                } else if (currentType == Neighbor.MACHINE) {
                    // 如果是机器，就加入网络
                    network.putMachinePoint(currentPos.x, currentPos.y, currentPos.z);
                    // 连接此机器和周围导线
                    for (BlockFace face : BlockFace.values()) {
                        var targetX = currentPos.x + face.getXOffset();
                        var targetY = currentPos.y + face.getYOffset();
                        var targetZ = currentPos.z + face.getZOffset();
                        if (network.hasPoint(targetX, targetY, targetZ) && canConnectMachineAtItsFace(level, currentPos, face)) {
                            network.setConnectAt(targetX, targetY, targetZ, face.getOpposite());
                        }
                    }
                }
            }

            return network;
        } else {
            return null;
        }
    }

    /**
     * 让一个机器在某处输出能量
     *
     * @param machine      要输出能量的机器，必须是已经存在于世界上的方块实体
     * @param energyOutput 这一次最多输出多少能量
     * @param outputFaces  可能能输出能量的面
     * @return 如果没有成功输出任何能量，返回false
     */
    public static <T extends BlockEntity & EnergyHolder> boolean outputEnergyAt(@NotNull T machine, double energyOutput, Iterable<BlockFace> outputFaces) {
        energyOutput = Math.min(energyOutput, machine.getStoredEnergy());
        var ret = false;
        faceLoop:
        for (var face : outputFaces) {
            var network = EnergyNetworkManager.findAt(machine.getFloorX() + face.getXOffset(), machine.getFloorY() + face.getYOffset(), machine.getFloorZ() + face.getZOffset(), machine.getLevel());
            if (network == null) continue;
            var machines = network.getSortedMachines();
            // 虽然自己给自己点看起来有点愚蠢，但是这是最高效的实现
            for (var each : machines) {
                if (!each.canAcceptInput(RF.getInstance())) continue; // 有些机器比如发电机即使连接到了网络也是不能被输入电能的
                var vacancy = Math.max(each.getMaxStorage() - each.getStoredEnergy(), 0);
                if (vacancy < 0.0001)
                    continue;
                var energyToOutput = Math.min(energyOutput, vacancy);
                energyOutput -= energyToOutput;
                each.inputInto(RF.getInstance(), energyToOutput);
                machine.outputFrom(RF.getInstance(), energyToOutput);
                ret = true;
                if (energyOutput < 0.0001)
                    break faceLoop;
            }
        }
        return ret;
    }
}
