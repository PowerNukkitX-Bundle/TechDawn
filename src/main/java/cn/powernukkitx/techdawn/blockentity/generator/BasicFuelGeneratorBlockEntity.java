package cn.powernukkitx.techdawn.blockentity.generator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.generator.BasicFuelGeneratorBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.blockentity.TechDawnGenerator;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.inventory.generator.BasicFuelGeneratorInventory;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;
import cn.powernukkitx.techdawn.util.BlockFaceIterator;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BasicFuelGeneratorBlock")
public class BasicFuelGeneratorBlockEntity extends MachineBlockEntity implements RecipeInventoryHolder, BlockEntityContainer, TechDawnGenerator {
    private final BlockFaceIterator blockFaceIterator;
    protected BasicFuelGeneratorInventory inventory;
    protected int burnTime = 0;
    protected int maxBurnTime = 0;

    public BasicFuelGeneratorBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.blockFaceIterator = new BlockFaceIterator(getBlock().getBlockFace());
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getClass() == BasicFuelGeneratorBlock.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicFuelGeneratorBlock";
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return false;
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public BasicFuelGeneratorBlock getBlock() {
        return (BasicFuelGeneratorBlock) super.getBlock();
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType, BlockFace face) {
        return canProvideOutput(energyType) && face != getBlock().getBlockFace();
    }

    @Override
    public double getMaxStorage() {
        return 24000;
    }

    @Override
    public double getGeneratingSpeed() {
        return 15;
    }

    @NotNull
    @Override
    public CustomInventory generateUI() {
        var customInv = new CustomInventory(InventoryType.FURNACE, "ui.techdawn.basic_fuel_generator");
        customInv.setItem(0, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        customInv.setItem(1, inventory.getItem(0), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/25 阻止潜在的多人刷物品
            inventory.setFuel(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull CustomInventory inventory, boolean immediately) {
        inventory.setItem(0, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        inventory.setItem(1, this.inventory.getItem(0));
        inventory.sendContents(inventory.getViewers());
        if (!immediately && burnTime != 0) {
            for (var each : inventory.getViewers()) {
                int windowId = each.getWindowId(inventory);
                if (windowId > 0) {
                    var pk = new ContainerSetDataPacket();
                    pk = new ContainerSetDataPacket();
                    pk.windowId = windowId;
                    pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_LIT_DURATION;
                    pk.value = maxBurnTime;
                    each.dataPacket(pk);

                    pk = new ContainerSetDataPacket();
                    pk.windowId = windowId;
                    pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_LIT_TIME;
                    pk.value = burnTime;
                    each.dataPacket(pk);
                }
            }
        }
    }

    @Override
    public Inventory getIngredientView() {
        return new InventorySlice(inventory, 0, 1);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(inventory, 1, 1);
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
    public Item getItem(int index) {
        int i = this.getSlotIndex(index);
        if (i < 0) {
            return new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        } else {
            CompoundTag data = (CompoundTag) this.namedTag.getList("Items").get(i);
            return NBTIO.getItemHelper(data);
        }
    }

    @Override
    public void setItem(int index, Item item) {
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
    public int getSize() {
        return 1;
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new BasicFuelGeneratorInventory(this, InventoryType.FURNACE);
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
                inventory.setFuel(NBTIO.getItemHelper(data));
            }
        }
        // load burnTime
        if (!this.namedTag.contains("BurnTime") || this.namedTag.getShort("BurnTime") < 0 || (this.namedTag.getShort("BurnTime") == 0 && this.namedTag.getShort("CookTime") > 0)) {
            burnTime = 0;
        } else {
            burnTime = this.namedTag.getShort("BurnTime");
        }
        // load maxBurnTime
        if (!this.namedTag.contains("MaxBurnTime") || this.namedTag.getShort("MaxBurnTime") < 0) {
            maxBurnTime = 0;
        } else {
            maxBurnTime = this.namedTag.getShort("MaxBurnTime");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.getSize(); index++) {
            this.setItem(index, this.inventory.getItem(index));
        }

        this.namedTag.putShort("CookTime", burnTime);
        this.namedTag.putShort("MaxBurnTime", maxBurnTime);
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
        if (this.closed) {
            return false;
        }
        var result = super.onUpdate();
        // 燃烧发电
        if (burnTime > 0) {
            --burnTime;
            this.setStoredEnergy(this.getStoredEnergy() + getGeneratingSpeed());
            result = true;
        } else {
            var fuel = inventory.getFuel();
            if (fuel != null && fuel.getId() != Item.AIR && Fuel.duration.containsKey(fuel.getId())) {
                maxBurnTime = Fuel.duration.get(fuel.getId());
                burnTime = maxBurnTime;
                var newFuel = fuel.clone();
                newFuel.setCount(newFuel.getCount() - 1);
                inventory.setFuel(newFuel);
                requestUIUpdateImmediately();
                getBlock().setWorkingProperty(true);
                result = true;
            } else {
                getBlock().setWorkingProperty(false);
            }
        }
        // 向外提供能量
        EnergyNetworkManager.outputEnergyAt(this, getGeneratingSpeed(), blockFaceIterator);
        return result;
    }
}
