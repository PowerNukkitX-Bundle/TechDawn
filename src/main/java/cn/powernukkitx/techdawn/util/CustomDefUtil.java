package cn.powernukkitx.techdawn.util;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.block.customblock.data.Transformation;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CustomDefUtil {
    private static final @NotNull Vector3 ONE = new Vector3(1, 1, 1);

    private CustomDefUtil() {
        throw new UnsupportedOperationException();
    }

    public static CustomBlockDefinition get4DirectionWorkingBasicMachineDef(CustomBlock block, String workingTexture, String waitingTexture) {
        return get4DirectionWorkingMachineDef(block, workingTexture, waitingTexture, "techdawn-blocks-machine-basic_electric_machine_top", "techdawn-blocks-machine-basic_machine");
    }

    public static CustomBlockDefinition get4DirectionWorkingMachineDef(CustomBlock block, String workingTexture, String waitingTexture, String topTexture, String sideTexture) {
        var workingMaterial = Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, topTexture)
                .south(Materials.RenderMethod.OPAQUE, workingTexture)
                .any(Materials.RenderMethod.OPAQUE, sideTexture);
        var waitingMaterial = Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, topTexture)
                .south(Materials.RenderMethod.OPAQUE, waitingTexture)
                .any(Materials.RenderMethod.OPAQUE, sideTexture);
        return CustomBlockDefinition.builder(block, Materials.builder())
                .permutations(new Permutation(Component.builder().materialInstances(waitingMaterial).build(),
                                "q.block_property('direction') == 0 && q.block_property('working') == 0"),
                        new Permutation(Component.builder().materialInstances(waitingMaterial).transformation(fromRotation(new Vector3f(0, 270, 0))).build(),
                                "q.block_property('direction') == 1 && q.block_property('working') == 0"),
                        new Permutation(Component.builder().materialInstances(waitingMaterial).transformation(fromRotation(new Vector3f(0, 180, 0))).build(),
                                "q.block_property('direction') == 2 && q.block_property('working') == 0"),
                        new Permutation(Component.builder().materialInstances(waitingMaterial).transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('direction') == 3 && q.block_property('working') == 0"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).build(),
                                "q.block_property('direction') == 0 && q.block_property('working') == 1"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 270, 0))).build(),
                                "q.block_property('direction') == 1 && q.block_property('working') == 1"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 180, 0))).build(),
                                "q.block_property('direction') == 2 && q.block_property('working') == 1"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('direction') == 3 && q.block_property('working') == 1"))
                .customBuild(nbt -> nbt.getCompound("components").remove("minecraft:material_instances"));
    }

    @Contract("_ -> new")
    public static @NotNull Transformation fromRotation(@NotNull Vector3f rotation) {
        rotation.x /= 90;
        rotation.y /= 90;
        rotation.z /= 90;
        return new Transformation(Vector3.ZERO, ONE, rotation.asVector3());
    }
}
