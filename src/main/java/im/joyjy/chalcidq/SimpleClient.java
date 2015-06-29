package im.joyjy.chalcidq;


public class SimpleClient {

	private Connector connector;
	private String topic;

	/**
	 * 创建客户端
	 * @param connector
	 */
	public SimpleClient(Connector connector) {
		this.connector = connector;
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
		if (this.topic != null) {
			throw new RuntimeException();
		}
		this.topic = topic;
		connector.subscribe(topic);
	}
}
