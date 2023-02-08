package cn.powernukkitx.techdawn.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.EntityEventPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LevelUtil {
    private LevelUtil() {
        throw new UnsupportedOperationException();
    }

    public static void resendAroundBlocks(@NotNull Position position) {
        var blocks = new Vector3[6];
        blocks[0] = position.add(1, 0, 0);
        blocks[1] = position.add(-1, 0, 0);
        blocks[2] = position.add(0, 1, 0);
        blocks[3] = position.add(0, -1, 0);
        blocks[4] = position.add(0, 0, 1);
        blocks[5] = position.add(0, 0, -1);
        position.level.sendBlocks(position.getLevel().getChunkPlayers(position.getChunkX(), position.getChunkZ()).values().toArray(Player.EMPTY_ARRAY), blocks);
    }

    public static void sendSwingArm(@Nullable Player player) {
        if (player != null) {
            var pk = new EntityEventPacket();
            pk.eid = player.getId();
            pk.event = EntityEventPacket.ARM_SWING;
            player.dataPacket(pk);
            Server.broadcastPacket(player.getViewers().values(), pk);
        }
    }
}
