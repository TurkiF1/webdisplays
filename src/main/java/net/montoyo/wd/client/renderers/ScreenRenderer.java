/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.montoyo.wd.entity.ScreenBlockEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter for the extracted-state rendering lifecycle introduced in 1.21.11.
 * Browser quad submission is added once MCEF exposes a modern GPU texture.
 */
public class ScreenRenderer implements BlockEntityRenderer<ScreenBlockEntity, ScreenRenderer.ScreenRenderState> {
    public static final class ScreenRenderState extends BlockEntityRenderState {
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
    public void submit(ScreenRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // MCEF browser texture submission is the next rendering-port step.
    }
}
