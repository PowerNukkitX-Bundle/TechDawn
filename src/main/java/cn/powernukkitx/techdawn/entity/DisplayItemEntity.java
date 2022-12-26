package cn.powernukkitx.techdawn.entity;

import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.concurrent.ThreadLocalRandom;

public final class DisplayItemEntity extends EntityItem {
    public DisplayItemEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public static DisplayItemEntity createAndDisplay(Position position, Item item) {
        DisplayItemEntity itemEntity = null;

        CompoundTag itemTag = NBTIO.putItemHelper(item);
        itemTag.setName("Item");

        if (item.getId() != 0 && item.getCount() > 0) {
            itemEntity = new DisplayItemEntity(
                    position.level.getChunk((int) position.getX() >> 4, (int) position.getZ() >> 4, true),
                    new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", position.getX()))
                                    .add(new DoubleTag("", position.getY())).add(new DoubleTag("", position.getZ())))

                            .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0))
                                    .add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))

                            .putList(new ListTag<FloatTag>("Rotation")
                                    .add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360))
                                    .add(new FloatTag("", 0)))

                            .putShort("Health", 5).putCompound("Item", itemTag).putShort("PickupDelay", 999999));
            itemEntity.spawnToAll();
        }

        return itemEntity;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return (source.getCause() == EntityDamageEvent.DamageCause.NONE || source.getCause() == EntityDamageEvent.DamageCause.CUSTOM) && super.attack(source);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        return this.isClosed();
    }
}
