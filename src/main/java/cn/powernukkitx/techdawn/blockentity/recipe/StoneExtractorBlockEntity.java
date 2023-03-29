package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventorySlice;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.RecipeInventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.StoneExtractorBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.inventory.recipe.StoneExtractorInventory;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class StoneExtractorBlockEntity extends MachineBlockEntity implements RecipeInventoryHolder {
    protected StoneExtractorInventory inventory;

    public StoneExtractorBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.scheduleUpdate();
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof StoneExtractorBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_StoneExtractorBlock";
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
    public StoneExtractorBlock getBlock() {
        return (StoneExtractorBlock) super.getBlock();
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
        return 360;
    }

    @NotNull
    @Override
    public CustomInventory generateUI() {
        var customInv = new CustomInventory(InventoryType.FURNACE, "ui.techdawn.stone_extractor");
        customInv.setItem(0, inventory.getItem(0), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setInput(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setItem(1, inventory.getItem(1), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setExtracted(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setItem(2, inventory.getItem(2), (item, inventoryTransactionEvent) -> {
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
        inventory.setItem(1, this.inventory.getItem(1));
        inventory.sendSlot(1, inventory.getViewers());
        inventory.setItem(2, this.inventory.getItem(2));
        inventory.sendSlot(2, inventory.getViewers());
        for (var each : inventory.getViewers()) {
            int windowId = each.getWindowId(inventory);
            if (windowId > 0) {
                var pk = new ContainerSetDataPacket();
                pk.windowId = windowId;
                pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT;
                pk.value = (int) ((getStoredEnergy() / 360) * 200);
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
        this.inventory = new StoneExtractorInventory(this, InventoryType.FURNACE);
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        } else {
            ListTag<CompoundTag> items = this.namedTag.getList("Items", CompoundTag.class);
            for (int i = 0, len = Math.min(items.size(), 3); i < len; i++) {
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
        Item input = this.inventory.getInput();
        Item extracted = this.inventory.getExtracted();
        Item product = this.inventory.getResult();
        var smelt = this.server.getCraftingManager().matchModProcessRecipe("extracting", List.of(input, extracted));
        // 配方是否合适
        boolean canSmelt = false;
        if (smelt != null) {
            canSmelt = (input.getCount() > 0 && extracted.getCount() > 0 && ((smelt.getResult().equals(product, true) && product.getCount() < product.getMaxStackSize()) || product.getId() == Item.AIR));
            //检查输入
            if (!(smelt.getIngredients().get(0).match(input) || smelt.getIngredients().get(1).match(input))) {
                canSmelt = false;
            }
            if (!(smelt.getIngredients().get(0).match(extracted) || smelt.getIngredients().get(1).match(extracted))) {
                canSmelt = false;
            }
        }
        if (canSmelt) {
            if (getStoredEnergy() == getMaxStorage()) {
                var result = smelt.getResult().clone();
                result.setCount(result.getCount() + product.getCount());
                this.inventory.setResult(result);

                input.setCount(input.getCount() - 1);
                if (input.getCount() == 0) input = AIR_ITEM;
                this.inventory.setInput(input);

                extracted.setCount(extracted.getCount() - 1);
                if (extracted.getCount() == 0) extracted = AIR_ITEM;
                this.inventory.setExtracted(extracted);

                setStoredEnergy(0);
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
        return new InventorySlice(inventory, 0, 2);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(inventory, 2, 3);
    }
}
