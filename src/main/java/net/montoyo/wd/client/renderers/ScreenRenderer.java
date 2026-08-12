/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.renderers;

import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.entity.ScreenData;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.data.Rotation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** Renders each MCEF browser texture on its WebDisplays multiblock. */
public class ScreenRenderer implements BlockEntityRenderer<ScreenBlockEntity, ScreenRenderer.ScreenRenderState> {
    private record ScreenEntry(BlockSide side, int width, int height, Rotation rotation,
                               Identifier texture, float animationScale) {
    }

    public static final class ScreenRenderState extends BlockEntityRenderState {
        private final List<ScreenEntry> screens = new ArrayList<>();
    }

    public static class ScreenRendererProvider implements BlockEntityRendererProvider<ScreenBlockEntity, ScreenRenderState> {
        @Override
        public @NotNull BlockEntityRenderer<ScreenBlockEntity, ScreenRenderState> create(@NotNull Context context) {
            return new ScreenRenderer();
        }
    }

    @Override
    public ScreenRenderState createRenderState() {
        return new ScreenRenderState();
    }

    @Override
    public void extractRenderState(ScreenBlockEntity blockEntity, ScreenRenderState state, float partialTick,
                                   Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, crumblingOverlay);
        state.screens.clear();
        if (!blockEntity.isLoaded())
            return;

        for (int i = 0; i < blockEntity.screenCount(); i++) {
            ScreenData screen = blockEntity.getScreen(i);
            if (screen.browser == null) {
                double distance = WebDisplays.PROXY.distanceTo(blockEntity, cameraPos);
                if (distance <= WebDisplays.INSTANCE.loadDistance2 * 16)
                    screen.createBrowser(blockEntity, true);
            }

            if (!(screen.browser instanceof MCEFBrowser browser) || !browser.isTextureReady())
                continue;

            float scale = 1.0f;
            if (screen.doTurnOnAnim) {
                scale = Math.min(1.0f, (System.currentTimeMillis() - screen.turnOnTime) / 100.0f);
                if (scale >= 1.0f)
                    screen.doTurnOnAnim = false;
            }

            state.screens.add(new ScreenEntry(screen.side, screen.size.x, screen.size.y,
                    screen.rotation, browser.getTextureIdentifier(), scale));
        }
    }

    @Override
    public void submit(ScreenRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState cameraState) {
        for (ScreenEntry screen : state.screens) {
            poseStack.pushPose();

            float midX = 0.5f + (screen.side.right.x * screen.width + screen.side.up.x * screen.height
                    + screen.side.left.x + screen.side.down.x) * 0.5f;
            float midY = 0.5f + (screen.side.right.y * screen.width + screen.side.up.y * screen.height
                    + screen.side.left.y + screen.side.down.y) * 0.5f;
            float midZ = 0.5f + (screen.side.right.z * screen.width + screen.side.up.z * screen.height
                    + screen.side.left.z + screen.side.down.z) * 0.5f;
            poseStack.translate(midX, midY, midZ);

            switch (screen.side) {
                case BOTTOM -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
                case TOP -> poseStack.mulPose(Axis.XN.rotationDegrees(90.0f));
                case NORTH -> poseStack.mulPose(Axis.YN.rotationDegrees(180.0f));
                case SOUTH -> { }
                case WEST -> poseStack.mulPose(Axis.YN.rotationDegrees(90.0f));
                case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            }

            poseStack.scale(screen.animationScale, screen.animationScale, 1.0f);
            if (!screen.rotation.isNull)
                poseStack.mulPose(Axis.ZP.rotationDegrees(screen.rotation.angle));

            float halfWidth = screen.width * 0.5f - 2.0f / 16.0f;
            float halfHeight = screen.height * 0.5f - 2.0f / 16.0f;
            if (screen.rotation.isVertical) {
                float swap = halfWidth;
                halfWidth = halfHeight;
                halfHeight = swap;
            }

            float finalHalfWidth = halfWidth;
            float finalHalfHeight = halfHeight;
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutoutNoCull(screen.texture),
                    (pose, consumer) -> emitQuad(pose, consumer, finalHalfWidth, finalHalfHeight));
            poseStack.popPose();
        }
    }

    private static void emitQuad(PoseStack.Pose pose, VertexConsumer consumer, float halfWidth, float halfHeight) {
        vertex(consumer, pose, -halfWidth, -halfHeight, 0.505f, 0.0f, 1.0f);
        vertex(consumer, pose, halfWidth, -halfHeight, 0.505f, 1.0f, 1.0f);
        vertex(consumer, pose, halfWidth, halfHeight, 0.505f, 1.0f, 0.0f);
        vertex(consumer, pose, -halfWidth, halfHeight, 0.505f, 0.0f, 0.0f);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z,
                               float u, float v) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0x00F000F0)
                .setNormal(pose, 0.0f, 0.0f, 1.0f);
    }
}
