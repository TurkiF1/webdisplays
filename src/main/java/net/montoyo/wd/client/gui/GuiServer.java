package net.montoyo.wd.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.math.Vector3i;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;

/** Transitional UI for the optional WebDisplays file server. */
public class GuiServer extends WDScreen {
    private final BlockPos serverPos;
    public GuiServer(Vector3i pos, NameUUIDPair owner) {
        super(Component.translatable("webdisplays.gui.server"));
        serverPos = pos.toBlock();
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawCenteredString(font, title, width / 2, height / 2, 0xFFFFFF);
    }
    @Override public boolean isForBlock(BlockPos pos, BlockSide side) { return pos.equals(serverPos); }
}
