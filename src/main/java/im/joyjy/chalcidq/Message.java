package im.joyjy.chalcidq;

import java.util.Date;

public interface Message {
	
	/**
	 * 消息发送时间
	 * @return
	 */
	public Date getTime();

	/**
	 * 消息内容
	 * @return
	 */
	public String getContent();

	/**
	 * 响应消息表示已收到
	 */
	public void ack();
}
