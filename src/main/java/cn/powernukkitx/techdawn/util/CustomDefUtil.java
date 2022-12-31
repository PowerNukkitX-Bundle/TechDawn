package cn.powernukkitx.techdawn.util;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.block.customblock.data.Permutations;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.List;

public final class CustomDefUtil {
    private CustomDefUtil() {
        throw new UnsupportedOperationException();
    }

    public static CustomBlockDefinition get4DirectionWorkingBasicMachineDef(CustomBlock block, String workingTexture, String waitingTexture) {
        return get4DirectionWorkingMachineDef(block, workingTexture, waitingTexture, "techdawn-blocks-machine-basic_electric_machine_top", "techdawn-blocks-machine-basic_machine");
    }

    public static CustomBlockDefinition get4DirectionWorkingMachineDef(CustomBlock block, String workingTexture, String waitingTexture, String topTexture, String sideTexture) {
        var workingMaterial = new CompoundTag("minecraft:material_instances").putCompound("materials", Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, topTexture)
                .south(Materials.RenderMethod.OPAQUE, workingTexture)
                .any(Materials.RenderMethod.OPAQUE, sideTexture)
                .build()).putCompound("mappings", new CompoundTag());
        var waitingMaterial = new CompoundTag("minecraft:material_instances").putCompound("materials", Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, topTexture)
                .south(Materials.RenderMethod.OPAQUE, waitingTexture)
                .any(Materials.RenderMethod.OPAQUE, sideTexture)
                .build()).putCompound("mappings", new CompoundTag());
        return CustomBlockDefinition.builder(block, Materials.builder())
                .permutations(new Permutations(
                        Permutation.builder().condition("q.block_property('direction') == 0 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 1 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 270)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 2 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 180)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 3 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 90)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 0 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 1 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 270)
                                                .putFloat("z", 0),
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 2 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 180)
                                                .putFloat("z", 0),
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 3 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 90)
                                                .putFloat("z", 0),
                                        workingMaterial
                                ))
                ))
                .customBuild(nbt -> nbt.getCompound("components").remove("minecraft:material_instances"));
    }
}
