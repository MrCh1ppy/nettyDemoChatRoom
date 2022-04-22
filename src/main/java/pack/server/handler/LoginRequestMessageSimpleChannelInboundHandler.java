package pack.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pack.meaage.LoginRequestMessage;
import pack.meaage.LoginResponseMessage;
import pack.server.service.UserServiceFactory;
import pack.server.session.SessionFactory;

@ChannelHandler.Sharable
public class LoginRequestMessageSimpleChannelInboundHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
		var username = msg.getUsername();
		var password = msg.getPassword();
		var check = UserServiceFactory.getUserService().login(username, password);
		LoginResponseMessage res;
		if (check) {
			SessionFactory.getSession().bind(ctx.channel(),username);
			res = new LoginResponseMessage(true, "success");
		}
		else {
			res = new LoginResponseMessage(false, "failure");
		}
		ctx.writeAndFlush(res);
	}
}
