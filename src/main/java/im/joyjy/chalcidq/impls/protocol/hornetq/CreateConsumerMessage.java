package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class CreateConsumerMessage extends HornetQMessage {

	private String topic;

	public CreateConsumerMessage(long channelId, String topic) {
		super(HornetQMessage.SESS_CREATESONCUMER);
		this.topic = topic;
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(clientId);
		writeSimpleString(buf, topic);
		buf.writeByte(0); //filterString
		buf.writeBoolean(false); //browseOnly
		buf.writeBoolean(true); // requireResponse
	}

}
