package cn.powernukkitx.techdawn.entity.windmill;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAsyncPrepare;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.HappyVillagerParticle;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AnimateEntityPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.windmill.BaseWindmillBlock;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.util.MathUtil;

import java.util.List;
import java.util.stream.IntStream;

import static cn.powernukkitx.techdawn.util.MathUtil.sigmod;

@AutoRegister(CustomEntity.class)
public class BaseWindmillEntity extends Entity implements CustomEntity, EntityAsyncPrepare {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:base_windmill")
            .summonable(true)
            .spawnEgg(false)
            .build();

    protected int nextAnimateTick;
    protected float windCoefficient = 0;

    public BaseWindmillEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.nextAnimateTick = Server.getInstance().getTick() + 2;
    }

    @Override
    public float getHeight() {
        return 0f;
    }

    @Override
    public float getWidth() {
        return 0f;
    }

    @Override
    public float getLength() {
        return 0f;
    }

    @Override
    public CustomEntityDefinition getDefinition() {
        return def;
    }

    @Override
    public int getNetworkId() {
        return getDefinition().getRuntimeId();
    }

    @Override
    protected float getGravity() {
        return 0f;
    }

    @Override
    public boolean isImmobile() {
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        var output = getMaxOutput() * this.windCoefficient;
        if (output >= getMinOutput()) {
            level.addParticle(new RedstoneParticle(this.add(0, 1.5).add(this.getDirectionVector())));
        }
        if (output >= getMinOutput() && level.getBlockEntity(this.add(0, 1.5).add(this.getDirectionVector()))
                instanceof EnergyHolder holder &&
                holder.canAcceptInput(Rotation.getInstance(), this.getDirection().getOpposite())) {
            holder.inputInto(Rotation.getInstance(), output);
        }
        if (currentTick >= nextAnimateTick) {
            if (output < getMinOutput()) {
                nextAnimateTick += 200;
                var animationBuilder = AnimateEntityPacket.Animation.builder();
                animationBuilder.animation("animation.techdawn.windmill.rotate_stop");
                Entity.playAnimationOnEntities(animationBuilder.build(), List.of(this));
                return super.onUpdate(currentTick);
            }
            level.addParticle(new HappyVillagerParticle(this.add(this.getDirectionVector().normalize().multiply(1.5f))));
            var second = Math.round(MathUtil.scale(-output, -getMaxOutput(), -getMinOutput(), 3, 18));
            nextAnimateTick += second * 20;
            var animationBuilder = AnimateEntityPacket.Animation.builder();
            animationBuilder.animation("animation.techdawn.windmill.rotate_" + second);
            Entity.playAnimationOnEntities(animationBuilder.build(), List.of(this));
        }
        if (!(getTickCachedLevelBlock() instanceof BaseWindmillBlock)) {
            close();
        }
        return super.onUpdate(currentTick);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (player != null && player.isCreative()) {
            this.close();
        }
        return true;
    }

    protected float getMaxOutput() {
        return 4.5f;
    }

    protected float getMinOutput() {
        return 0.75f;
    }

    @Override
    public void asyncPrepare(int currentTick) {
        if (currentTick >= nextAnimateTick) {
            int x = (int) this.x;
            int y = (int) this.y;
            int z = (int) this.z;
            var totalAirCount = IntStream.rangeClosed(0, 3).parallel().map(i -> {
                int dx = -5 + 3 * i;
                final int mx = Math.min(5, dx + 2);
                int airCount = 0;
                for (; dx <= mx; dx++) {
                    for (int dy = -5; dy <= 5; dy++) {
                        for (int dz = -5; dz <= 5; dz++) {
                            if (0 == this.getLevel().getBlockIdAt(x + dx, y + dy, z + dz)) {
                                if (dx >= -2 && dx <= 2 && dy >= -2 && dy <= 2 && dz >= -2 && dz <= 2) {
                                    airCount += 4;
                                } else {
                                    ++airCount;
                                }
                            }
                        }
                    }
                }
                return airCount;
            }).sum();
            this.windCoefficient = windOfHeight(y) * windOfFilling(1 - (totalAirCount / (11 * 11 * 11f + 5 * 5 * 5 * 3f)));
            // 雨天20%风力加成
            if (level.isRaining()) {
                this.windCoefficient = Math.min(this.windCoefficient * 1.2f, 1f);
            }
        }
    }

    public static float windOfHeight(float y) {
        return sigmod((y - 64) / 32f);
    }

    public static float windOfFilling(float filling) {
        return 1f - 2f * (sigmod(filling * 8f) - 0.5f);
    }
}
