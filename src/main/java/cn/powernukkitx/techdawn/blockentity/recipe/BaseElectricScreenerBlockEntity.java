package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.energy.EnergyHolder;
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
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.BaseElectricScreenerBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.inventory.recipe.BaseScreenerInventory;
import cn.powernukkitx.techdawn.listener.GoldPanListener;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.ItemUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BaseElectricScreenerBlockEntity extends MachineBlockEntity implements EnergyHolder, RecipeInventoryHolder {
    protected BaseScreenerInventory inventory;
    protected float cookTime = 0;

    public BaseElectricScreenerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BaseElectricScreenerBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseElectricScreenerBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public BaseElectricScreenerBlock getBlock() {
        return (BaseElectricScreenerBlock) super.getBlock();
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType, BlockFace face) {
        return energyType.canConvertTo(RF.getInstance()) && face != getBlock().getBlockFace();
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
        return 1000;
    }

    protected @NotNull String getUITitle() {
        return "ui.techdawn.base_electric_screener";
    }

    @NotNull
    @Override
    public cn.powernukkitx.fakeInv.CustomInventory generateUI() {
        var customInv = new cn.powernukkitx.fakeInv.CustomInventory(InventoryType.FURNACE, getUITitle());
        customInv.setItem(0, inventory.getItem(0), (item, inventoryTransactionEvent) -> {
            if (InventoryUtil.isTransactionUnsafe(inventory, customInv, inventoryTransactionEvent)) {
                inventoryTransactionEvent.setCancelled();
                uiManger.update(true);
            } else {
                inventory.setItem(0, InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
                uiManger.update(true);
            }
        });
        customInv.setItem(2, inventory.getItem(1), (item, inventoryTransactionEvent) -> {
            if (InventoryUtil.isTransactionUnsafe(inventory, customInv, inventoryTransactionEvent)) {
                inventoryTransactionEvent.setCancelled();
                uiManger.update(true);
            } else {
                inventory.setItem(1, InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
                uiManger.update(true);
            }
        });
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull cn.powernukkitx.fakeInv.CustomInventory inventory, boolean immediately) {
        inventory.setItem(0, this.inventory.getItem(0));
        inventory.sendSlot(0, inventory.getViewers());
        inventory.setItem(2, this.inventory.getItem(1));
        inventory.sendSlot(2, inventory.getViewers());
        if (!immediately) {
            for (var each : inventory.getViewers()) {
                int windowId = each.getWindowId(inventory);
                if (windowId > 0) {
                    var pk = new ContainerSetDataPacket();
                    pk.windowId = windowId;
                    pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT;
                    pk.value = (int) cookTime;
                    each.dataPacket(pk);

                    pk = new ContainerSetDataPacket();
                    pk.windowId = windowId;
                    pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_LIT_TIME;
                    pk.value = (int) (getStoredEnergy() / getMaxStorage() * 200);
                    each.dataPacket(pk);
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public float getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
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

    private void setItemToNBT(int index, Item item) {
        int i = this.getSlotIndex(index);

        CompoundTag d = NBTIO.putItemHelper(item, index);

        if (item.getId() == Item.AIR || item.getCount() <= 0) {
            if (i >= 0) {
                this.namedTag.getList("Items").getAll().remove(i);
            }
        } else if (i < 0) {
            (this.namedTag.getList("Items", CompoundTag.class)).add(d);
        } else {
            (this.namedTag.getList("Items", CompoundTag.class)).add(i, d);
        }
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new BaseScreenerInventory(this, InventoryType.FURNACE);
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        } else {
            ListTag<CompoundTag> items = this.namedTag.getList("Items", CompoundTag.class);
            if (items.size() >= 1) {
                var data = (CompoundTag) this.namedTag.getList("Items").get(0);
                inventory.setInput(NBTIO.getItemHelper(data));
            }
            if (items.size() >= 2) {
                var data = (CompoundTag) this.namedTag.getList("Items").get(1);
                inventory.setResult(NBTIO.getItemHelper(data));
            }
        }

        if (!this.namedTag.contains("CookTime") || this.namedTag.getShort("CookTime") < 0 || (this.namedTag.getShort("BurnTime") == 0 && this.namedTag.getShort("CookTime") > 0)) {
            cookTime = 0;
        } else {
            cookTime = this.namedTag.getShort("CookTime");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.inventory.getSize(); index++) {
            this.setItemToNBT(index, this.inventory.getItem(index));
        }

        this.namedTag.putShort("CookTime", (short) cookTime);
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        this.inventory.clearAll();
    }

    public void resetProgress() {
        this.cookTime = 0;
    }

    public float getSpeedMultiplier() {
        return 4f; // 2.5 seconds
    }

    public int getEnergyCostPerTick() {
        return 20;
    }

    @Override
    public boolean onUpdate() {
        var result = super.onUpdate();
        // 检查能否开始冶炼
        var input = this.inventory.getInput();
        var inputNameSpaceId = input.getNamespaceId();
        var product = this.inventory.getResult();
        // 配方是否合适
        boolean canSmelt = switch (inputNameSpaceId) {
            case "minecraft:gravel", "minecraft:sand", "techdawn:flint_gravel" -> true;
            default -> false;
        };
        // 还有没有电
        if (canSmelt && getStoredEnergy() < getEnergyCostPerTick()) canSmelt = false;

        // 开始冶炼
        if (canSmelt) {
            if (cookTime == 0) { // 开始新的冶炼进程
                this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.BLOCK_FURNACE_LIT);
                getBlock().setWorkingProperty(true);
                setStoredEnergy(getStoredEnergy() - getEnergyCostPerTick());
                cookTime += getSpeedMultiplier();
            } else if (cookTime > 0 && cookTime < 200) { // 继续冶炼
                if (cookTime % 20 == 0) {
                    this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.BLOCK_FURNACE_LIT);
                }
                setStoredEnergy(getStoredEnergy() - getEnergyCostPerTick());
                cookTime += getSpeedMultiplier();
            } else if (cookTime >= 200) { // 冶炼完成
                Object2DoubleOpenHashMap<Item> table = switch (inputNameSpaceId) {
                    case "minecraft:sand" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_SAND;
                    case "minecraft:gravel" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_GRAVEL;
                    case "techdawn:flint_gravel" -> GoldPanListener.STONE_GOLD_PAN_ITEMS_FLINT_GRAVEL;
                    default -> throw new IllegalStateException("Unexpected value: " + input.getNamespaceId());
                };

                var reslutItem = ItemUtil.randomItem(table);
                if (product == null || product.isNull()) {
                    this.inventory.setResult(reslutItem);
                } else {
                    if (reslutItem.equals(product, true, true)) {
                        reslutItem.setCount(product.getCount() + 1);
                        this.inventory.setResult(reslutItem);
                    } else if (!reslutItem.isNull()) {
                        this.level.dropItem(this.add(0.5, -0.1, 0.5), reslutItem);
                    }
                }

                input.setCount(input.getCount() - 1);
                if (input.getCount() == 0) input = AIR_ITEM;
                this.inventory.setInput(input);

                switch (inputNameSpaceId) {
                    case "minecraft:gravel", "techdawn:flint_gravel" ->
                            this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.DIG_GRAVEL, 1, 1);
                    case "minecraft:sand" -> this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.DIG_SAND, 1, 1);
                }
                this.level.addParticle(new DestroyBlockParticle(this.add(0.5, 0.5, 0.5),
                        BlockState.of(inputNameSpaceId).getBlock()));

                this.cookTime = 0;
            }
        } else {
            resetProgress();
            getBlock().setWorkingProperty(false);
        }
        return result;
    }

    @Override
    public Inventory getIngredientView() {
        return new InventorySlice(inventory, 0, 2);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(inventory, 2, 3);
    }
}
