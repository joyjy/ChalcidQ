
package im.joyjy.chalcidq;

import im.joyjy.chalcidq.Message;
import im.joyjy.chalcidq.ReceiveHandler;
import im.joyjy.chalcidq.impls.conn.NettyConnector;
import im.joyjy.chalcidq.impls.protocol.HornetQProtocol;

import org.junit.Assert;
import org.junit.Test;

public class SimpleClientTest {

	@Test
	public void ReceiveTest() throws InterruptedException{
		
		TestHandler handler = new TestHandler();
		
		SimpleClient client = new SimpleClient(new NettyConnector("127.0.0.1", 5445, new HornetQProtocol()));
		client.setReceiveHandler(handler);
		
		client.connect();
		client.register("queue.test");
		
		Thread.sleep(5*60*1000);
		
		Assert.assertTrue(handler.received);
	}
	
	class TestHandler implements ReceiveHandler{
		
		public boolean received;

		public void handle(Message message) {
			this.received = true;
			System.out.println(message.getContent()+" - "+message.getTime());
		}
	}
	
}
