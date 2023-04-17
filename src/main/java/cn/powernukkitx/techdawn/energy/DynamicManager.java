package cn.powernukkitx.techdawn.energy;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockVector3;
import cn.powernukkitx.techdawn.block.hinge.BaseHingeBlock;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DynamicManager {
    private static final Map<String, Long2ObjectMap<Map<BlockVector3, Boolean>>> HingeUpdateRequestMap = new HashMap<>();

    private DynamicManager() {
        throw new UnsupportedOperationException();
    }

    public static void requestUpdateHinge(@NotNull Position position, boolean working) {
        String levelName = position.getLevel().getName();
        Long2ObjectMap<Map<BlockVector3, Boolean>> map = HingeUpdateRequestMap.computeIfAbsent(levelName, k -> new Long2ObjectOpenHashMap<>());
        map.computeIfAbsent(Level.chunkHash(position.getChunkX(), position.getChunkZ()), i -> new HashMap<>())
                .putIfAbsent(position.asBlockVector3(), working);
    }

    public static void requestUpdateHinge(@NotNull List<Position> position, @NotNull String levelName, boolean working) {
        Long2ObjectMap<Map<BlockVector3, Boolean>> map = HingeUpdateRequestMap.computeIfAbsent(levelName, k -> new Long2ObjectOpenHashMap<>());
        for (var pos : position) {
            map.computeIfAbsent(Level.chunkHash(pos.getChunkX(), pos.getChunkZ()), i -> new HashMap<>())
                    .putIfAbsent(pos.asBlockVector3(), working);
        }
    }

    public static void updateHinge(@NotNull Level level) {
        String levelName = level.getName();
        Long2ObjectMap<Map<BlockVector3, Boolean>> map = HingeUpdateRequestMap.get(levelName);
        if (map == null) {
            return;
        }
        map.long2ObjectEntrySet().parallelStream().forEach(e -> {
            var updateMap = e.getValue();
            for (var each : updateMap.entrySet()) {
                var pos = each.getKey();
                var x = pos.x;
                var y = pos.y;
                var z = pos.z;
                var block = level.getBlock(x, y, z);
                if (block instanceof BaseHingeBlock baseHingeBlock) {
                    baseHingeBlock.setWorkingProperty(each.getValue());
                }
            }
        });
        map.clear();
    }
}
