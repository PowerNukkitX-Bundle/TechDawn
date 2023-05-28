package cn.powernukkitx.techdawn.block.windmill;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.*;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.*;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.entity.windmill.BaseWindmillEntity;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

@AutoRegister(CustomBlock.class)
public class BaseWindmillBlock extends BlockTransparentMeta implements CustomBlock, Faceable {
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION);

    private static final Cache<BlockVector3, BaseWindmillEntity> entityCache = Caffeine.newBuilder()
            .maximumSize(1024)
            .expireAfterWrite(Duration.ofMinutes(10))
            .weakKeys()
            .build();

    public BaseWindmillBlock() {
        super();
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:base_windmill";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                        .any(Materials.RenderMethod.BLEND, "techdawn-blocks-transparent"))
                .permutation(new Permutation(Component.builder()
                        .selectionBox(new SelectionBox(-8, 0, -8, 16, 16, 8))
                        .collisionBox(new CollisionBox(-8, 0, -8, 16, 16, 8))
                        .build(), "q.block_property('direction') == 0"))
                .permutation(new Permutation(Component.builder()
                        .selectionBox(new SelectionBox(-8, 0, -8, 8, 16, 16))
                        .collisionBox(new CollisionBox(-8, 0, -8, 8, 16, 16))
                        .build(), "q.block_property('direction') == 1"))
                .permutation(new Permutation(Component.builder()
                        .selectionBox(new SelectionBox(-8, 0, 0, 16, 16, 8))
                        .collisionBox(new CollisionBox(-8, 0, 0, 16, 16, 8))
                        .build(), "q.block_property('direction') == 2"))
                .permutation(new Permutation(Component.builder()
                        .selectionBox(new SelectionBox(0, 0, -8, 8, 16, 16))
                        .collisionBox(new CollisionBox(0, 0, -8, 8, 16, 16))
                        .build(), "q.block_property('direction') == 3"))
                .build();
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        var sr = super.place(item, block, target, face, fx, fy, fz, player);
        if (sr) {
            this.createWindmillEntity().spawnToAll();
        }
        return sr;
    }

    public BaseWindmillEntity createWindmillEntity() {
        return new BaseWindmillEntity(this.getChunk(), Entity.getDefaultNBT(
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

    @NotNull
    public BaseWindmillEntity getWindmillEntity() {
        return Objects.requireNonNull(getWindmillEntity(false));
    }

    @Nullable
    public BaseWindmillEntity getWindmillEntity(boolean doNotCreate) {
        return entityCache.get(this.asBlockVector3(), k -> {
            var entityPos = this.add(0.5, -1, 0.5).add(this.getBlockFace().getUnitVector().multiply(-0.3));
            var tmp = this.level.fastNearbyEntities(new SimpleAxisAlignedBB(
                            this.x - 2, this.y - 2, this.z - 2, this.x + 2, this.y + 2, this.z + 2
                    )).stream()
                    .filter(entity -> entity instanceof BaseWindmillEntity)
                    .filter(entity -> entity.distanceManhattan(entityPos) < 0.1)
                    .findAny();
            if (tmp.isPresent()) {
                return (BaseWindmillEntity) tmp.get();
            } else if (!doNotCreate) {
                var e = this.createWindmillEntity();
                e.spawnToAll();
                return e;
            }
            return null;
        });
    }

    @Override
    public boolean onBreak(Item item) {
        var tmp = getWindmillEntity(true);
        if (tmp != null) {
            tmp.close();
        }
        entityCache.invalidate(this.asBlockVector3());
        return super.onBreak(item);
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, face);
    }

    @Override
    public int getLightFilter() {
        return 0;
    }

    @Override
    public double getMaxX() {
        return this.x + switch (this.getBlockFace()) {
            case NORTH, SOUTH -> 0.5;
            case EAST, WEST -> 1;
            default -> 0;
        };
    }

    @Override
    public double getMaxY() {
        return this.y + 1;
    }

    @Override
    public double getMaxZ() {
        return this.z + switch (this.getBlockFace()) {
            case NORTH, SOUTH -> 1;
            case EAST, WEST -> 0.5;
            default -> 0;
        };
    }

    @Override
    public double getMinX() {
        return this.x + switch (this.getBlockFace()) {
            case NORTH, SOUTH -> 0.5;
            default -> 0;
        };
    }

    @Override
    public double getMinY() {
        return this.y;
    }

    @Override
    public double getMinZ() {
        return this.z + switch (this.getBlockFace()) {
            case EAST, WEST -> 0.5;
            default -> 0;
        };
    }
}
