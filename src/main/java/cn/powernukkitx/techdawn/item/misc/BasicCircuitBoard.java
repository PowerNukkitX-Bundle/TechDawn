package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BasicCircuitBoard extends BaseCircuitBoard {
    public BasicCircuitBoard() {
        super("techdawn:basic_circuit_board","basic_circuit_board");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_GOLD;
    }

    @NotNull
    @Override
    public String getTags() {
        return "basic_circuit_board circuit_board";
    }
}
