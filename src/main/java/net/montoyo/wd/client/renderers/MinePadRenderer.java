package net.montoyo.wd.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.montoyo.wd.config.ClientConfig;
import net.montoyo.wd.item.ItemMinePad2;

/** Uses vanilla item rendering until the custom MinePad pipeline is migrated. */
public final class MinePadRenderer implements IItemRenderer {
    public static boolean renderAtSide(float handSideSign) {
        float relSide = handSideSign;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.player.getMainArm() == HumanoidArm.LEFT) relSide *= -1;
        boolean sideHold = mc.player.isShiftKeyDown() != ClientConfig.sidePad;
        if ((relSide < 0 && mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemMinePad2)
                || (relSide > 0 && mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ItemMinePad2)) sideHold = true;
        return sideHold;
    }

    @Override
    public boolean render(PoseStack stack, ItemStack item, float handSideSign, float swingProgress,
                          float equipProgress, MultiBufferSource buffers, int packedLight) {
        return false;
    }
}
