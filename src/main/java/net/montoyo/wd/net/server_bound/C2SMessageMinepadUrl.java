package net.montoyo.wd.net.server_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.montoyo.wd.item.ItemMinePad2;
import net.montoyo.wd.net.Packet;
import net.montoyo.wd.utilities.serialization.Util;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class C2SMessageMinepadUrl extends Packet {
	UUID id;
	String url;
	
	public C2SMessageMinepadUrl(UUID id, String url) {
		this.id = id;
		this.url = url;
	}
	
	public C2SMessageMinepadUrl(FriendlyByteBuf buf) {
		super(buf);
		this.id = buf.readUUID();
		this.url = buf.readUtf();
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(id);
		buf.writeUtf(url);
	}
	
	protected void merge(ItemStack stack) {
		CompoundTag tag = Util.getOrCreateItemTag(stack);
		if (url.equals("")) {
			tag.remove("PadID");
		} else {
			Util.putUUID(tag, "PadID", id);
			tag.putString("PadURL", url);
		}
		Util.setItemTag(stack, tag);
	}
	
	@Override
	public void handle(net.montoyo.wd.net.PacketContext ctx) {
		// check if the player is holding a minePad with the requested id
		// if the player is, then update that pad
		for (InteractionHand value : InteractionHand.values()) {
			ItemStack stack = ctx.getSender().getItemInHand(value);
			CompoundTag tag = Util.getItemTag(stack);
			if (stack.getItem() instanceof ItemMinePad2 && tag != null && tag.contains("PadID")) {
				UUID padId = Util.getUUID(tag, "PadID");
				if (padId.equals(id)) {
					merge(stack);
					return;
				}
			}
		}
		
		// if the player is not holding the requested minePad, update the first one that does not already have an ID
		for (InteractionHand value : InteractionHand.values()) {
			ItemStack stack = ctx.getSender().getItemInHand(value);
			CompoundTag tag = Util.getItemTag(stack);
			if (stack.getItem() instanceof ItemMinePad2 && (tag == null || !tag.contains("PadID"))) {
				merge(stack);
				return;
			}
		}
	}
}
