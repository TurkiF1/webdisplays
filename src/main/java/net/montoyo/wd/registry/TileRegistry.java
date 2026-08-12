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
            .register("screen", () -> BlockEntityType.create(ScreenBlockEntity::new, BlockRegistry.SCREEN_BLOCk.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> KEYBOARD = TILE_TYPES.register("kb_left", () -> BlockEntityType.create(KeyboardBlockEntity::new, BlockRegistry.KEYBOARD_BLOCK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> REMOTE_CONTROLLER = TILE_TYPES.register("rctrl",
            () -> BlockEntityType.create(RemoteControlBlockEntity::new, BlockRegistry.REMOTE_CONTROLLER_BLOCK.get()));         //WITHOUT FACING (>= 3)

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> REDSTONE_CONTROLLER = TILE_TYPES.register("redctrl",
            () -> BlockEntityType.create(RedstoneControlBlockEntity::new, BlockRegistry.REDSTONE_CONTROL_BLOCK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SERVER = TILE_TYPES.register("server",
            () -> BlockEntityType.create(ServerBlockEntity::new, BlockRegistry.SERVER_BLOCK.get()));

    public static void init(IEventBus bus) {
        TILE_TYPES.register(bus);
    }
}
