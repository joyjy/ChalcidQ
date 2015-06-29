package im.joyjy.chalcidq.impls.protocol.hornetq;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import im.joyjy.chalcidq.Message;
import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class ReceivedMessage extends HornetQMessage implements Message {

	private Map<String, Object> properties = new HashMap<String, Object>();
	private Runnable ack;
	private long timstamp;

	public ReceivedMessage() {
		super(HornetQMessage.SESS_RECEIVE_MSG);
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decode(ByteBuf in) {
		
		super.decode(in);
		
		int offset = in.readInt();
		in.readerIndex(offset+4);
		
		messageId = in.readLong();
		readNullableSimpleString(in); // address
		in.readByte(); // userId
		in.readByte(); // type
		in.readBoolean(); // durable
		in.readLong(); // expiration
		timstamp = in.readLong(); // timestamp
		in.readByte(); // priority
		boolean hasProperties = in.readBoolean();
		if(hasProperties){
			int size = in.readInt();
			for(int i=0; i<size; i++){
				String key = readSimpleString(in);
				Object value = null;
				byte vType = in.readByte();
				switch(vType){
					case 0x0a: value = readSimpleString(in);
				}
				properties.put(key, value);
			}
		}
	}

	public Date getTime() {
		return new Date(timstamp);
	}

	public String getContent() {
		return (String)properties.get("content");
	}

	public void ack() {
		this.ack.run();
	}

	public void onAck(Runnable ack){
		this.ack = ack;
	}
}
