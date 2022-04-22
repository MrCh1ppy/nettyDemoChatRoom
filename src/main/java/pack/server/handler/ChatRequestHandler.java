package pack.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.ChatRequestMessage;
import pack.meaage.ChatResponseMessage;
import pack.server.session.SessionFactory;

@ChannelHandler.Sharable
public class ChatRequestHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
		var to = msg.getTo();
		var channel = SessionFactory.getSession().getChannel(to);
		if(channel!=null){
			channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
		}
		else {
			ctx.writeAndFlush(new ChatResponseMessage(false,"对方不在线"));
		}
	}
}
