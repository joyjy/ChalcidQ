package im.joyjy.chalcidq;

import java.util.Date;

/**
 * 消息
 * @author jo
 *
 */
public interface Message {
	
	/**
	 * 获取来源
	 * @return
	 */
	public String getTopic();

	/**
	 * 消息内容
	 * @return
	 */
	public String getContent();
	
	/**
	 * 消息发送时间
	 * @return
	 */
	public Date getTime();

	/**
	 * 响应消息表示已收到
	 */
	public void ack();
}
