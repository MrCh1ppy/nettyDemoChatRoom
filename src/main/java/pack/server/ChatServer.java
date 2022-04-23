package pack.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import pack.meaage.PingMessage;
import pack.protocol.edcoder.FrameDecoderFactory;
import pack.protocol.edcoder.MessageCodecSharable;
import pack.server.handler.ChatRequestHandler;
import pack.server.handler.GroupChatHandler;
import pack.server.handler.GroupCreateRequestMessageSimpleChannelInboundHandler;
import pack.server.handler.LoginRequestMessageSimpleChannelInboundHandler;
import pack.server.session.QuitHandler;

import java.net.InetSocketAddress;

public class ChatServer {
	private static final LoggingHandler loggingHandler=new LoggingHandler(LogLevel.DEBUG);
	private static final MessageCodecSharable messageCodecSharable=new MessageCodecSharable();
	private static final ChatRequestHandler chatRequestHandler=new ChatRequestHandler();
	private static final LoginRequestMessageSimpleChannelInboundHandler loginRequestHandler=new LoginRequestMessageSimpleChannelInboundHandler();

	private static final GroupCreateRequestMessageSimpleChannelInboundHandler groupCreateHandler=new GroupCreateRequestMessageSimpleChannelInboundHandler();
	public static final GroupChatHandler GROUP_CHAT_HANDLER = new GroupChatHandler();
	public static final QuitHandler QUIT_HANDLER = new QuitHandler();


	public static void main(String[] args) {
		var boss = new NioEventLoopGroup();
		var worker = new NioEventLoopGroup();
		try{
			var future = new ServerBootstrap()
					.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(new IdleStateHandler(5,0,0))
									.addLast(FrameDecoderFactory.getInstance())
									.addLast(loggingHandler)
									.addLast(messageCodecSharable)
									.addLast(chatRequestHandler)
									.addLast(loginRequestHandler)
									.addLast(groupCreateHandler)
									.addLast(GROUP_CHAT_HANDLER)
									.addLast(QUIT_HANDLER)
									.addLast(new PingHandler())
									.addLast(new ChannelDuplexHandler(){
										/**
										 * Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
										 * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
										 * <p>
										 * Sub-classes may override this method to change behavior.
										 *
										 * @param ctx
										 * @param evt
										 */
										@Override
										public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
											var event = (IdleStateEvent) evt;
											if(event.state()== IdleState.READER_IDLE){
												System.out.println("need to lost=============================================");
												ctx.channel().close();
											}
										}
									})
							;

						}
					})
					.bind(new InetSocketAddress(9000)).sync();
			System.out.println("on");
			future.channel().closeFuture().sync();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	private static class PingHandler extends SimpleChannelInboundHandler<PingMessage> {
		/**
		 * Is called for each message of type {@link I}.
		 *
		 * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
		 *            belongs to
		 * @param msg the message to handle
		 * @throws Exception is thrown if an error occurred
		 */
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
			System.out.println("receive ping..");
		}
	}
}
