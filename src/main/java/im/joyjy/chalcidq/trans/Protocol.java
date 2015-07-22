package im.joyjy.chalcidq.trans;

import io.netty.buffer.ByteBuf;

/**
 * 处理生命周期回调方法，编解码数据包
 * @author jo
 */
public abstract class Protocol {

	protected Connector connector;
	
	public void setConnector(Connector connector){
		this.connector = connector;
	}
	
	// Definition
	
	/**
	 * 数据包长度定义字段长度
	 * @return
	 */
	public abstract int getLengthFieldSize();

	/**
	 * 心跳 TTL
	 * @return
	 */
	public abstract int getHearbeatTTL();

	/**
	 * 服务器超时时间
	 * @return
	 */
	public abstract int getListenTTL();
	
	// life cycle callback
	
	/**
	 * 建立连接
	 * @param channel
	 */
	public abstract void onConnect();

	/**
	 * 订阅消息
	 * @param address
	 * @param queueName 
	 */
	public abstract void onSubscribe(String address, String queueName);
	
	/**
	 * 断开连接
	 */
	public abstract void onDisconnect();
	
	// message

	/**
	 * 心跳包
	 * @return
	 */
	public abstract Packet heartbeat();
	
	/**
	 * 分发收到的消息
	 * @param in
	 * @return
	 */
	public abstract Packet dispatch(ByteBuf in);
}
