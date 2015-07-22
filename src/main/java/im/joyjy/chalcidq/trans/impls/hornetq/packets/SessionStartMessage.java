package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SessionStartMessage extends HornetQMessage {
	
	public SessionStartMessage(long channelId) {
		super(HornetQMessage.SESS_START);
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {}
}
