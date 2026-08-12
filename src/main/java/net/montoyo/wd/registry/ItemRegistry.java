package net.montoyo.wd.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.montoyo.wd.block.item.KeyboardItem;
import net.montoyo.wd.core.CraftComponent;
import net.montoyo.wd.core.DefaultUpgrade;
import net.montoyo.wd.item.*;

import java.util.Locale;

@SuppressWarnings({"unchecked", "unused"})
public class ItemRegistry {
    private static final String MOD_ID = "webdisplays";

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    private static Item.Properties properties(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name)));
    }

    protected static final DeferredHolder<Item, Item>[] COMP_CRAFT_ITEMS = new DeferredHolder[CraftComponent.values().length];
    protected static final DeferredHolder<Item, Item>[] UPGRADE_ITEMS = new DeferredHolder[DefaultUpgrade.values().length];

    public static final DeferredHolder<Item, Item> CONFIGURATOR = ITEMS.register("screencfg", () -> new ItemScreenConfigurator(properties("screencfg")));
    public static final DeferredHolder<Item, Item> OWNERSHIP_THEIF = ITEMS.register("ownerthief", () -> new ItemOwnershipThief(properties("ownerthief")));
    public static final DeferredHolder<Item, Item> LINKER = ITEMS.register("linker", () -> new ItemLinker(properties("linker")));
    public static final DeferredHolder<Item, Item> MINEPAD = ITEMS.register("minepad", () -> new ItemMinePad2(properties("minepad")));
    public static final DeferredHolder<Item, Item> LASER_POINTER = ITEMS.register("laserpointer", () -> new ItemLaserPointer(properties("laserpointer")));

    static {
        DefaultUpgrade[] defaultUpgrades = DefaultUpgrade.values();
        for (int i = 0; i < defaultUpgrades.length; i++) {
            DefaultUpgrade upgrade = defaultUpgrades[i];
            String name = "upgrade_" + upgrade.name().toLowerCase(Locale.ROOT);
            UPGRADE_ITEMS[i] = ITEMS.register(name, () -> new ItemUpgrade(upgrade, properties(name)));
        }

        CraftComponent[] components = CraftComponent.values();
        for (int i = 0; i < components.length; i++) {
            CraftComponent cc = components[i];
            String name = "craftcomp_" + cc.name().toLowerCase(Locale.ROOT);
            COMP_CRAFT_ITEMS[i] = ITEMS.register(name, () -> new ItemCraftComponent(properties(name)));
        }
    }

    public static final DeferredHolder<Item, Item> SCREEN = ITEMS.register("screen", () -> new BlockItem(BlockRegistry.SCREEN_BLOCk.get(), properties("screen")/*.tab(WebDisplays.CREATIVE_TAB)*/));

    public static final DeferredHolder<Item, Item> KEYBOARD = ITEMS.register("keyboard", () -> new KeyboardItem(BlockRegistry.KEYBOARD_BLOCK.get(), properties("keyboard")/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredHolder<Item, Item> REDSTONE_CONTROLLER = ITEMS.register("redctrl", () -> new BlockItem(BlockRegistry.REDSTONE_CONTROL_BLOCK.get(), properties("redctrl")/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredHolder<Item, Item> REMOTE_CONTROLLER = ITEMS.register("rctrl", () -> new BlockItem(BlockRegistry.REMOTE_CONTROLLER_BLOCK.get(), properties("rctrl")/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredHolder<Item, Item> SERVER = ITEMS.register("server", () -> new BlockItem(BlockRegistry.SERVER_BLOCK.get(), properties("server")/*.tab(WebDisplays.CREATIVE_TAB)*/));

    public static DeferredHolder<Item, Item> getComputerCraftItem(int index) {
        return COMP_CRAFT_ITEMS[index];
    }

    public static DeferredHolder<Item, Item> getUpgradeItem(int index) {
        return UPGRADE_ITEMS[index];
    }

    public static int countCompCraftItems() {
        return COMP_CRAFT_ITEMS.length;
    }

    public static int countUpgrades() {
        return UPGRADE_ITEMS.length;
    }

    public static boolean isCompCraftItem(Item item) {
        for (DeferredHolder<Item, Item> itemRegistryObject : COMP_CRAFT_ITEMS)
            if (item == itemRegistryObject.get())
                return true;
        return false;
    }
}
