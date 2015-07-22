package im.joyjy.chalcidq.trans;

import io.netty.buffer.ByteBuf;


/**
 * 协议数据包定义
 * @author jo
 */
public abstract class Packet {

	/**
	 * 消息标识
	 */
	protected long messageId;

	/**
	 * 消息类型
	 * @return
	 */
	public abstract int getType();

	/**
	 * 是否针对某个消息回复
	 * @return
	 */
	public abstract boolean isResponse();
	
	/**
	 * 编码
	 * @param buf
	 */
	public abstract void encode(ByteBuf buf);
}
