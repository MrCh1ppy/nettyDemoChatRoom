package pack.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.ChatResponseMessage;

@ChannelHandler.Sharable
public class ChatResponseHandler extends SimpleChannelInboundHandler<ChatResponseMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ChatResponseMessage msg) throws Exception {
		var text = msg.getContent();
		var from = msg.getFrom();
		System.out.println(from + ": " + text);
	}
}
