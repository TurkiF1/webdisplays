package net.montoyo.wd.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.montoyo.wd.client.ClientProxy;
import net.montoyo.wd.item.ItemLaserPointer;
import net.montoyo.wd.registry.ItemRegistry;

/** Keeps laser interaction active while vanilla renders the held item. */
public final class LaserPointerRenderer implements IItemRenderer {
    public static boolean isOn() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen == null && mc.player != null && mc.level != null
                && (ClientProxy.mouseOn || ItemLaserPointer.isOn())
                && mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ItemRegistry.LASER_POINTER.get()
                && (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY);
    }

    @Override
    public boolean render(PoseStack stack, ItemStack item, float handSideSign, float swingProgress,
                          float equipProgress, MultiBufferSource buffers, int packedLight) {
        return false;
    }
}
