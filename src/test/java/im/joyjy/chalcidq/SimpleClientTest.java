
package im.joyjy.chalcidq;

import im.joyjy.chalcidq.trans.impls.NettyConnector;
import im.joyjy.chalcidq.trans.impls.hornetq.HornetQProtocol;

import org.junit.Test;

public class SimpleClientTest {

	@Test
	public void ReceiveTest() throws InterruptedException{
		
		NettyConnector connector = new NettyConnector("127.0.0.1", 5445, new HornetQProtocol());
		SimpleClient client1 = new SimpleClient("device.test", connector);
		client1.setReceiveHandler(new TestHandler(client1));
		
		client1.connect();
		client1.register("address.test");
		
		Thread.sleep(5*60*1000);
	}
	
	class TestHandler implements ReceiveHandler{

		private SimpleClient client;

		public TestHandler(SimpleClient client) {
			this.client = client;
		}

		public void handle(Message message) {
			System.out.println(client.toString() + message);
		}
	}
	
}
