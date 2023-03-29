package cn.powernukkitx.techdawn.block.handle;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3f;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.entity.handle.BaseHandleEntity;
import cn.powernukkitx.techdawn.util.LevelUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

@AutoRegister(CustomBlock.class)
public class BaseHandleBlock extends BlockTransparent implements CustomBlock {
    private static final Cache<BlockVector3, BaseHandleEntity> entityCache = Caffeine.newBuilder()
            .maximumSize(1024)
            .expireAfterWrite(Duration.ofMinutes(10))
            .weakKeys()
            .build();

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:base_handle";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                        .any(Materials.RenderMethod.BLEND, "techdawn-blocks-transparent"))
                .selectionBox(new Vector3f(-7f, 0f, -7f), new Vector3f(14f, 8f, 14f))
                .collisionBox(new Vector3f(-7f, 0f, -7f), new Vector3f(14f, 8f, 14f)).build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    public BaseHandleEntity createHandleEntity() {
        return new BaseHandleEntity(this.getChunk(), Entity.getDefaultNBT(this.add(0.5, 0, 0.5)));
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        var sr = super.place(item, block, target, face, fx, fy, fz, player);
        if (sr) {
            this.createHandleEntity().spawnToAll();
        }
        return sr;
    }

    @NotNull
    public BaseHandleEntity getHandleEntity() {
        return Objects.requireNonNull(getHandleEntity(false));
    }

    @Nullable
    public BaseHandleEntity getHandleEntity(boolean doNotCreate) {
        return entityCache.get(this.asBlockVector3(), k -> {
            var tmp = this.level.getChunkEntities(this.getChunkX(), this.getChunkZ()).values().stream()
                    .filter(entity -> entity instanceof BaseHandleEntity)
                    .filter(entity -> entity.getFloorX() == this.getFloorX() && entity.getFloorY() == this.getFloorY() && entity.getFloorZ() == this.getFloorZ())
                    .findAny();
            if (tmp.isPresent()) {
                return (BaseHandleEntity) tmp.get();
            } else if (!doNotCreate) {
                var e = this.createHandleEntity();
                e.spawnToAll();
                return e;
            }
            return null;
        });
    }

    @Override
    public boolean onBreak(Item item) {
        var tmp = getHandleEntity(true);
        if (tmp != null) {
            tmp.close();
        }
        entityCache.invalidate(this.asBlockVector3());
        return super.onBreak(item);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        getHandleEntity().onPlayerInteract(player);
        if (item.canBePlaced()) LevelUtil.resendAroundBlocks(this, player);
        return true;
    }

    @Override
    public int getLightFilter() {
        return 0;
    }

    @Override
    public double getMaxX() {
        return this.x + 0.875;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5;
    }

    @Override
    public double getMaxZ() {
        return this.z + 0.875;
    }

    @Override
    public double getMinX() {
        return this.x + 0.125;
    }

    @Override
    public double getMinY() {
        return this.y;
    }

    @Override
    public double getMinZ() {
        return this.z + 0.125;
    }
}
