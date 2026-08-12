package net.montoyo.wd.client.gui.controls;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.montoyo.wd.client.gui.WDScreen;
import net.montoyo.wd.client.gui.loading.JsonOWrapper;
import net.montoyo.wd.utilities.data.Bounds;

/** Base GUI control. Rendering helpers are staged for the 1.21.11 render pipeline. */
public abstract class Control {
    public static final int COLOR_BLACK=0xFF000000, COLOR_WHITE=0xFFFFFFFF, COLOR_RED=0xFFFF0000,
            COLOR_GREEN=0xFF00FF00, COLOR_BLUE=0xFF0000FF, COLOR_CYAN=0xFF00FFFF,
            COLOR_MANGENTA=0xFFFF00FF, COLOR_YELLOW=0xFFFFFF00;
    protected final Minecraft mc = Minecraft.getInstance();
    protected final Font font = mc.font;
    protected static WDScreen parent;
    protected String name;
    protected Object userdata;

    public Control() { parent = WDScreen.CURRENT_SCREEN; }
    public Object getUserdata(){return userdata;} public void setUserdata(Object value){userdata=value;}
    public boolean keyTyped(int keyCode,int modifier){return false;} public boolean keyUp(int key,int scan,int modifiers){return false;}
    public boolean keyDown(int key,int scan,int modifiers){return false;} public boolean mouseClicked(double x,double y,int b){return false;}
    public void unfocus(){} public boolean mouseReleased(double x,double y,int b){return false;}
    public boolean mouseClickMove(double x,double y,int b,double dx,double dy){return false;}
    public boolean mouseMove(double x,double y){return false;} public boolean mouseScroll(double x,double y,double amount){return false;}
    public void draw(GuiGraphics graphics,int mouseX,int mouseY,float partialTick){} public void postDraw(GuiGraphics graphics,int x,int y,float pt){}
    public void destroy(){} public WDScreen getParent(){return parent;}
    public abstract int getX(); public abstract int getY(); public abstract int getWidth(); public abstract int getHeight(); public abstract void setPos(int x,int y);

    public void fillRect(MultiBufferSource.BufferSource source,int x,double y,int w,int h,int color){}
    public void fillTexturedRect(PoseStack pose,int x,int y,int w,int h,double u1,double v1,double u2,double v2){}
    public static void blend(boolean enable){} public void bindTexture(Identifier id){}
    public void drawBorder(GuiGraphics graphics,int x,int y,int w,int h,int color){}
    public void drawBorder(GuiGraphics graphics,int x,int y,int w,int h,int color,double size){}
    public GuiGraphics beginFramebuffer(RenderTarget fbo,float width,float height){return null;}
    public void endFramebuffer(GuiGraphics graphics,RenderTarget fbo){}

    public static String tr(String text){
        if(text.length()>=2&&text.charAt(0)=='$') return text.charAt(1)=='$'?text.substring(1):I18n.get(text.substring(1));
        return text;
    }
    public void setName(String value){name=value;} public String getName(){return name;}
    public void load(JsonOWrapper json){name=json.getString("name","");}
    public static Bounds findBounds(java.util.List<Control> controls){
        int minX=Integer.MAX_VALUE,minY=Integer.MAX_VALUE,maxX=Integer.MIN_VALUE,maxY=Integer.MIN_VALUE;
        for(Control c:controls){minX=Math.min(minX,c.getX());minY=Math.min(minY,c.getY());maxX=Math.max(maxX,c.getX()+c.getWidth());maxY=Math.max(maxY,c.getY()+c.getHeight());}
        return controls.isEmpty()?new Bounds(0,0,0,0):new Bounds(minX,minY,maxX,maxY);
    }
}
