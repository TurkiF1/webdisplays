package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;

import java.util.function.Function;

public class TurnOffControl extends ScreenControl {
	public static final Identifier id = Identifier.parse("webdisplays:deactivate");

	public static final TurnOffControl INSTANCE = new TurnOffControl();

	public TurnOffControl() {
		super(id);
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
	}
	
	@Override
	public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes, net.montoyo.wd.net.PacketContext ctx, Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
		throw new RuntimeException("Cannot handle deactivation packet from server");
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, net.montoyo.wd.net.PacketContext ctx) {
		if (side != null) {
			WebDisplays.PROXY.closeGui(pos, side);
			tes.disableScreen(side);
		} else {
			for (BlockSide value : BlockSide.values()) {
				WebDisplays.PROXY.closeGui(pos, value);
				tes.disableScreen(value);
			}
		}
	}
}
