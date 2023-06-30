package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventorySlice;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.RecipeInventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.StoneScreenerBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.inventory.recipe.StoneScreenerInventory;
import cn.powernukkitx.techdawn.listener.GoldPanListener;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.ItemUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class StoneScreenerBlockEntity extends MachineBlockEntity implements RecipeInventoryHolder {
    protected StoneScreenerInventory inventory;

    public StoneScreenerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.scheduleUpdate();
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof StoneScreenerBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_StoneScreenerBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(Rotation.getInstance());
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType, BlockFace face) {
        return canAcceptInput(energyType) && face == BlockFace.UP;
    }

    @NotNull
    @Override
    public EnergyType getStoredEnergyType() {
        return Rotation.getInstance();
    }

    @Override
    protected boolean putIntoEnergyNetwork() {
        return false;
    }

    @Override
    public StoneScreenerBlock getBlock() {
        return (StoneScreenerBlock) super.getBlock();
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return false;
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType, BlockFace face) {
        return false;
    }

    @Override
    public double getMaxStorage() {
        return 180;
    }

    @NotNull
    @Override
    public CustomInventory generateUI() {
        var customInv = new CustomInventory(InventoryType.FURNACE, "ui.techdawn.stone_screener");
        customInv.setItem(0, inventory.getItem(0), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setInput(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setItem(2, inventory.getItem(1), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setResult(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull CustomInventory inventory, boolean immediately) {
        inventory.setItem(0, this.inventory.getItem(0));
        inventory.sendSlot(0, inventory.getViewers());
        inventory.setItem(2, this.inventory.getItem(1));
        inventory.sendSlot(2, inventory.getViewers());
        for (var each : inventory.getViewers()) {
            int windowId = each.getWindowId(inventory);
            if (windowId > 0) {
                var pk = new ContainerSetDataPacket();
                pk.windowId = windowId;
                pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT;
                pk.value = (int) ((getStoredEnergy() / 180) * 200);
                each.dataPacket(pk);
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected int getSlotIndex(int index) {
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getByte("Slot") == index) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new StoneScreenerInventory(this, InventoryType.FURNACE);
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        } else {
            ListTag<CompoundTag> items = this.namedTag.getList("Items", CompoundTag.class);
            for (int i = 0, len = Math.min(items.size(), 2); i < len; i++) {
                this.inventory.setItem(i, NBTIO.getItemHelper(items.get(i)));
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        var items = new ListTag<CompoundTag>("Items");
        for (int i = 0; i < getInventory().getSize(); i++) {
            items.add(NBTIO.putItemHelper(getInventory().getItem(i), i));
        }
        this.namedTag.putList(items);
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        this.inventory.clearAll();
    }

    @Override
    public boolean onUpdate() {
        super.onUpdate();
        // 检查能否开始冶炼
        var input = this.inventory.getInput();
        var inputNameSpaceId = input.getNamespaceId();
        var product = this.inventory.getResult();
        // 配方是否合适
        boolean canSmelt = switch (inputNameSpaceId) {
            case "minecraft:gravel", "minecraft:sand", "techdawn:flint_gravel" -> true;
            default -> false;
        };
        if (canSmelt) {
            if (getStoredEnergy() == getMaxStorage()) {
                Object2DoubleOpenHashMap<Item> table = switch (inputNameSpaceId) {
                    case "minecraft:sand" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_SAND;
                    case "minecraft:gravel" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_GRAVEL;
                    case "techdawn:flint_gravel" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_FLINT_GRAVEL;
                    default -> throw new IllegalStateException("Unexpected value: " + input.getNamespaceId());
                };

                var result = ItemUtil.randomItem(table);
                if (product == null || product.isNull()) {
                    this.inventory.setResult(result);
                } else {
                    if (result.equals(product, true, true)) {
                        result.setCount(product.getCount() + 1);
                        this.inventory.setResult(result);
                    } else if (!result.isNull()) {
                        this.level.dropItem(this.add(0.5, -0.1, 0.5), result );
                    }
                }

                input.setCount(input.getCount() - 1);
                if (input.getCount() == 0) input = AIR_ITEM;
                this.inventory.setInput(input);

                setStoredEnergy(0);

                switch (inputNameSpaceId) {
                    case "minecraft:gravel", "techdawn:flint_gravel" ->
                            this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.DIG_GRAVEL, 1, 1);
                    case "minecraft:sand" ->
                            this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.DIG_SAND, 1, 1);
                }
                this.level.addParticle(new DestroyBlockParticle(this.add(0.5, 0.5, 0.5),
                        BlockState.of(inputNameSpaceId).getBlock()));
            }
            requestUIUpdateImmediately();
            return true;
        } else {
            setStoredEnergy(0);
            requestUIUpdateImmediately();
            return false;
        }
    }

    @Override
    public Inventory getIngredientView() {
        return new InventorySlice(inventory, 0, 1);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(inventory, 1, 2);
    }
}
