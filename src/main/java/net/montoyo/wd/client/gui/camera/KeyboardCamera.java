package net.montoyo.wd.client.gui.camera;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;

/** Kept as a no-op while keyboard camera math is moved to the modern renderer. */
public final class KeyboardCamera {
    private KeyboardCamera() {}
    public static void updateCamera(ViewportEvent.ComputeCameraAngles event) {}
    public static void gameTick(ClientTickEvent.Post event) {}
    public static void focus(ScreenBlockEntity screen, BlockSide side) {}
}
