package cn.powernukkitx.techdawn.item.bottle;

import org.jetbrains.annotations.NotNull;

public abstract class BaseGlassBottle extends BaseBottle {
    public BaseGlassBottle(@NotNull String id, @NotNull String textureSubName) {
        super(id, "glass-" + textureSubName);
    }
}
