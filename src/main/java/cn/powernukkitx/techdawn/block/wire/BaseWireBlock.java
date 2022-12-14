package cn.powernukkitx.techdawn.block.wire;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.powernukkitx.techdawn.energy.RF;
import org.jetbrains.annotations.NotNull;

public class BaseWireBlock extends BlockTransparentMeta implements CustomBlock {
    public static IntBlockProperty X_LINK = new IntBlockProperty("pnx:x_link", true, 3, 0);
    public static IntBlockProperty Y_LINK = new IntBlockProperty("pnx:y_link", true, 3, 0);
    public static IntBlockProperty Z_LINK = new IntBlockProperty("pnx:z_link", true, 3, 0);
    // 需要以FNV164序列排序
    public static BlockProperties PROPERTIES = new BlockProperties(Z_LINK, Y_LINK, X_LINK);

    public BaseWireBlock() {
        super();
        try {
            getMutableState().setIntValue(X_LINK, 0);
            getMutableState().setIntValue(Y_LINK, 0);
            getMutableState().setIntValue(Z_LINK, 0);
        } catch (NullPointerException ignore) {
            // 启动时肯定会有一次NPE，意料之中
        }
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public String getNamespaceId() {
        return "techdawn:base_wire";
    }

    public String getTextureName() {
        return "techdawn-blocks-wire-base_wire";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition
                .builder(this, Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getTextureName()))
                .geometry("geometry.pipe")
                .customBuild(nbt -> componentNBTProcessor(nbt.getCompound("components")));
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

    public void setFaceLinked(BlockFace face, boolean linked) {
        switch (face) {
            case WEST -> {
                if (linked) {
                    getMutableState().setIntValue(X_LINK, getMutableState().getIntValue(X_LINK) | 0b01);
                } else {
                    getMutableState().setIntValue(X_LINK, getMutableState().getIntValue(X_LINK) & 0b10);
                }
            }
            case EAST -> {
                if (linked) {
                    getMutableState().setIntValue(X_LINK, getMutableState().getIntValue(X_LINK) | 0b10);
                } else {
                    getMutableState().setIntValue(X_LINK, getMutableState().getIntValue(X_LINK) & 0b01);
                }
            }
            case UP -> {
                if (linked) {
                    getMutableState().setIntValue(Y_LINK, getMutableState().getIntValue(Y_LINK) | 0b01);
                } else {
                    getMutableState().setIntValue(Y_LINK, getMutableState().getIntValue(Y_LINK) & 0b10);
                }
            }
            case DOWN -> {
                if (linked) {
                    getMutableState().setIntValue(Y_LINK, getMutableState().getIntValue(Y_LINK) | 0b10);
                } else {
                    getMutableState().setIntValue(Y_LINK, getMutableState().getIntValue(Y_LINK) & 0b01);
                }
            }
            case SOUTH -> {
                if (linked) {
                    getMutableState().setIntValue(Z_LINK, getMutableState().getIntValue(Z_LINK) | 0b01);
                } else {
                    getMutableState().setIntValue(Z_LINK, getMutableState().getIntValue(Z_LINK) & 0b10);
                }
            }
            case NORTH -> {
                if (linked) {
                    getMutableState().setIntValue(Z_LINK, getMutableState().getIntValue(Z_LINK) | 0b10);
                } else {
                    getMutableState().setIntValue(Z_LINK, getMutableState().getIntValue(Z_LINK) & 0b01);
                }
            }
        }
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face,
                         double fx, double fy, double fz, Player player) {
        for (var each : BlockFace.values()) {
            var side = getSide(each);
            if (side instanceof BaseWireBlock pipe) {
                pipe.setFaceLinked(each.getOpposite(), true);
                this.level.setBlock(pipe, pipe, true);
                this.setFaceLinked(each, true);
                this.level.setBlock(this, this, true);
            } else if (side.getLevelBlockEntity() instanceof EnergyHolder holder && holder.canAcceptInput(RF.getInstance())) {
                this.setFaceLinked(each, true);
                this.level.setBlock(this, this, true);
            }
        }
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Override
    public boolean onBreak(Item item) {
        for (var each : BlockFace.values()) {
            var side = getSide(each);
            if (side instanceof BaseWireBlock pipe) {
                pipe.setFaceLinked(each.getOpposite(), false);
                this.level.setBlock(pipe, pipe, true);
                this.setFaceLinked(each, false);
                this.level.setBlock(this, this, true);
            }
        }
        return super.onBreak(item);
    }

    @Override
    public int onTouch(Player player, PlayerInteractEvent.Action action) {
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            System.out.println("X_Link: " + getMutableState().getIntValue(X_LINK));
            System.out.println("Y_Link: " + getMutableState().getIntValue(Y_LINK));
            System.out.println("Z_Link: " + getMutableState().getIntValue(Z_LINK));
            System.out.println(getProperties().getAllProperties());
        }
        return super.onTouch(player, action);
    }

    private void componentNBTProcessor(@NotNull CompoundTag componentNBT) {
        // 线缆连接渲染
        componentNBT.putCompound("minecraft:part_visibility", new CompoundTag()
                .putCompound("boneConditions", new CompoundTag()
                        .putCompound("top", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:y_link') == 1 || q.block_property('pnx:y_link') == 3")
                                .putString("bone_name", "top")
                                .putInt("molang_version", 6))
                        .putCompound("bottom", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:y_link') == 2 || q.block_property('pnx:y_link') == 3")
                                .putString("bone_name", "bottom")
                                .putInt("molang_version", 6))
                        .putCompound("north", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:z_link') == 2 || q.block_property('pnx:z_link') == 3")
                                .putString("bone_name", "north")
                                .putInt("molang_version", 6))
                        .putCompound("east", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:x_link') == 1 || q.block_property('pnx:x_link') == 3")
                                .putString("bone_name", "east")
                                .putInt("molang_version", 6))
                        .putCompound("south", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:z_link') == 1 || q.block_property('pnx:z_link') == 3")
                                .putString("bone_name", "south")
                                .putInt("molang_version", 6))
                        .putCompound("west", new CompoundTag()
                                .putString("bone_condition", "q.block_property('pnx:x_link') == 2 || q.block_property('pnx:x_link') == 3")
                                .putString("bone_name", "west")
                                .putInt("molang_version", 6))));
        // 碰撞箱和选择箱
        componentNBT.putCompound("minecraft:collision_box", new CompoundTag()
                        .putBoolean("enabled", true)
                        .putList(new ListTag<FloatTag>("origin")
                                .add(new FloatTag("", -3.25f))
                                .add(new FloatTag("", 4.75f))
                                .add(new FloatTag("", -3.25f)))
                        .putList(new ListTag<FloatTag>("size")
                                .add(new FloatTag("", 6.5f))
                                .add(new FloatTag("", 6.5f))
                                .add(new FloatTag("", 6.5f))))
                .putCompound("minecraft:selection_box", new CompoundTag()
                        .putBoolean("enabled", true)
                        .putList(new ListTag<FloatTag>("origin")
                                .add(new FloatTag("", -4))
                                .add(new FloatTag("", 4))
                                .add(new FloatTag("", -4)))
                        .putList(new ListTag<FloatTag>("size")
                                .add(new FloatTag("", 8))
                                .add(new FloatTag("", 8))
                                .add(new FloatTag("", 8))));
    }

    public double getMinX() {
        return this.x + 0.296875;
    }

    public double getMinY() {
        return this.y + 0.296875;
    }

    public double getMinZ() {
        return this.z + 0.296875;
    }

    public double getMaxX() {
        return this.x + 0.703125;
    }

    public double getMaxY() {
        return this.y + 0.703125;
    }

    public double getMaxZ() {
        return this.z + 0.703125;
    }
}
