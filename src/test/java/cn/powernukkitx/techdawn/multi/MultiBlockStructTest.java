package cn.powernukkitx.techdawn.multi;

import cn.nukkit.block.BlockStone;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultiBlockStructTest {
    @Test
    public void testTranspose1() {
        var mb = new MultiBlockStruct(BlockFace.SOUTH)
                .add(0, 0, -1, new BlockStone());
        System.out.println(mb);
        System.out.println(mb.transpose(BlockFace.NORTH));
    }

    @Test
    public void testRotate1() {
        Assertions.assertSame(BlockFace.SOUTH, MultiBlockStruct.rotate(BlockFace.NORTH, BlockFace.NORTH, BlockFace.SOUTH));
        Assertions.assertSame(BlockFace.EAST, MultiBlockStruct.rotate(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST));
        Assertions.assertSame(BlockFace.NORTH, MultiBlockStruct.rotate(BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST));
    }

    @Test
    public void testRotate2() {
        Assertions.assertEquals(new BlockVector3(0, 0, -1), MultiBlockStruct.rotate(new BlockVector3(0, 0, 1), BlockFace.NORTH, BlockFace.SOUTH));
        Assertions.assertEquals(new BlockVector3(1, 3, 1), MultiBlockStruct.rotate(new BlockVector3(-1, 3, 1), BlockFace.NORTH, BlockFace.EAST));
        Assertions.assertEquals(new BlockVector3(3, 4, 5), MultiBlockStruct.rotate(new BlockVector3(3, 4, 5), BlockFace.WEST, BlockFace.WEST));
    }
}
