package net.montoyo.wd.client.gui.controls;

import net.minecraft.client.gui.GuiGraphics;
import net.montoyo.wd.client.gui.loading.JsonOWrapper;

import java.util.ArrayList;

/** Lightweight 1.21.11 implementation of the legacy list control. */
public class List extends BasicControl {
    private record Entry(String text, Object userdata) {}

    public static class EntryClick extends Event<List> {
        private final int id;
        private final Entry entry;
        public EntryClick(List list) { source = list; id = list.selected; entry = list.content.get(id); }
        public int getId() { return id; }
        public String getLabel() { return entry.text; }
        public Object getUserdata() { return entry.userdata; }
    }

    private int width = 100, height = 100, selected = -1, selColor = 0xFF0080FF;
    private final ArrayList<Entry> content = new ArrayList<>();

    public List() {}
    public List(int x, int y, int width, int height) { this.x=x; this.y=y; this.width=width; this.height=height; }
    public void setSize(int width, int height) { this.width=width; this.height=height; }
    public void setWidth(int width) { this.width=width; }
    public void setHeight(int height) { this.height=height; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    public void updateContent() {}
    public int addElement(String text) { return addElement(text, null); }
    public int addElement(String text, Object userdata) { content.add(new Entry(text, userdata)); return content.size()-1; }
    public int addElementRaw(String text) { return addElement(text, null); }
    public int addElementRaw(String text, Object userdata) { return addElement(text, userdata); }
    public String getEntryLabel(int id) { return content.get(id).text; }
    public Object getEntryUserdata(int id) { return content.get(id).userdata; }
    public int findEntryByLabel(String label) { for(int i=0;i<content.size();i++) if(content.get(i).text.equals(label)) return i; return -1; }
    public int findEntryByUserdata(Object value) { for(int i=0;i<content.size();i++) if(java.util.Objects.equals(content.get(i).userdata,value)) return i; return -1; }
    public void setSelectionColor(int color) { selColor=color; }
    public int getSelectionColor() { return selColor; }
    public int getElementCount() { return content.size(); }
    public void removeElement(int id) { content.remove(id); if(selected>=content.size()) selected=-1; }
    public void removeElementRaw(int id) { removeElement(id); }
    public void clear() { content.clear(); selected=-1; }
    public void clearRaw() { clear(); }

    @Override public boolean mouseMove(double mouseX, double mouseY) {
        if(disabled) return false;
        int next = mouseX>=x && mouseX<x+width && mouseY>=y && mouseY<y+height ? (int)((mouseY-y)/12) : -1;
        selected = next>=0 && next<content.size() ? next : -1;
        return selected>=0;
    }
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!disabled && button==0 && mouseMove(mouseX,mouseY)) { parent.actionPerformed(new EntryClick(this)); return true; }
        return false;
    }
    @Override public void draw(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if(!visible) return;
        graphics.fill(x,y,x+width,y+height,0xFF202020);
        for(int i=0;i<content.size() && y+(i+1)*12<y+height;i++) {
            int color=i==selected?selColor:0xFFFFFFFF;
            graphics.drawString(font,content.get(i).text,x+3,y+3+i*12,color);
        }
    }
    @Override public void load(JsonOWrapper json) { super.load(json); width=json.getInt("width",100); height=json.getInt("height",100); selColor=json.getColor("selectionColor",selColor); }
}
