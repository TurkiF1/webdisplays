package net.montoyo.wd.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;

/** Transitional keyboard focus screen for the new client renderer. */
public class GuiKeyboard extends WDScreen {
    private final BlockPos keyboardPos;
    private final BlockPos screenPos;
    private final BlockSide screenSide;

    public GuiKeyboard() {
        super(Component.translatable("webdisplays.gui.keyboard"));
        keyboardPos = BlockPos.ZERO;
        screenPos = BlockPos.ZERO;
        screenSide = BlockSide.NORTH;
    }

    public GuiKeyboard(ScreenBlockEntity screen, BlockSide side, BlockPos keyboardPos) {
        super(Component.translatable("webdisplays.gui.keyboard"));
        this.keyboardPos = keyboardPos;
        this.screenPos = screen.getBlockPos();
        this.screenSide = side;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawCenteredString(font, title, width / 2, height / 2, 0xFFFFFF);
    }

    @Override
    public boolean isForBlock(BlockPos pos, BlockSide side) {
        return pos.equals(keyboardPos) || (pos.equals(screenPos) && side == screenSide);
    }
}
