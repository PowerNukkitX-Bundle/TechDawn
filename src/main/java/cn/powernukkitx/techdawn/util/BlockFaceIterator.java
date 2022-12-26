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
    private final Queue<BlockFace> queue;

    public BlockFaceIterator() {
        this.queue = new ArrayDeque<>(List.of(BlockFace.values()));
    }

    public BlockFaceIterator(BlockFace... except) {
        if (except == null || except.length == 0) {
            this.queue = new ArrayDeque<>(List.of(BlockFace.values()));
        } else {
            this.queue = new ArrayDeque<>(6 - except.length);
            for (BlockFace face : BlockFace.values()) {
                boolean contains = false;
                for (BlockFace exceptFace : except) {
                    if (face == exceptFace) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    this.queue.add(face);
                }
            }
        }
    }

    @NotNull
    @Override
    public Iterator<BlockFace> iterator() {
        queue.add(queue.poll());
        return queue.iterator();
    }
}
