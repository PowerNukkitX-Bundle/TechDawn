package cn.powernukkitx.techdawn.blockentity.actuator;

import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
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
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.StoneExtractorBlock;
import cn.powernukkitx.techdawn.block.machine.recipe.StoneGrinderBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.inventory.actuator.StoneDiggerInventory;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class StoneDiggerBlockEntity extends MachineBlockEntity implements RecipeInventoryHolder {
    protected StoneDiggerInventory inventory;
    protected int miningTick = 0;
    protected int lastMiningBlockId = 0;
    protected boolean isMining = false;
    protected String playerName = "";

    public StoneDiggerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof StoneGrinderBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_StoneDiggerBlock";
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

    @NotNull
    @Override
    public CustomInventory generateUI() {
        var title = "ui.techdawn.stone_digger";
        if (isPlayerOffline()) {
            title = "ui.techdawn.base_electric_digger.offline";
        }
        var customInv = new CustomInventory(InventoryType.FURNACE, title);
        customInv.setItem(0, inventory.getPickaxe(), (item, inventoryTransactionEvent) -> {
            // TODO: 2022/12/20 阻止潜在的多人刷物品
            inventory.setPickaxe(InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
        });
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull CustomInventory inventory, boolean immediately) {
        inventory.setItem(0, this.inventory.getPickaxe());
        inventory.sendSlot(0, inventory.getViewers());
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

    @Override
    protected void initBlockEntity() {
        this.inventory = new StoneDiggerInventory(this, InventoryType.FURNACE);
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        } else {
            ListTag<CompoundTag> items = this.namedTag.getList("Items", CompoundTag.class);
            for (int i = 0, len = Math.min(items.size(), 1); i < len; i++) {
                this.inventory.setItem(i, NBTIO.getItemHelper(items.get(i)));
            }
        }
        if (this.namedTag.contains("playerName")) {
            this.playerName = this.namedTag.getString("playerName");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        var items = new ListTag<CompoundTag>("Items");
        items.add(NBTIO.putItemHelper(getInventory().getPickaxe(), 0));
        this.namedTag.putList(items);
        this.namedTag.putString("playerName", playerName);
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

    @Override
    public Inventory getIngredientView() {
        return new InventorySlice(this.inventory, 0, 1);
    }

    @Override
    public Inventory getProductView() {
        return new InventorySlice(this.inventory, 0, 0);
    }

    @Override
    public StoneDiggerInventory getInventory() {
        return this.inventory;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isPlayerOffline() {
        return getPlayerName() != null && !getPlayerName().isEmpty() && server.getPlayer(getPlayerName()) == null;
    }

    @Override
    public boolean onUpdate() {
        super.onUpdate();
        if (this.closed) {
            return false;
        }
        // 检查玩家是否在线
        if (isPlayerOffline()) {
            return uiManger.isUIDisplaying();
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
        if (!isMining && getStoredEnergy() >= breakTick * 9) {
            isMining = true;
        }
        if (isMining && getStoredEnergy() >= 9) {
            this.setStoredEnergy(getStoredEnergy() - 9);
            if (miningTick < breakTick) {
                if (miningTick == 0) {
                    sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_START_BREAK, 65535 / breakTick, blockUnder);
                }
                miningTick++;
                this.sendBlockEventPacket(LevelEventPacket.EVENT_BLOCK_UPDATE_BREAK, 65535 / breakTick, blockUnder);
                this.level.addParticle(new PunchBlockParticle(this.add(0.5, -1, 0.5), blockUnder, BlockFace.DOWN));
            } else {
                miningTick = 0;
                lastMiningBlockId = 0;
                isMining = false;
                var result = this.level.useBreakOn(blockUnder, BlockFace.UP, pickaxe, server.getPlayer(getPlayerName()), true);
                if (result.isNull()) {
                    level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_BREAK);
                }
                inventory.setPickaxe(result);

            }
            return true;
        }
        return false;
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
}
