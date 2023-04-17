package cn.powernukkitx.techdawn.listener;

import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import cn.powernukkitx.techdawn.Main;
import cn.powernukkitx.techdawn.energy.DynamicManager;

public final class TickListener extends PluginTask<Main> {
    private int previousTick = 0;

    public TickListener(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        if (previousTick == currentTick) return;
        previousTick = currentTick;
        for (var level : Server.getInstance().getLevels().values()) {
            DynamicManager.updateHinge(level);
        }
    }
}
