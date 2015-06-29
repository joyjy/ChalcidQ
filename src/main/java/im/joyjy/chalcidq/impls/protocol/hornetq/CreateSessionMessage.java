package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class CreateSessionMessage extends HornetQMessage {
	
	private static long sessionChannelId = 0xa;
	
	private String name;

	public CreateSessionMessage(String name) {
		super(HornetQMessage.CREATESESSION);
		this.name = name;
		this.channelId = 1;
		sessionChannelId++;
	}

	public long getSessionChannelId() {
		return sessionChannelId;
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		writeString(buf, name);
		buf.writeLong(sessionChannelId); // sessionChannelId
		buf.writeInt(124); // version
		buf.writeByte(0); // username
		buf.writeByte(0); // password
		buf.writeInt(102400); // minLargeMessageSize
		buf.writeBoolean(false); // xa
		buf.writeBoolean(false); // autoCommitSends
		buf.writeBoolean(false); // autoCommitAcks
		buf.writeInt(0xffffffff);
		buf.writeBoolean(false); // preAcknowlege
		buf.writeByte(0); // defaultAddress
	}
}
