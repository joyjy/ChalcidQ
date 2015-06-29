package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SessionCloseMessage extends HornetQMessage {
	public SessionCloseMessage() {
		super(HornetQMessage.PING);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
}
