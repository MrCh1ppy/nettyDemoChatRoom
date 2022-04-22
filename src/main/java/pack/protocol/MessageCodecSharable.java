package pack.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pack.meaage.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
	private static final byte[] magicNum = {1, 1, 4, 5, 1};

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		var byteArrayOutputStream = new ByteArrayOutputStream();
		var stream = new ObjectOutputStream(byteArrayOutputStream);
		stream.writeObject(msg);
		var text = byteArrayOutputStream.toByteArray();
		var len = text.length;
		var buf = ctx.alloc().buffer();
		buf.writeBytes(magicNum)
				//version
				.writeByte(1)
				//0jdk 1json read way
				.writeByte(0)
				//msg code
				.writeByte(msg.getMsgCode())
				//sequence id
				.writeInt(msg.getSequenceId())
				//len
				.writeInt(len)
				//text
				.writeBytes(text);
		out.add(buf);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.readBytes(5);
		in.readByte();
		var readWay=in.readByte();
		in.readByte();
		in.readInt();
		var len=in.readInt();
		var bytes = new byte[len];
		in.readBytes(bytes,0,len);
		if(readWay==(byte) 0){
			var stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			var message = (Message) stream.readObject();
			out.add(message);
		}
	}
}
