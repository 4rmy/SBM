package dev.army.Farming;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.army.Core.TablistParser;
import dev.army.SkyblockMod;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.client.renderer.RenderPipelines.LINES_SNIPPET;
import static net.minecraft.client.renderer.RenderPipelines.MATRICES_FOG_SNIPPET;

public class FarmingRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static final RenderPipeline.Snippet NO_DEPTH_LINES_SNIPPET =
            RenderPipeline.builder(MATRICES_FOG_SNIPPET)
                    .withVertexShader("core/rendertype_lines")
                    .withFragmentShader("core/rendertype_lines")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withCull(false)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
                    .withPrimitiveTopology(PrimitiveTopology.LINES)
                    .withDepthStencilState(Optional.empty())
                    .buildSnippet();
    private static final RenderPipeline NO_DEPTH_LINES_PIPE = RenderPipeline
            .builder(NO_DEPTH_LINES_SNIPPET)
            .withLocation(SkyblockMod.id("no_depth_lines"))
            .build();
    private static final RenderType NO_DEPTH_LINES = RenderType.create(
            "lines_no_cull",
            RenderSetup.builder(NO_DEPTH_LINES_PIPE)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                    .createRenderSetup());

    private List<VoxelShape> barnShapes = new ArrayList<>();
    private List<VoxelShape> pestShapes = new ArrayList<>();

    public void plotBorderExtraction(LevelExtractionContext ctx) {
        if (!TablistParser.getArea().equals("garden")) return;
        barnShapes.clear();

        for (Integer i : FarmingTablist.getPestPlots()) {
            FarmingCore.PosRange rng = FarmingCore.cordToPos(FarmingCore.grid.get(i));
            barnShapes.add(Shapes.box(
                    rng.start().x, rng.start().y, rng.start().z,
                    rng.end().x, rng.end().y, rng.end().z
            ));
        }
    }

    public void plotBorderRender(LevelRenderContext context) {
        if (!TablistParser.getArea().equals("garden")) return;
        assert mc.player != null;
        if (mc.player.getMainHandItem().isEmpty()) return;
        if (!mc.player.getMainHandItem().getDisplayName().getString().toLowerCase().contains("vacuum")) return;

        if (barnShapes.isEmpty()) return;

        PoseStack poseStack = context.poseStack();
        poseStack.pushPose();
        poseStack.translate(context.gameRenderer().mainCamera().position().scale(-1));

        for (VoxelShape s : barnShapes)
            context.submitNodeCollector()
                    .submitShapeOutline(
                            context.poseStack(),
                            s,
                            RenderTypes.lines(),
                            0xFFFF5555,
                            3.0f,
                            true
                    );

        poseStack.popPose();
    }

    public void pestsOutlineExtraction(LevelExtractionContext levelExtractionContext) {
        if (!TablistParser.getArea().equals("garden")) return;
        pestShapes.clear();
        assert mc.player != null;
        FarmingCore.PosRange rng = FarmingCore.cordToPos(
                FarmingCore.posToCord(
                        mc.player.position()
                )
        );
        assert mc.level != null;
        List<Entity> entities = mc.level.getEntities(mc.player, new AABB(rng.start(), rng.end())).stream()
                .filter(e -> e.getCustomName() != null)
                .filter(e -> e.getCustomName().getString().contains("❤"))
                .toList();

        for (Entity e : entities) {
            pestShapes.add(Shapes.create(
                    new AABB(e.position().subtract(.3, .3, .3),
                            e.position().add(.3, .3, .3))
            ));
        }
    }

    public void pestsOutlineRender(LevelRenderContext context) {
        if (!TablistParser.getArea().equals("garden")) return;
        if (pestShapes.isEmpty()) return;

        PoseStack poseStack = context.poseStack();
        poseStack.pushPose();
        poseStack.translate(context.gameRenderer().mainCamera().position().scale(-1));

        for (VoxelShape s : pestShapes)
            context.submitNodeCollector()
                    .submitShapeOutline(
                            context.poseStack(),
                            s,
                            NO_DEPTH_LINES,
                            0xFFFF55FF,
                            2.0f,
                            true
                    );

        poseStack.popPose();
    }
}

// === NOTES ===
/*
    Barn Range:
    <-48, 66, 47> // bottom left
          to
    <47, 72, -48> // top right

    Plot 1 Range:
    <-48, 66, -49> // bottom left
    to
    <47, 72, -142> // top right

    Garden layout:
    21 13 09 14 22
    15 05 01 06 16
    10 02 xx 03 11
    17 07 04 08 18
    23 19 12 20 24
*/