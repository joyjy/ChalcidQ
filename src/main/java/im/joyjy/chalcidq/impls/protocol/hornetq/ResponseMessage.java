package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class ResponseMessage extends HornetQMessage {

	public ResponseMessage(byte type) {
		super(type);
	}

	@Override
	public void encodeBody(ByteBuf buf) {
	}

	@Override
	public void decode(ByteBuf in) {
		super.decode(in);
		in.readBytes(in.capacity()-HEADER_SIZE);
	}

	@Override
	public boolean isResponse() {
		return true;
	}

	
}
