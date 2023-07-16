package cn.powernukkitx.techdawn.blockentity.actuator;

import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventorySlice;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.RecipeInventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.PunchBlockParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.actuator.BaseElectricDiggerBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.inventory.actuator.BaseDiggerInventory;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BaseElectricDiggerBlockEntity extends MachineBlockEntity implements EnergyHolder, RecipeInventoryHolder {
    protected BaseDiggerInventory inventory;
    protected int miningTick = 0;
    protected int lastMiningBlockId = 0;
    protected boolean isMining = false;

    public BaseElectricDiggerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BaseElectricDiggerBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseElectricDiggerBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public BaseElectricDiggerBlock getBlock() {
        return (BaseElectricDiggerBlock) super.getBlock();
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
        return 6000;
    }

    protected @NotNull String getUITitle() {
        return "ui.techdawn.base_electric_digger";
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
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull cn.powernukkitx.fakeInv.CustomInventory inventory, boolean immediately) {
        inventory.setItem(0, this.inventory.getItem(0));
        inventory.sendSlot(0, inventory.getViewers());
        if (!immediately) {
            for (var each : inventory.getViewers()) {
                int windowId = each.getWindowId(inventory);
                if (windowId > 0) {
                    var pk = new ContainerSetDataPacket();
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
        this.inventory = new BaseDiggerInventory(this, InventoryType.FURNACE);
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
                inventory.setPickaxe(NBTIO.getItemHelper(data));
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.inventory.getSize(); index++) {
            this.setItemToNBT(index, this.inventory.getItem(index));
        }
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        this.inventory.clearAll();
        if (isMining) {
            this.sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_STOP_BREAK, 0, level.getBlock(this.add(0, -1)));
        }
    }

    public int getEnergyCostPerTick() {
        return 30;
    }

    @Override
    public boolean onUpdate() {
        super.onUpdate();
        if (this.closed) {
            return false;
        }
        // 检查是否是镐子
        var pickaxe = inventory.getPickaxe();
        if (pickaxe == null || pickaxe.isNull() || !pickaxe.isPickaxe()) {
            return uiManger.isUIDisplaying();
        }
        var blockUnder = level.getBlock(this.add(0, -1, 0));
        if (lastMiningBlockId != blockUnder.getId()) {
            lastMiningBlockId = blockUnder.getId();
            miningTick = 0;
            isMining = false;
            this.sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_STOP_BREAK, 0, blockUnder);
        }
        if (blockUnder.getId() == Block.AIR) {
            return uiManger.isUIDisplaying();
        }
        double miningTimeRequired;
        if (blockUnder instanceof CustomBlock customBlock) {
            miningTimeRequired = customBlock.breakTime(pickaxe, null);
        } else {
            miningTimeRequired = blockUnder.calculateBreakTime(pickaxe, null);
        }
        int breakTick = (int) Math.ceil(miningTimeRequired * 20);
        if (!isMining && getStoredEnergy() >= breakTick * getEnergyCostPerTick()) {
            isMining = true;
        }
        if (isMining && getStoredEnergy() >= getEnergyCostPerTick()) {
            this.setStoredEnergy(getStoredEnergy() - getEnergyCostPerTick());
            if (miningTick < breakTick) {
                if (miningTick == 0) {
                    sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_START_BREAK, 65535 / breakTick, blockUnder);
                }
                this.getBlock().setWorkingProperty(true);
                miningTick++;
                this.sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_UPDATE_BREAK, 65535 / breakTick, blockUnder);
                this.level.addParticle(new PunchBlockParticle(this.add(0.5, -1, 0.5), blockUnder, BlockFace.DOWN));
            } else {
                miningTick = 0;
                lastMiningBlockId = 0;
                isMining = false;
                this.getBlock().setWorkingProperty(false);
                var result = this.level.useBreakOn(blockUnder, BlockFace.UP, pickaxe, null, true);
                if (result.isNull()) {
                    level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_BREAK);
                }
                inventory.setPickaxe(result);
            }
            return true;
        }
        return uiManger.isUIDisplaying();
    }

    protected void sendBlockEventPacket(int ev, int data, @NotNull Block block) {
        var pk = new LevelEventPacket();
        pk.evid = ev;
        pk.x = (float) block.x;
        pk.y = (float) block.y;
        pk.z = (float) block.z;
        pk.data = data;
        this.level.addChunkPacket(block.getFloorX() >> 4, block.getFloorZ() >> 4, pk);
    }

    @Override
    public Inventory getIngredientView() {
        return new InventorySlice(inventory, 0, 1);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(inventory, 0, 0);
    }
}
