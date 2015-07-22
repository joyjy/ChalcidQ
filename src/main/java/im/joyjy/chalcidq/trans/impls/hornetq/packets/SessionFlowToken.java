package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SessionFlowToken extends HornetQMessage {

	public SessionFlowToken() {
		super(HornetQMessage.SESS_FLOWTOKEN);
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(0); // consumerId
	    buf.writeInt(65536); // credits
	}

}
