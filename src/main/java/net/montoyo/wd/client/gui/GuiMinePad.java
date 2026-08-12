package net.montoyo.wd.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.montoyo.wd.client.ClientProxy;
import net.montoyo.wd.utilities.data.BlockSide;

/** Transitional 1.21.11 minePad screen while its legacy renderer is migrated. */
public class GuiMinePad extends WDScreen {
    public GuiMinePad() { super(Component.translatable("webdisplays.gui.minepad.close")); }
    public GuiMinePad(ClientProxy.PadData pad) { this(); }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawCenteredString(font, title, width / 2, height / 2, 0xFFFFFF);
    }

    @Override
    public boolean isForBlock(BlockPos pos, BlockSide side) { return false; }
}
