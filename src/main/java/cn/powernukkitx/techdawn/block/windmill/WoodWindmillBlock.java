package cn.powernukkitx.techdawn.block.windmill;

import cn.nukkit.block.BlockPlanks;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.entity.windmill.BaseWindmillEntity;
import cn.powernukkitx.techdawn.entity.windmill.WoodWindmillEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class WoodWindmillBlock extends BaseWindmillBlock implements CustomBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:wood_windmill";
    }

    @Override
    public BaseWindmillEntity createWindmillEntity() {
        return new WoodWindmillEntity(this.getChunk(), Entity.getDefaultNBT(
                // pos
                this.add(0.5, -1, 0.5).add(this.getBlockFace().getUnitVector().multiply(-0.3)),
                // motion
                Vector3.ZERO,
                // yaw
                this.getBlockFace().getOpposite().getHorizontalIndex() * 90,
                // pitch
                0
        ));
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.addParticle(new DestroyBlockParticle(this, new BlockPlanks()));
        return super.onBreak(item);
    }
}
