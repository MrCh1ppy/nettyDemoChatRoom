package pack.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import pack.protocol.FrameDecoderFactory;
import pack.protocol.MessageCodecSharable;
import pack.server.handler.ChatRequestHandler;
import pack.server.handler.GroupCreateRequestMessageSimpleChannelInboundHandler;
import pack.server.handler.LoginRequestMessageSimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class ChatServer {
	private static final LoggingHandler loggingHandler=new LoggingHandler(LogLevel.DEBUG);
	private static final MessageCodecSharable messageCodecSharable=new MessageCodecSharable();
	private static final ChatRequestHandler chatRequestHandler=new ChatRequestHandler();
	private static final LoginRequestMessageSimpleChannelInboundHandler loginRequestHandler=new LoginRequestMessageSimpleChannelInboundHandler();

	private static final GroupCreateRequestMessageSimpleChannelInboundHandler groupCreateHandler=new GroupCreateRequestMessageSimpleChannelInboundHandler();


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
									.addLast(FrameDecoderFactory.getInstance())
									.addLast(loggingHandler)
									.addLast(messageCodecSharable)
									.addLast(chatRequestHandler)
									.addLast(loginRequestHandler)
									.addLast(groupCreateHandler);

						}
					})
					.bind(new InetSocketAddress(9000)).sync();
			System.out.println("on");
			future.channel().closeFuture().sync();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}

}
