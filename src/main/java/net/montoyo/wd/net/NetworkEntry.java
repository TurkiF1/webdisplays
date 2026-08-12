package net.montoyo.wd.net;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.SimpleChannel;

import java.util.function.Function;

public class NetworkEntry<T extends Packet> {
	Class<T> clazz;
	Function<FriendlyByteBuf, T> fabricator;
	
	public NetworkEntry(Class<T> clazz, Function<FriendlyByteBuf, T> fabricator) {
		this.clazz = clazz;
		this.fabricator = fabricator;
	}
	
	public void register(int indx, SimpleChannel channel) {
		channel.messageBuilder(clazz, indx)
				.encoder(Packet::write)
				.decoder(fabricator)
				.consumerMainThread(Packet::handle)
				.add();
	}
}
