package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.core.ScreenRights;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;

import java.util.function.Function;

public class ModifyFriendListControl extends ScreenControl {
	public static final Identifier id = new Identifier("webdisplays:mod_friend_list");
	
	boolean adding;
	NameUUIDPair friend;
	
	public ModifyFriendListControl(NameUUIDPair pair, boolean adding) {
		super(id);
		this.adding = adding;
		this.friend = pair;
	}
	
	public ModifyFriendListControl(FriendlyByteBuf buf) {
		super(id);
		adding = buf.readBoolean();
		friend = new NameUUIDPair(buf);
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(adding);
		friend.writeTo(buf);
	}
	
	@Override
	public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes, net.montoyo.wd.net.PacketContext ctx, Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
		ServerPlayer player = ctx.getSender();
		checkPerms(ScreenRights.MANAGE_FRIEND_LIST, permissionChecker, ctx.getSender());
		if (adding) tes.addFriend(player, side, friend);
		else tes.removeFriend(player, side, friend);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, net.montoyo.wd.net.PacketContext ctx) {
		throw new RuntimeException("TODO");
	}
}
