package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SessionStartMessage extends HornetQMessage {
	
	public SessionStartMessage(long channelId) {
		super(HornetQMessage.SESS_START);
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {}
}
