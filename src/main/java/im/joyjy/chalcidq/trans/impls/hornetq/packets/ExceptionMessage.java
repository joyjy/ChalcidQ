package im.joyjy.chalcidq.trans.impls.hornetq.packets;

import im.joyjy.chalcidq.trans.impls.hornetq.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class ExceptionMessage extends HornetQMessage {

	public ExceptionMessage() {
		super(HornetQMessage.EXCEPTION);
	}

	@Override
	public void encodeBody(ByteBuf buf) {
	}

	@Override
	public void decode(ByteBuf in) {
		super.decode(in);
		int code = in.readInt();
		String message = readNullableString(in);
		throw new RuntimeException(code + ":" + message);
	}

}
