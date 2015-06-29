package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class Ping extends HornetQMessage {
	
	public static final int TTL = 60000;
	private long ttl;

	public Ping() {
		super(HornetQMessage.PING);
		ttl = TTL;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(ttl);
	}

	@Override
	public void decode(ByteBuf in) {
		super.decode(in);
		ttl = in.readLong();
	}
}
