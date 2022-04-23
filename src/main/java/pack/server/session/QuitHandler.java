package pack.server.session;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class QuitHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionFactory.getSession().unbind(ctx.channel());
		System.out.println("bye~");
		log.debug("{} lost",ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		SessionFactory.getSession().unbind(ctx.channel());
		System.out.println("bye~ in space");
		log.debug("{} lost in space",ctx);
	}


}
