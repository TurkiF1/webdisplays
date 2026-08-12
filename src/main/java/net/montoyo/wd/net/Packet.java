package net.montoyo.wd.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor;
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
	
	public static void onTick(TickEvent.RenderTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END)) {
			if (!runLater.isEmpty()) {
				if (DistSafety.isConnected()) {
					for (Runnable runnable : runLater) runnable.run();
					runLater.clear();
				}
			}
		}
	}
	
	static {
		MinecraftForge.EVENT_BUS.addListener(Packet::onTick);
	}
	
}
