package cn.powernukkitx.techdawn.block.handle;

import cn.nukkit.block.BlockPlanks;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.entity.handle.BaseHandleEntity;
import cn.powernukkitx.techdawn.entity.handle.WoodHandleEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class WoodHandleBlock extends BaseHandleBlock implements CustomBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:wood_handle";
    }

    @Override
    public BaseHandleEntity createHandleEntity() {
        return new WoodHandleEntity(this.getChunk(), Entity.getDefaultNBT(this.add(0.5, 0, 0.5)));
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.addParticle(new DestroyBlockParticle(this, new BlockPlanks()));
        return super.onBreak(item);
    }
}
