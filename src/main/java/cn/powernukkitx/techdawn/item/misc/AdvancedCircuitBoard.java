package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AdvancedCircuitBoard extends BaseCircuitBoard {
    public AdvancedCircuitBoard() {
        super("techdawn:advanced_circuit_board");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_GOLD;
    }

    @NotNull
    @Override
    public String getTags() {
        return "advanced_circuit_board circuit_board";
    }
}


