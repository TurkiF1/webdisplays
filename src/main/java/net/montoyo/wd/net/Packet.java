package net.montoyo.wd.net;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.montoyo.wd.utilities.DistSafety;

import java.util.ArrayList;

public class Packet {
	public Packet() {
	}
	
	public Packet(FriendlyByteBuf buf) {
	
	}
	
	public void write(FriendlyByteBuf buf) {
	}
	
	public void handle(net.montoyo.wd.net.PacketContext ctx) {
	}
	
	public boolean checkClient(net.montoyo.wd.net.PacketContext ctx) {
		return ctx.isClientSide();
	}
	
	public boolean checkServer(net.montoyo.wd.net.PacketContext ctx) {
		return ctx.isServerSide();
	}
	
	public void respond(net.montoyo.wd.net.PacketContext ctx, Packet packet) {
		ctx.enqueueWork(() -> WDNetworkRegistry.INSTANCE.reply(packet, ctx));
	}
	
	private static final ArrayList<Runnable> runLater = new ArrayList<>();
	
	public void respondLater(net.montoyo.wd.net.PacketContext ctx, Packet packet) {
		ctx.enqueueWork(() -> runLater.add(() -> {
			if (checkClient(ctx))
				WDNetworkRegistry.INSTANCE.sendToServer(packet);
			else if (ctx.getSender() != null)
					WDNetworkRegistry.INSTANCE.send(packet, WDNetworkRegistry.player(ctx.getSender()));
			else WDNetworkRegistry.INSTANCE.reply(packet, ctx);
		}));
	}
	
	public static void onTick(ClientTickEvent.Post event) {
		if (!runLater.isEmpty()) {
			if (DistSafety.isConnected()) {
				for (Runnable runnable : runLater) runnable.run();
				runLater.clear();
			}
		}
	}
	
	static {
		NeoForge.EVENT_BUS.addListener(Packet::onTick);
	}
	
}
