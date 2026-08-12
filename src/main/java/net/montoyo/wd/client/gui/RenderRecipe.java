package net.montoyo.wd.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Temporary recipe preview while the legacy debug renderer is migrated. */
public class RenderRecipe extends Screen {
    public RenderRecipe() { super(Component.translatable("webdisplays.gui.recipes")); }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawCenteredString(font, title, width / 2, height / 2, 0xFFFFFF);
    }
}
