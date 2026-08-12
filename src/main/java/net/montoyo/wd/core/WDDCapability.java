package net.montoyo.wd.core;

/**
 * Legacy in-memory representation retained for compatibility helpers.
 * Player persistence now uses NeoForge's persistent entity data directly.
 */
public class WDDCapability implements IWDDCapability {
    private boolean firstRun = true;

    public WDDCapability() {
    }

    @Override
    public boolean isFirstRun() {
        return firstRun;
    }

    @Override
    public void clearFirstRun() {
        firstRun = false;
    }

    @Override
    public void cloneTo(IWDDCapability dst) {
        if (!firstRun) dst.clearFirstRun();
    }
}
