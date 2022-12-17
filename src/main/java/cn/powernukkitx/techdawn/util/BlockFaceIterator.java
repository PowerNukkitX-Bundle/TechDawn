package cn.powernukkitx.techdawn.util;

import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * 一个用于迭代{@link BlockFace}的迭代器，每次迭代都会改变迭代顺序
 */
public final class BlockFaceIterator implements Iterable<BlockFace> {
    private final Queue<BlockFace> queue = new ArrayDeque<>(List.of(BlockFace.values()));

    @NotNull
    @Override
    public Iterator<BlockFace> iterator() {
        queue.add(queue.poll());
        return queue.iterator();
    }
}
