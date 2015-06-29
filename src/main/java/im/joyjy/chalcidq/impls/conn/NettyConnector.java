package im.joyjy.chalcidq.impls.conn;

import im.joyjy.chalcidq.Connector;
import im.joyjy.chalcidq.Packet;
import im.joyjy.chalcidq.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 基于 Netty 的 Connector 实现
 * @author jo
 */
public class NettyConnector extends Connector {

	private Channel channel;

	/**
	 * 创建一个 NettyConnector
	 * @param host
	 * @param port
	 * @param protocol
	 */
	public NettyConnector(String host, int port, Protocol protocol) {
		super(host, port, protocol);
	}

	@Override
	public synchronized void connect() {

		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel channel)
							throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast("ping", new IdleStateHandler(protocol.getListenTTL(), protocol.getHearbeatTTL(), 0));
						pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, protocol.getLengthFieldSize()){
							
							@Override
						    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
						        return super.extractFrame(ctx, buffer, index, length).skipBytes(protocol.getLengthFieldSize());
						    }
						});
						pipeline.addLast("encoder", new MessageToByteEncoder<Packet>(){

							@Override
							protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
								msg.encode(out);
							}
							
						});
						
						pipeline.addLast("handler", new SimpleChannelInboundHandler<ByteBuf>(){
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
								Packet msg = protocol.dispatch(in);
								NettyConnector.super.onReceive(msg);
							}
							
							@Override
						     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
						         if (evt instanceof IdleStateEvent) {
						             IdleStateEvent e = (IdleStateEvent) evt;
						             if (e.state() == IdleState.READER_IDLE) {
						            	 disconnect();
						             } else if (e.state() == IdleState.WRITER_IDLE) {
						                 ctx.writeAndFlush(protocol.heartbeat());
						             }
						         }
						     }
							
						});
					}
				});
		
		ChannelFuture f = b.connect(host, port).awaitUninterruptibly();
		if(f.isSuccess()){
			channel = f.channel();
			protocol.onConnect();
		}
	}

	@Override
	public synchronized void disconnect() {
		protocol.onDisconnect();
		channel.close().awaitUninterruptibly();
	}

	@Override
	public void subscribe(String topic) {
		protocol.onSubscribe(topic);
	}

	@Override
	public void send(Packet message) {
		channel.writeAndFlush(message);
	}

	@Override
	public Packet send(Packet message, int waitingResp) {
		channel.writeAndFlush(message);
		Packet packet;
		while((packet = pollResponse(waitingResp)) == null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return packet;
	}
}
