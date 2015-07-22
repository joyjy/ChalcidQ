package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class CreateQueueMessage extends HornetQMessage {

	private String address;
	private String queueName;

	public CreateQueueMessage(long channelId, String address, String queueName) {
		super(HornetQMessage.CREATE_QUEUE);
		this.address = address;
		this.queueName = queueName;
		this.channelId = channelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		writeSimpleString(buf, address);
	    writeSimpleString(buf, queueName);
	    buf.writeByte(0); // filterString
	    buf.writeBoolean(true); // durable
	    buf.writeBoolean(false); // temporary
	    buf.writeBoolean(true); //requiresResponse
	}

}
