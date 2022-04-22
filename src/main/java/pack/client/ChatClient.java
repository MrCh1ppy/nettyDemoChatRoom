package pack.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import pack.client.handler.ChatResponseHandler;
import pack.client.handler.GroupCreateResponseHandler;
import pack.meaage.*;
import pack.protocol.FrameDecoderFactory;
import pack.protocol.MessageCodecSharable;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class ChatClient {
	private static final LoggingHandler loggingHandler=new LoggingHandler(LogLevel.DEBUG);
	private static final MessageCodecSharable messageCodecSharable=new MessageCodecSharable();
	private static final GroupCreateResponseHandler groupCreateHandler=new GroupCreateResponseHandler();

	public static void main(String[] args) {
		var worker = new NioEventLoopGroup();
		var waitForLogin = new CountDownLatch(1);
		var flag = new AtomicBoolean(false);
		try {
			var handler = new Bootstrap()
					.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(FrameDecoderFactory.getInstance())
									//.addLast(loggingHandler)
									.addLast(messageCodecSharable)
									.addLast(new SimpleChannelInboundHandler<LoginResponseMessage>() {
										@Override
										protected void channelRead0(ChannelHandlerContext ctx, LoginResponseMessage msg) throws Exception {
											log.debug("{}",msg);
											if(msg.isSuccess()){
												flag.set(true);
												log.debug("登录成功");
												System.out.println("登录成功");
											}
											waitForLogin.countDown();
										}
									})
									.addLast(new ChatResponseHandler())
									.addLast("client handler",new ChannelInboundHandlerAdapter(){

										@Override
										public void channelActive(ChannelHandlerContext ctx) throws Exception {
											new Thread(()->{
												var sc = new Scanner(System.in);
												System.out.println("lay down ur username");
												var username = sc.nextLine().trim();
												System.out.println("lay down u passsword");
												var password = sc.nextLine().trim();
												//build Object
												var msg = new LoginRequestMessage(username, password);
												ctx.writeAndFlush(msg);
												try {
													waitForLogin.await();
												} catch (InterruptedException e) {
													throw new RuntimeException(e);
												}
												if(!flag.get()){
													System.out.println("登录失败,closing..");
													sc.close();
													ctx.channel().close();
													return;
												}

												while (true){
													System.out.println("================================");
													System.out.println("send [username] [text]");
													System.out.println("gsend [groupName] [content]");
													System.out.println("gcreate [groupName] [m1,m2...]");
													System.out.println("================================");
													var order = sc.nextLine().split(" ");
													switch (order[0]){
														case "send"-> {
															var sendMsg = new ChatRequestMessage(username, order[1], order[2]);
															ctx.writeAndFlush(sendMsg);
														}
														case "gsend"-> {
															var senMsg = new GroupChatRequestMessage(username, order[1], order[2]);
															ctx.writeAndFlush(senMsg);
														}
														case "gcreate"-> {
															var set = Arrays
																	.stream(order[2].split(","))
																	.collect(Collectors.toSet());
															var m = new GroupCreateRequestMessage(order[1], set);
															ctx.writeAndFlush(m);
														}
														case "gmembers"-> {
															var m = new GroupMembersRequestMessage(order[1]);
															ctx.writeAndFlush(m);
														}
														case "gjoin"-> {
															var m = new GroupJoinRequestMessage(order[1], order[2]);
															ctx.writeAndFlush(m);
														}
														case "gquit"-> {
															var m = new GroupQuitRequestMessage(order[1], order[2]);
															ctx.writeAndFlush(m);
														}
														case "quit"-> {
															System.out.println("退出。。。");
														}
														default -> System.out.println("");
													}
												}

											},"in").start();
										}
									})
									.addLast(groupCreateHandler)
							;
						}
					});
			var channel = handler.connect(new InetSocketAddress("localhost", 9000)).sync().channel();
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}finally {
			worker.shutdownGracefully();
		}
	}

}
