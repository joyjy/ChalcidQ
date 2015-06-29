package im.joyjy.chalcidq.impls.protocol.hornetq;

import im.joyjy.chalcidq.impls.protocol.HornetQMessage;
import io.netty.buffer.ByteBuf;

public class SubscribeTopologyV2Message extends HornetQMessage {

	public SubscribeTopologyV2Message() {
		super(HornetQMessage.SUSCRIBE_TOPOLOGY_V2);
	}

	@Override
	public void encodeBody(ByteBuf buf) {
		buf.writeBoolean(false); // clusterConnection
		buf.writeInt(124); // clientVersion
	}

}
