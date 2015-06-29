package im.joyjy.chalcidq.impls.protocol;

import im.joyjy.chalcidq.Packet;
import im.joyjy.chalcidq.Protocol;
import im.joyjy.chalcidq.impls.StringMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.CreateConsumerMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.CreateSessionMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.ExceptionMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.Ping;
import im.joyjy.chalcidq.impls.protocol.hornetq.ReceivedMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.ResponseMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.SessionCloseMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.SessionFlowToken;
import im.joyjy.chalcidq.impls.protocol.hornetq.SessionStartMessage;
import im.joyjy.chalcidq.impls.protocol.hornetq.SubscribeTopologyV2Message;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class HornetQProtocol extends Protocol {

	@Override
	public int getLengthFieldSize() {
		return 4;
	}

	@Override
	public int getHearbeatTTL() {
		return Ping.TTL >> 10;
	}

	@Override
	public int getListenTTL() {
		return 0;
	}

	@Override
	public void onConnect() {
		connector.send(new StringMessage("HORNETQ"));
		connector.send(new Ping());
		connector.send(new SubscribeTopologyV2Message());
		
		CreateSessionMessage msg = new CreateSessionMessage(UUID.randomUUID().toString());
		connector.setChannelId(msg.getSessionChannelId());
		connector.send(msg, HornetQMessage.CREATESESSION_RESP);
	}

	@Override
	public void onSubscribe(String topic) {
		CreateConsumerMessage msg = new CreateConsumerMessage(connector.getChannelId(), topic);
		connector.send(msg, HornetQMessage.SESS_QUEUEQUEUY_RESP);
		
		connector.send(new SessionFlowToken(connector.getChannelId()));
		
		connector.send(new SessionStartMessage(connector.getChannelId()));
	}

	@Override
	public void onDisconnect() {
		connector.send(new SessionCloseMessage());
	}

	@Override
	public Packet dispatch(ByteBuf in) {
		byte type = in.readByte();
		
		switch(type){
			case HornetQMessage.SESS_RECEIVE_MSG: {
				ReceivedMessage message = new ReceivedMessage();
				message.decode(in);
				message.onAck(new Runnable(){

					public void run() {
						//connector.send(null); // TODO
					}
				});
				return message;
			}
			case HornetQMessage.PING: {
				Ping ping = new Ping();
				ping.decode(in);
				return ping;
			}
			case HornetQMessage.CREATESESSION_RESP: // 创建 session 返回值
			case HornetQMessage.SESS_QUEUEQUEUY_RESP: // 创建 consumer/suscribe queue 返回值
			case HornetQMessage.CLUSTER_TOPOLOGY_V2: { // subscribe topology v2 返回值
				ResponseMessage message = new ResponseMessage(type);
				message.decode(in);
				return message;
			}
			case HornetQMessage.EXCEPTION:{
				HornetQMessage message = new ExceptionMessage();
				message.decode(in);
			}
			default: return null;
		}
	}

	@Override
	public Packet heartbeat() {
		return new Ping();
	}
}
