package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class CreateConsumerMessage extends HornetQMessage {

	private String queue;

	public CreateConsumerMessage(long channelId, String queue) {
		super(HornetQMessage.SESS_CREATESONCUMER);
		this.queue = queue;
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeLong(0);
		writeSimpleString(buf, queue);
		buf.writeByte(0); //filterString
		buf.writeBoolean(false); //browseOnly
		buf.writeBoolean(true); // requireResponse
	}

}
