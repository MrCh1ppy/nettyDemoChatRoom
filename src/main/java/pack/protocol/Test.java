package pack.protocol;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import pack.meaage.LoginRequestMessage;

public class Test {
	public static void main(String[] args) throws Exception {
		var channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());
		var msg = new LoginRequestMessage("1", "1");
		channel.writeOutbound(msg);
		var buf = ByteBufAllocator.DEFAULT.buffer();
		new MessageCodec().encode(null,msg,buf);

		channel.writeInbound(buf);
	}
}
