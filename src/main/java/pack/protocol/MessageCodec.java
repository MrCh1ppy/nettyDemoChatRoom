package pack.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pack.meaage.Message;
import pack.meaage.MessageType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MessageCodec extends ByteToMessageCodec<Message> {
	private static final byte[] magicNum = {1, 1, 4, 5, 1};
	private static final Logger log=LoggerFactory.getLogger("check");

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		var byteArrayOutputStream = new ByteArrayOutputStream();
		var stream = new ObjectOutputStream(byteArrayOutputStream);
		stream.writeObject(msg);
		var text = byteArrayOutputStream.toByteArray();
		var len = text.length;
		out.writeBytes(magicNum)
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
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		var magicBuf = in.readBytes(5);
		var version = in.readByte();
		var msgCode = MessageType.values()[in.readByte()];
		var readWay = in.readByte();
		var sequenceId = in.readInt();
		var len = in.readInt();
		var bytes = new byte[len];
		in.readBytes(bytes,0,len);
		log.debug("{},{},{},{},{},{}",magicBuf,version,readWay,len,sequenceId,msgCode.code());
		System.out.println(magicBuf+" "+version+" "+readWay+" "+len+" "+sequenceId+" "+msgCode.code());
		if(readWay==(byte) 0){
			var stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			var message = (Message) stream.readObject();
			out.add(message);
		}
	}
}
