package pack.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.GroupChatResponseMessage;

@ChannelHandler.Sharable
public class ChatGroupResponseHandler extends SimpleChannelInboundHandler<GroupChatResponseMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GroupChatResponseMessage msg) throws Exception {
		System.out.println(msg.getFrom()+" msg: "+msg.getContent());
	}
}
