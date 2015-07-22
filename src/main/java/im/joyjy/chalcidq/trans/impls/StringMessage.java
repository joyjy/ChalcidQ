package im.joyjy.chalcidq.trans.impls;

import im.joyjy.chalcidq.trans.Packet;
import io.netty.buffer.ByteBuf;

/**
 * 纯文本消息
 * @author jo
 *
 */
public class StringMessage extends Packet {
	
	private String value;
	
    /**
     * 建立一个纯文本消息
     * @param value
     */
    public StringMessage(String value){
    	this.value = value;
    }

    @Override
	public void encode(ByteBuf buf) {
		buf.writeBytes(value.getBytes());
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public boolean isResponse() {
		return false;
	}
}
