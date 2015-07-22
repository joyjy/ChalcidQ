package im.joyjy.chalcidq.trans.impls.hornetq;

import im.joyjy.chalcidq.trans.Packet;
import im.joyjy.chalcidq.trans.Protocol;
import im.joyjy.chalcidq.trans.impls.StringMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.AcknowledgeMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.CreateConsumerMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.CreateQueueMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.CreateSessionMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.ExceptionMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.Ping;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.ReceivedMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.ResponseMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.SessionCloseMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.SessionFlowToken;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.SessionStartMessage;
import im.joyjy.chalcidq.trans.impls.hornetq.packets.SubscribeTopologyV2Message;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class HornetQProtocol extends Protocol {

	@Override
	public int getLengthFieldSize() {
		return 4;
	}

	@Override
	public int getHearbeatTTL() {
		return Ping.TTL >> 4;
	}

	@Override
	public int getListenTTL() {
		return 0;
	}

	@Override
	public void onConnect() {
		connector.send(new StringMessage("HORNETQ"));
		connector.send(new Ping());
		connector.send(new SubscribeTopologyV2Message(), HornetQMessage.CLUSTER_TOPOLOGY_V2);
		
		CreateSessionMessage msg = new CreateSessionMessage(UUID.randomUUID().toString());
		connector.setChannelId(msg.getSessionChannelId());
		connector.send(msg, HornetQMessage.CREATESESSION_RESP);
	}

	@Override
	public void onSubscribe(String address, String queueName) {
		
		long channelId = connector.getChannelId();
		
		 // TODO channelId 变成 0x0a 了
		connector.send(new CreateQueueMessage(channelId, address, queueName), HornetQMessage.NULL_RESPONSE);
		// 创建完之后 CoreSession(0x0a) Close
		
		// 0x0b
		connector.send(new CreateConsumerMessage(channelId, queueName), HornetQMessage.SESS_QUEUEQUEUY_RESP);
		// 0x0b
		connector.send(new SessionFlowToken());
		// 0x0b
		connector.send(new SessionStartMessage(channelId));
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
				final ReceivedMessage message = new ReceivedMessage();
				message.decode(in);
				message.onAck(new Runnable(){

					public void run() {
						connector.send(new AcknowledgeMessage(message.getId()));
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
			case HornetQMessage.CLUSTER_TOPOLOGY_V2: // subscribe topology v2 返回值
			case HornetQMessage.NULL_RESPONSE: { // 
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
