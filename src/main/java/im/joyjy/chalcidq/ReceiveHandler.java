package im.joyjy.chalcidq;

/**
 * 消息接收回调接口定义
 * @author jo
 */
public interface ReceiveHandler {

	/**
	 * 收到消息
	 * @param message
	 */
	void handle(Message message);
}
