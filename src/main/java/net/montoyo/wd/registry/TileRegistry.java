package net.montoyo.wd.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.montoyo.wd.entity.*;

public class TileRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILE_TYPES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, "webdisplays");

    //Register tile entities
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ScreenBlockEntity>> SCREEN_BLOCK_ENTITY = TILE_TYPES
            .register("screen", () -> new BlockEntityType<>(ScreenBlockEntity::new, false, BlockRegistry.SCREEN_BLOCk.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> KEYBOARD = TILE_TYPES.register("kb_left", () -> new BlockEntityType<>(KeyboardBlockEntity::new, false, BlockRegistry.KEYBOARD_BLOCK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> REMOTE_CONTROLLER = TILE_TYPES.register("rctrl",
            () -> new BlockEntityType<>(RemoteControlBlockEntity::new, false, BlockRegistry.REMOTE_CONTROLLER_BLOCK.get()));         //WITHOUT FACING (>= 3)

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> REDSTONE_CONTROLLER = TILE_TYPES.register("redctrl",
            () -> new BlockEntityType<>(RedstoneControlBlockEntity::new, false, BlockRegistry.REDSTONE_CONTROL_BLOCK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SERVER = TILE_TYPES.register("server",
            () -> new BlockEntityType<>(ServerBlockEntity::new, false, BlockRegistry.SERVER_BLOCK.get()));

    public static void init(IEventBus bus) {
        TILE_TYPES.register(bus);
    }
}
