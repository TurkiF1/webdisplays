package net.montoyo.wd.utilities.math;

import net.minecraft.world.phys.AABB;

/** Mutable builder for Minecraft's now-immutable AABB. */
public final class MutableAABB {
    private double minX, minY, minZ, maxX, maxY, maxZ;

    public MutableAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        setAndCheck(x1, y1, z1, x2, y2, z2);
    }

    public void setAndCheck(double x1, double y1, double z1, double x2, double y2, double z2) {
        minX = Math.min(x1, x2); minY = Math.min(y1, y2); minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2); maxY = Math.max(y1, y2); maxZ = Math.max(z1, z2);
    }

    public void expand(double x1, double y1, double z1, double x2, double y2, double z2) {
        minX = Math.min(minX, Math.min(x1, x2)); minY = Math.min(minY, Math.min(y1, y2)); minZ = Math.min(minZ, Math.min(z1, z2));
        maxX = Math.max(maxX, Math.max(x1, x2)); maxY = Math.max(maxY, Math.max(y1, y2)); maxZ = Math.max(maxZ, Math.max(z1, z2));
    }

    public AABB toMc() { return new AABB(minX, minY, minZ, maxX, maxY, maxZ); }
}
