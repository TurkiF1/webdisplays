package net.montoyo.wd.client.gui.controls;

import net.minecraft.client.gui.GuiGraphics;
import net.montoyo.wd.client.gui.loading.JsonOWrapper;

/** Simple group frame replacing the removed legacy immediate-mode renderer. */
public class ControlGroup extends Container {
    private int width = 100, height = 100, labelColor = 0xFFFFFFFF;
    private String label = "";
    private boolean labelShadowed = true;
    public ControlGroup() { paddingX = paddingY = 8; }
    public ControlGroup(int x, int y, int width, int height) { this(); this.x=x; this.y=y; this.width=width; this.height=height; }
    public ControlGroup(int x, int y, int width, int height, String label) { this(x,y,width,height); this.label=label; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    public void setSize(int width, int height) { this.width=width; this.height=height; }
    public void setLabel(String label) { this.label=label; }
    public String getLabel() { return label; }
    public int getLabelColor() { return labelColor; }
    public void setLabelColor(int color) { labelColor=color; }
    public boolean isLabelShadowed() { return labelShadowed; }
    public void setLabelShadowed(boolean value) { labelShadowed=value; }
    @Override public void draw(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if(!visible) return;
        graphics.renderOutline(x,y,width,height,0xFF808080);
        if(!label.isEmpty()) graphics.drawString(font,label,x+8,y-4,labelColor,labelShadowed);
        super.draw(graphics,mouseX,mouseY,partialTick);
    }
    @Override public void load(JsonOWrapper json) { super.load(json); width=json.getInt("width",100); height=json.getInt("height",100); label=tr(json.getString("label","")); labelColor=json.getColor("labelColor",labelColor); labelShadowed=json.getBool("labelShadowed",true); }
}
