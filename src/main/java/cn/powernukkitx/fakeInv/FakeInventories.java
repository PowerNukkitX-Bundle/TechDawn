package cn.powernukkitx.fakeInv;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryType;
import cn.powernukkitx.fakeInv.block.DoubleFakeBlock;
import cn.powernukkitx.fakeInv.block.FakeBlock;
import cn.powernukkitx.fakeInv.block.SingleFakeBlock;

import java.util.EnumMap;
import java.util.Map;

public final class FakeInventories {
    private static final Map<InventoryType, FakeBlock> FAKE_BLOCKS = new EnumMap<>(InventoryType.class);

    public static FakeBlock getFakeBlock(InventoryType inventoryType) {
        if (FAKE_BLOCKS.isEmpty()) {
            init();
        }
        return FAKE_BLOCKS.get(inventoryType);
    }

    private static void init() {
        FAKE_BLOCKS.put(InventoryType.CHEST, new SingleFakeBlock(Block.CHEST, BlockEntity.CHEST));
        FAKE_BLOCKS.put(InventoryType.ENDER_CHEST, new SingleFakeBlock(Block.ENDER_CHEST, BlockEntity.ENDER_CHEST));
        FAKE_BLOCKS.put(InventoryType.DOUBLE_CHEST, new DoubleFakeBlock(Block.CHEST, BlockEntity.CHEST));
        FAKE_BLOCKS.put(InventoryType.FURNACE, new SingleFakeBlock(Block.FURNACE, BlockEntity.FURNACE));
        FAKE_BLOCKS.put(InventoryType.WORKBENCH, new SingleFakeBlock(Block.CRAFTING_TABLE, InventoryType.WORKBENCH.getDefaultTitle()));
        FAKE_BLOCKS.put(InventoryType.BREWING_STAND, new SingleFakeBlock(Block.BREWING_STAND_BLOCK, BlockEntity.BREWING_STAND));
        FAKE_BLOCKS.put(InventoryType.DISPENSER, new SingleFakeBlock(Block.DISPENSER, InventoryType.DISPENSER.getDefaultTitle()));
        FAKE_BLOCKS.put(InventoryType.DROPPER, new SingleFakeBlock(Block.DROPPER, InventoryType.DROPPER.getDefaultTitle()));
        FAKE_BLOCKS.put(InventoryType.HOPPER, new SingleFakeBlock(Block.HOPPER_BLOCK, BlockEntity.HOPPER));
        FAKE_BLOCKS.put(InventoryType.SHULKER_BOX, new SingleFakeBlock(Block.SHULKER_BOX, BlockEntity.SHULKER_BOX));
    }

    private FakeInventories() {
        throw new UnsupportedOperationException();
    }
}
