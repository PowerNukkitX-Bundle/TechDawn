package cn.powernukkitx.techdawn.energy;

import cn.nukkit.Server;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.ints.Int2ByteMap;
import it.unimi.dsi.fastutil.ints.Int2ByteOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 这个类仅作存储用途，不包含任何网络控制逻辑
 */
public class EnergyNetworkStore {
    public static final byte NO_CONNECTION_CONSTANT = 0;
    public static final byte NO_POINT_CONSTANT = Byte.MAX_VALUE;

    private final Long2ObjectMap<Int2ByteMap> networkPoints;
    private final Long2ObjectMap<IntSet> networkMachines;
    private final Level level;

    private List<EnergyHolder> sortedMachines;
    private int lastSortTick = -1;


    public EnergyNetworkStore(Level level) {
        this.level = level;
        this.networkPoints = new Long2ObjectOpenHashMap<>();
        this.networkMachines = new Long2ObjectOpenHashMap<>();
    }

    @NotNull
    public static EnergyNetworkStore merge(@NotNull EnergyNetworkStore... sources) {
        var result = new EnergyNetworkStore(sources[0].level);
        for (EnergyNetworkStore source : sources) {
            for (var entry : source.networkPoints.long2ObjectEntrySet()) {
                var chunkHash = entry.getLongKey();
                var points = entry.getValue();
                var resultPoints = result.networkPoints.computeIfAbsent(chunkHash, k -> new Int2ByteOpenHashMap());
                for (var pointEntry : points.int2ByteEntrySet()) {
                    var point = pointEntry.getIntKey();
                    var connection = pointEntry.getByteValue();
                    resultPoints.put(point, connection);
                }
            }
            for (var entry : source.networkMachines.long2ObjectEntrySet()) {
                var chunkHash = entry.getLongKey();
                var machines = entry.getValue();
                var resultMachines = result.networkMachines.computeIfAbsent(chunkHash, k -> new IntOpenHashSet());
                for (int machine : machines) {
                    resultMachines.add(machine);
                }
            }
        }
        return result;
    }

    public Level getLevel() {
        return level;
    }

    public void putPoint(int x, int y, int z) {
        putPoint(x, y, z, 0, NO_CONNECTION_CONSTANT);
    }

    public void putPoint(int x, int y, int z, byte connection) {
        putPoint(x, y, z, 0, connection);
    }

    public void putPoint(int x, int y, int z, BlockFace... connection) {
        byte result = 0;
        for (var face : connection) {
            result |= 1 << face.getIndex();
        }
        putPoint(x, y, z, 0, result);
    }

    public void putPoint(int x, int y, int z, int layer) {
        putPoint(x, y, z, layer, NO_CONNECTION_CONSTANT);
    }

    public void putPoint(int x, int y, int z, int layer, BlockFace... connection) {
        byte result = 0;
        for (var face : connection) {
            result |= 1 << face.getIndex();
        }
        var sub = networkPoints.computeIfAbsent(Level.chunkHash(x >> 4, z >> 4), k -> new Int2ByteOpenHashMap());
        sub.put(Level.localBlockHash(x, y, z, layer, level), result);
    }

    public void putPoint(int x, int y, int z, int layer, byte connection) {
        var sub = networkPoints.computeIfAbsent(Level.chunkHash(x >> 4, z >> 4), k -> new Int2ByteOpenHashMap());
        sub.put(Level.localBlockHash(x, y, z, layer, level), connection);
    }

    public void removePoint(int x, int y, int z) {
        removePoint(x, y, z, 0);
    }

    /**
     * 警告：此方法不会消除与其相连的点的连接！
     */
    public void removePoint(int x, int y, int z, int layer) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return;
        }
        sub.remove(Level.localBlockHash(x, y, z, layer, level));
    }

    public boolean hasPoint(int x, int y, int z) {
        return hasPoint(x, y, z, 0);
    }

    public boolean hasPoint(int x, int y, int z, int layer) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return false;
        }
        return sub.containsKey(Level.localBlockHash(x, y, z, layer, level));
    }

    public byte getPointConnection(int x, int y, int z) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return NO_POINT_CONSTANT;
        }
        return sub.getOrDefault(Level.localBlockHash(x, y, z, 0, level), NO_CONNECTION_CONSTANT);
    }

    public byte getPointConnection(int x, int y, int z, int layer) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return NO_POINT_CONSTANT;
        }
        return sub.getOrDefault(Level.localBlockHash(x, y, z, layer, level), NO_CONNECTION_CONSTANT);
    }

    public boolean isConnectAt(int x, int y, int z, BlockFace face) {
        var state = getPointConnection(x, y, z);
        if (state == NO_CONNECTION_CONSTANT || state == NO_POINT_CONSTANT) return false;
        return ((state & (1 << face.getIndex())) != 0);
    }

    /**
     * @return if all face is connected return true, else false.
     */
    public boolean isConnectAt(int x, int y, int z, BlockFace... face) {
        var state = getPointConnection(x, y, z);
        if (state == NO_CONNECTION_CONSTANT || state == NO_POINT_CONSTANT) return false;
        for (var f : face) {
            if ((state & (1 << f.getIndex())) == 0) return false;
        }
        return true;
    }

    public void setConnectAt(int x, int y, int z, BlockFace... face) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return;
        }
        var hash = Level.localBlockHash(x, y, z, 0, level);
        var state = sub.getOrDefault(hash, NO_CONNECTION_CONSTANT);
        if (state == NO_CONNECTION_CONSTANT) return;
        for (var f : face) {
            state |= (1 << f.getIndex());
        }
        sub.put(hash, state);
    }

    public void disconnectAt(int x, int y, int z, BlockFace... faces) {
        var sub = networkPoints.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return;
        }
        var hash = Level.localBlockHash(x, y, z, 0, level);
        var state = sub.getOrDefault(hash, NO_CONNECTION_CONSTANT);
        if (state == NO_CONNECTION_CONSTANT) return;
        for (var f : faces) {
            state &= ~(1 << f.getIndex());
        }
        sub.put(hash, state);
    }

    public void putMachinePoint(int x, int y, int z) {
        var sub = networkMachines.computeIfAbsent(Level.chunkHash(x >> 4, z >> 4), k -> new IntOpenHashSet());
        sub.add(Level.localBlockHash(x, y, z, 0, level));
    }

    public boolean hasMachinePoint(int x, int y, int z) {
        var sub = networkMachines.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return false;
        }
        return sub.contains(Level.localBlockHash(x, y, z, 0, level));
    }

    public void removeMachinePoint(int x, int y, int z) {
        var sub = networkMachines.get(Level.chunkHash(x >> 4, z >> 4));
        if (sub == null) {
            return;
        }
        sub.remove(Level.localBlockHash(x, y, z, 0, level));
    }

    public LongSet chunkHashes() {
        var tmp = new LongOpenHashSet(networkPoints.keySet());
        tmp.addAll(networkMachines.keySet());
        return tmp;
    }

    public boolean isNoWirePointsInChunk(long chunkHash) {
        var sub = networkPoints.get(chunkHash);
        if (sub == null) {
            return true;
        }
        return sub.isEmpty();
    }

    public void debugParticle() {
        for (var entry : networkPoints.long2ObjectEntrySet()) {
            var chunkHash = entry.getLongKey();
            var points = entry.getValue();
            for (var point : points.keySet()) {
                var pos = Level.getBlockXYZ(chunkHash, point, level);
                level.addParticle(new RedstoneParticle(new Vector3(pos.x + 0.5, pos.y + 0.8, pos.z + 0.5)));
            }
        }
        for (var entry : networkMachines.long2ObjectEntrySet()) {
            var chunkHash = entry.getLongKey();
            var points = entry.getValue();
            for (var point : points) {
                var pos = Level.getBlockXYZ(chunkHash, point, level);
                level.addParticle(new RedstoneParticle(new Vector3(pos.x + 0.5, pos.y + 1.1, pos.z + 0.5)));
            }
        }
    }

    public List<? extends EnergyHolder> getSortedMachines() {
        int currentTick = Server.getInstance().getTick();
        if (currentTick > lastSortTick) {
            lastSortTick = currentTick;
            var tmp = new ArrayList<EnergyHolder>(networkMachines.size() * 8);
            for (var entry : networkMachines.long2ObjectEntrySet()) {
                var chunkHash = entry.getLongKey();
                var points = entry.getValue();
                for (var point : points) {
                    var pos = Level.getBlockXYZ(chunkHash, point, level);
                    var be = level.getBlockEntity(pos);
                    if (be instanceof EnergyHolder holder) {
                        tmp.add(holder);
                    }
                }
            }
            tmp.sort(Comparator.comparingDouble(h -> (h.getStoredEnergy() / h.getMaxStorage())
                    - (h.canProvideOutput(RF.getInstance()) ? 0 : 1)));
            return sortedMachines = tmp;
        } else {
            return sortedMachines;
        }
    }
}
