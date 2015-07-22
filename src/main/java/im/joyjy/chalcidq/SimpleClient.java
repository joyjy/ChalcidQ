package im.joyjy.chalcidq;

import im.joyjy.chalcidq.trans.Connector;

/**
 * Client
 * @author jo
 *
 */
public class SimpleClient {
	
	private String clientId;
	private String topic;
	private Connector connector;

	/**
	 * 创建客户端
	 * @param clientId 
	 * @param connector
	 */
	public SimpleClient(String clientId, Connector connector) {
		this.connector = connector;
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	/**
	 * 连接到服务器端
	 */
	public void connect() {
		connector.connect();
	}

	/**
	 * 从服务器端断开连接
	 */
	public void disconnect() {
		connector.disconnect();
	}

	/**
	 * 设置消息处理器
	 * @param handler
	 */
	public void setReceiveHandler(final ReceiveHandler handler) {
		connector.setReceiveHandler(new ReceiveHandler() {

			public void handle(Message message) {
				handler.handle(message);
				message.ack();
			}
		});
	}

	/**
	 * 订阅消息
	 * @param topic
	 */
	public void register(String topic) {
		if(this.topic != null){
			throw new RuntimeException();
		}
		this.topic = topic;
		connector.subscribe(topic, clientId);
	}

	@Override
	public String toString() {
		return "SimpleClient [clientId=" + clientId + ", topic=" + topic
				+ ", connector=" + connector + "]";
	}
	
	
}
