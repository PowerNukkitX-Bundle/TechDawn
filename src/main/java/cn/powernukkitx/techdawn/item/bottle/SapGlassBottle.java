package cn.powernukkitx.techdawn.item.bottle;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.potion.Effect;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class SapGlassBottle extends BaseGlassBottle {
    public SapGlassBottle() {
        super("techdawn:sap_glass_bottle", "sap_bottle");
    }

    @NotNull
    @Override
    public String getTags() {
        return "sap_bottle sap glass_bottle bottle potion";
    }

    @Override
    public List<Effect> getEffects() {
        return List.of(
                Effect.getEffect(Effect.SATURATION).setDuration(20 * 2).setAmplifier(1),
                Effect.getEffect(Effect.SLOWNESS).setDuration(20 * 15).setAmplifier(1),
                Effect.getEffect(Effect.NAUSEA).setDuration(20 * 15).setAmplifier(1)
        );
    }
}
