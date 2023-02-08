package cn.powernukkitx.techdawn.item.bottle;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.potion.Effect;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class TarGlassBottle extends BaseGlassBottle {
    public TarGlassBottle() {
        super("techdawn:tar_glass_bottle", "tar_bottle");
    }

    @NotNull
    @Override
    public String getTags() {
        return "tar_bottle tar glass_bottle bottle potion";
    }

    @Override
    public List<Effect> getEffects() {
        return List.of(
                Effect.getEffect(Effect.POISON).setDuration(20 * 8).setAmplifier(1),
                Effect.getEffect(Effect.SLOWNESS).setDuration(20 * 30).setAmplifier(1)
        );
    }
}
