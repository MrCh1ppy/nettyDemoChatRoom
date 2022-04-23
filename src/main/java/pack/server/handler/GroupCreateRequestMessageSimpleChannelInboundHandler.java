package pack.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.GroupCreateRequestMessage;
import pack.meaage.GroupCreateResponseMessage;
import pack.server.session.GroupSessionFactory;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageSimpleChannelInboundHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
		var groupName = msg.getGroupName();
		var members = msg.getMembers();
		var session = GroupSessionFactory.getGroupSession();
		var group = session.createGroup(groupName, members);
		if (group == null) {
			//success
			ctx.writeAndFlush(new GroupCreateResponseMessage(true, "success"));
			//pull group
			for (Channel channel : session.getMembersChannel(groupName)) {
				channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入群：" + groupName));
			}
			System.out.println("====================================================================mems"+group.getMembers().size());
		}
		else {
			ctx.writeAndFlush(new GroupCreateResponseMessage(false, "fail,already exist"));
		}
	}
}
