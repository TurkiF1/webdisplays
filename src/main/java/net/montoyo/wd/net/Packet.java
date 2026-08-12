package net.montoyo.wd.net;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.network.CustomPayloadEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.montoyo.wd.utilities.DistSafety;

import java.util.ArrayList;

public class Packet {
	public Packet() {
	}
	
	public Packet(FriendlyByteBuf buf) {
	
	}
	
	public void write(FriendlyByteBuf buf) {
	}
	
	public void handle(CustomPayloadEvent.Context ctx) {
	}
	
	public boolean checkClient(CustomPayloadEvent.Context ctx) {
		return ctx.isClientSide();
	}
	
	public boolean checkServer(CustomPayloadEvent.Context ctx) {
		return ctx.isServerSide();
	}
	
	public void respond(CustomPayloadEvent.Context ctx, Packet packet) {
		ctx.enqueueWork(() -> WDNetworkRegistry.INSTANCE.reply(packet, ctx));
	}
	
	private static final ArrayList<Runnable> runLater = new ArrayList<>();
	
	public void respondLater(CustomPayloadEvent.Context ctx, Packet packet) {
		ctx.enqueueWork(() -> runLater.add(() -> {
			if (checkClient(ctx))
				WDNetworkRegistry.INSTANCE.sendToServer(packet);
			else if (ctx.getSender() != null)
					WDNetworkRegistry.INSTANCE.send(packet, PacketDistributor.PLAYER.with(ctx.getSender()));
			else WDNetworkRegistry.INSTANCE.reply(packet, ctx);
		}));
	}
	
	public static void onTick(TickEvent.RenderTickEvent.Post event) {
		if (!runLater.isEmpty()) {
			if (DistSafety.isConnected()) {
				for (Runnable runnable : runLater) runnable.run();
				runLater.clear();
			}
		}
	}
	
	static {
		TickEvent.RenderTickEvent.Post.BUS.addListener(Packet::onTick);
	}
	
}
