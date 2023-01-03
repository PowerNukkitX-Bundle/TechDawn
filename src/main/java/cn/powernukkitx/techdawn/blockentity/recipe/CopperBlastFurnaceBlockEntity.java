package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventorySlice;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.RecipeInventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.construct.CopperBrickBlock;
import cn.powernukkitx.techdawn.block.machine.recipe.CopperBlastFurnaceBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.inventory.recipe.CopperBlastFurnaceInventory;
import cn.powernukkitx.techdawn.multi.MultiBlockStruct;
import cn.powernukkitx.techdawn.multi.StructPointMatcher;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_CopperBlastFurnaceBlock")
public class CopperBlastFurnaceBlockEntity extends MachineBlockEntity implements RecipeInventoryHolder, BlockEntityContainer {
    public static MultiBlockStruct COPPER_BLAST_STRUCT = null;

    protected CopperBlastFurnaceInventory inventory;
    protected int cookTime = 0;
    protected int maxBurnTime = 0;
    protected int burnTime = 0;
    protected float storedXP = 0;

    public CopperBlastFurnaceBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.scheduleUpdate();
    }

    private static class CopperBlastFurnaceMatcher implements StructPointMatcher {
        @NotNull
        private BlockFace facing;

        public CopperBlastFurnaceMatcher(@NotNull BlockFace facing) {
            this.facing = facing;
        }

        @Override
        public boolean matchAt(Block worldBlock) {
            return worldBlock instanceof CopperBlastFurnaceBlock cb && cb.getBlockFace() == facing;
        }

        @Override
        public boolean canRotate() {
            return true;
        }

        @Override
        public void rotate(BlockFace from, BlockFace to) {
            facing = MultiBlockStruct.rotate(facing, from, to);
        }

        @Override
        public StructPointMatcher copy() {
            return new CopperBlastFurnaceMatcher(facing);
        }
    }

    public static MultiBlockStruct getCopperBlastStruct() {
        if (COPPER_BLAST_STRUCT == null) COPPER_BLAST_STRUCT = new MultiBlockStruct(BlockFace.SOUTH)
                // layer 1
                .add(-1, 0, 1, new CopperBrickBlock())
                .add(0, 0, 1, new CopperBrickBlock())
                .add(1, 0, 1, new CopperBrickBlock())
                .add(-1, 0, 0, new CopperBrickBlock())
                .add(0, 0, 0, new CopperBrickBlock())
                .add(1, 0, 0, new CopperBrickBlock())
                .add(-1, 0, -1, new CopperBrickBlock())
                .add(0, 0, -1, new CopperBrickBlock())
                .add(1, 0, -1, new CopperBrickBlock())
                // layer 2
                .add(-1, 1, 1, new CopperBrickBlock())
                .add(0, 1, 1, new CopperBlastFurnaceMatcher(BlockFace.SOUTH))
                .add(1, 1, 1, new CopperBrickBlock())
                .add(-1, 1, 0, new CopperBrickBlock())
                .add(1, 1, 0, new CopperBrickBlock())
                .add(-1, 1, -1, new CopperBrickBlock())
                .add(0, 1, -1, new CopperBrickBlock())
                .add(1, 1, -1, new CopperBrickBlock())
                // layer 3
                .add(-1, 2, 1, new CopperBrickBlock())
                .add(0, 2, 1, new CopperBrickBlock())
                .add(1, 2, 1, new CopperBrickBlock())
                .add(-1, 2, 0, new CopperBrickBlock())
                .add(1, 2, 0, new CopperBrickBlock())
                .add(-1, 2, -1, new CopperBrickBlock())
                .add(0, 2, -1, new CopperBrickBlock())
                .add(1, 2, -1, new CopperBrickBlock())
                // layer 4
                .add(-1, 3, 1, new CopperBrickBlock())
                .add(0, 3, 1, new CopperBrickBlock())
                .add(1, 3, 1, new CopperBrickBlock())
                .add(-1, 3, 0, new CopperBrickBlock())
                .add(1, 3, 0, new CopperBrickBlock())
                .add(-1, 3, -1, new CopperBrickBlock())
                .add(0, 3, -1, new CopperBrickBlock())
                .add(1, 3, -1, new CopperBrickBlock());
        return COPPER_BLAST_STRUCT;
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof CopperBlastFurnaceBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_CopperBlastFurnaceBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return false;
    }

    @Override
    public CopperBlastFurnaceBlock getBlock() {
        return (CopperBlastFurnaceBlock) super.getBlock();
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return false;
    }

    @Override
    public double getMaxStorage() {
        return 0;
    }

    @Override
    public double getStoredEnergy() {
        return 0;
    }

    @NotNull
    @Override
    public CustomInventory generateUI() {
        var customInv = new CustomInventory(InventoryType.FURNACE, "ui.techdawn.copper_blast_furnace");
        customInv.setItem(0, inventory.getItem(0), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setSmelting(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setItem(1, inventory.getItem(1), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setFuel(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setItem(2, inventory.getItem(2), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setResult(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
            dropXP(inventoryTransactionEvent.getTransaction().getSource());
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
        if (!immediately) {
            for (var each : inventory.getViewers()) {
                int windowId = each.getWindowId(inventory);
                if (windowId > 0) {
                    var pk = new ContainerSetDataPacket();
                    pk.windowId = windowId;
                    pk.property = ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT;
                    pk.value = cookTime;
                    each.dataPacket(pk);

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
    public Inventory getInventory() {
        return inventory;
    }

    public float getStoredXP() {
        return storedXP;
    }

    public void setStoredXP(float storedXP) {
        this.storedXP = storedXP;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public void setMaxBurnTime(int maxBurnTime) {
        this.maxBurnTime = maxBurnTime;
    }

    @Override
    public int getSize() {
        return 3;
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
    protected void initBlockEntity() {
        this.inventory = new CopperBlastFurnaceInventory(this, InventoryType.FURNACE);
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        } else {
            ListTag<CompoundTag> items = this.namedTag.getList("Items", CompoundTag.class);
            if (items.size() >= 2) {
                var data = (CompoundTag) this.namedTag.getList("Items").get(0);
                inventory.setSmelting(NBTIO.getItemHelper(data));
                data = (CompoundTag) this.namedTag.getList("Items").get(1);
                inventory.setResult(NBTIO.getItemHelper(data));
            }
        }

        // cookTime
        if (!this.namedTag.contains("CookTime") || this.namedTag.getShort("CookTime") < 0 || (this.namedTag.getShort("BurnTime") == 0 && this.namedTag.getShort("CookTime") > 0)) {
            cookTime = 0;
        } else {
            cookTime = this.namedTag.getShort("CookTime");
        }
        // burnTime
        if (!this.namedTag.contains("BurnTime") || this.namedTag.getShort("BurnTime") < 0) {
            burnTime = 0;
        } else {
            burnTime = this.namedTag.getShort("BurnTime");
        }
        // maxBurnTime
        if (!this.namedTag.contains("MaxBurnTime") || this.namedTag.getShort("MaxBurnTime") < 0) {
            maxBurnTime = 0;
        } else {
            maxBurnTime = this.namedTag.getShort("MaxBurnTime");
        }
        // storedXp
        if (this.namedTag.contains("StoredXpInt")) {
            storedXP = this.namedTag.getShort("StoredXpInt");
        } else {
            storedXP = 0;
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.getSize(); index++) {
            this.setItem(index, this.inventory.getItem(index));
        }

        this.namedTag.putShort("CookTime", cookTime);
        this.namedTag.putShort("BurnTime", burnTime);
        this.namedTag.putShort("MaxBurnTime", maxBurnTime);
        this.namedTag.putShort("StoredXpInt", (int) storedXP);
    }

    public short calculateXpDrop() {
        return (short) (Math.floor(this.storedXP) + (ThreadLocalRandom.current().nextFloat() < (this.storedXP % 1) ? 1 : 0));
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        this.inventory.clearAll();
        dropXP(this);
    }

    public void dropXP(Vector3 pos) {
        var xp = calculateXpDrop();
        if (xp > 0) {
            setStoredXP(0);
            level.dropExpOrb(pos, xp);
        }
    }

    public void resetProgress() {
        this.cookTime = 0;
    }

    public int getSpeedMultiplier() {
        return 2;
    }

    public int getEnergyCostPerTick() {
        return 15;
    }

    @Override
    public boolean onUpdate() {
        var result = super.onUpdate();
        if (Server.getInstance().getTick() % 16 == 0) {
            var face = getBlock().getBlockFace();
            getBlock().setWorkingProperty(getCopperBlastStruct().transpose(face).match(this.add(-face.getXOffset(), -1, -face.getZOffset())));
        }
//        // 检查能否开始冶炼
//        Item raw = this.inventory.getSmelting();
//        Item product = this.inventory.getResult();
//        SmeltingRecipe smelt = this.server.getCraftingManager().matchFurnaceRecipe(raw);
//        // 配方是否合适
//        boolean canSmelt = false;
//        if (smelt != null) {
//            canSmelt = (raw.getCount() > 0 && ((smelt.getResult().equals(product, true) && product.getCount() < product.getMaxStackSize()) || product.getId() == Item.AIR));
//            //检查输入
//            if (!smelt.getInput().equals(raw, true, false)) {
//                canSmelt = false;
//            }
//        }
//        // 还有没有电
//        if (canSmelt && getStoredEnergy() < getEnergyCostPerTick()) canSmelt = false;
//
//        // 开始冶炼
//        if (canSmelt) {
//            if (cookTime == 0) { // 开始新的冶炼进程
//                this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.BLOCK_FURNACE_LIT);
//                getBlock().setWorkingProperty(true);
//                setStoredEnergy(getStoredEnergy() - getEnergyCostPerTick());
//                cookTime += getSpeedMultiplier();
//            } else if (cookTime > 0 && cookTime < 200) { // 继续冶炼
//                this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.BLOCK_FURNACE_LIT);
//                setStoredEnergy(getStoredEnergy() - getEnergyCostPerTick());
//                cookTime += getSpeedMultiplier();
//            } else if (cookTime >= 200) { // 冶炼完成
//                var smeltResult = smelt.getResult().clone();
//                smeltResult.setCount(smeltResult.getCount() + product.getCount());
//                this.inventory.setResult(smeltResult);
//                raw.setCount(raw.getCount() - 1);
//                if (raw.getCount() == 0) raw = AIR_ITEM;
//                this.inventory.setSmelting(raw);
//                storedXP += (float) this.server.getCraftingManager().getRecipeXp(smelt);
//                cookTime = 0;
//            }
//        } else {
//            resetProgress();
//            getBlock().setWorkingProperty(false);
//        }
        return true;
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
