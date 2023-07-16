package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.BaseElectricGrinderBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.inventory.recipe.BaseGrinderInventory;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BaseElectricGrinderBlockEntity extends MachineBlockEntity implements EnergyHolder, RecipeInventoryHolder {
    protected BaseGrinderInventory inventory;
    protected float cookTime = 0;

    public BaseElectricGrinderBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BaseElectricGrinderBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseElectricGrinderBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public BaseElectricGrinderBlock getBlock() {
        return (BaseElectricGrinderBlock) super.getBlock();
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
        return 4000;
    }

    protected @NotNull String getUITitle() {
        return "ui.techdawn.base_electric_grinder";
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
        this.inventory = new BaseGrinderInventory(this, InventoryType.FURNACE);
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
        return 1f; // 10 seconds
    }

    public int getEnergyCostPerTick() {
        return 20;
    }

    @Override
    public boolean onUpdate() {
        var result = super.onUpdate();
        // 检查能否开始冶炼
        Item input = this.inventory.getInput();
        Item product = this.inventory.getResult();
        var smelt = this.server.getCraftingManager().matchModProcessRecipe("grinding", List.of(input));
        // 配方是否合适
        boolean canSmelt = false;
        if (smelt != null) {
            canSmelt = (input.getCount() > 0 && ((smelt.getResult().equals(product, true) && product.getCount() < product.getMaxStackSize()) || product.getId() == Item.AIR));
            //检查输入
            if (!(smelt.getIngredients().get(0).match(input))) {
                canSmelt = false;
            }
        }
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
                var smeltResult = smelt.getResult().clone();
                smeltResult.setCount(smeltResult.getCount() + product.getCount());
                this.inventory.setResult(smeltResult);
                input.setCount(input.getCount() - 1);
                if (input.getCount() == 0) input = AIR_ITEM;
                this.inventory.setInput(input);
                cookTime = 0;
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
