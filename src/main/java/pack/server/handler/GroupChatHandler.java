package pack.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.GroupChatRequestMessage;
import pack.meaage.GroupChatResponseMessage;
import pack.server.session.GroupSession;
import pack.server.session.GroupSessionFactory;

@ChannelHandler.Sharable
public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
	public static final GroupSession SESSION = GroupSessionFactory.getGroupSession();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
		var membersChannel = SESSION.getMembersChannel(msg.getGroupName());
		System.out.println(membersChannel.size()+"<============================");
		membersChannel.forEach(cur->cur.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent())));
	}
}
