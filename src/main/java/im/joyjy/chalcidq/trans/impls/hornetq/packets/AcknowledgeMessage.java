package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class AcknowledgeMessage extends HornetQMessage {

	public AcknowledgeMessage(long messageId) {
		super(HornetQMessage.SESS_INDIVIDUAL_ACKNOWLEDGE);
		this.messageId = messageId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(0); // consumerId
		buf.writeLong(messageId);
		buf.writeBoolean(false); // requireResponse
	}

}
