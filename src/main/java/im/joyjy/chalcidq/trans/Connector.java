package im.joyjy.chalcidq.trans;

import im.joyjy.chalcidq.Message;
import im.joyjy.chalcidq.ReceiveHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * 网络连接数据收发类
 * @author jo
 */
public abstract class Connector {

	private long channelId;
	private ReceiveHandler handler;
	
	private byte[] pollLock = new byte[0];
	private Map<Integer, Packet> responses = new HashMap<Integer, Packet>();

	/**
	 * 服务端主机
	 */
	protected String host;
	/**
	 * 服务端端口号
	 */
	protected int port;
	/**
	 * 在此连接上使用的数据协议
	 */
	protected Protocol protocol;

	/**
	 * 设置 channelId
	 * @param channelId
	 */
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	
	/**
	 * 获取 channelId
	 * @return
	 */
	public long getChannelId() {
		return channelId;
	}

	/**
	 * 创建连接类
	 * @param protocol
	 */
	public Connector(String host, int port, Protocol protocol){
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		protocol.setConnector(this);
	}

	// public

	/**
	 * 连接到服务器端
	 */
	public abstract void connect();

	/**
	 * 订阅指定 topic 的消息
	 * @param address
	 * @param queueName 
	 */
	public abstract void subscribe(String address, String queueName);
	
	/**
	 * 向服务端断开连接
	 */
	public abstract void disconnect();

	/**
	 * 发送数据包
	 * @param packet
	 * @return 
	 */
	public abstract void send(Packet packet);

	/**
	 * 发送数据包并等待结果
	 * @param packet
	 * @param waitingResp
	 * @return 
	 */
	public abstract Packet send(Packet packet, int waitingResp);
	
	/**
	 * 设置消息接收处理回调方法
	 * @param handler
	 */
	public void setReceiveHandler(ReceiveHandler handler) {
		this.handler = handler;
	}
	
	// protected

	/**
	 * 当收到消息时，通知 handler
	 * @param packet
	 */
	protected void onReceive(Packet packet){
		if(packet instanceof Message){
			this.handler.handle((Message)packet);
		} else if(packet.isResponse()){
			synchronized(pollLock){
				responses.put(packet.getType(), packet);
			}
		}
	}

	protected Packet pollResponse(int waitingResp) {
		synchronized(pollLock){
			if(responses.containsKey(waitingResp)){
				return responses.remove(waitingResp);
			}
		}
		
		return null;
	}
}
