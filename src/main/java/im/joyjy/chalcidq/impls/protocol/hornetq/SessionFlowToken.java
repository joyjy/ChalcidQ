package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SessionFlowToken extends HornetQMessage {

	public SessionFlowToken(long channelId) {
		super(HornetQMessage.SESS_FLOWTOKEN);
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(0); // consumerId
	    buf.writeInt(65536); // credits
	}

}
