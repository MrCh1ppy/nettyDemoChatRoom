package pack.protocol.edcoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import pack.meaage.Message;
import pack.meaage.MessageType;
import pack.protocol.serilizer.Serializer;

import java.util.List;

@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
	private static final byte[] magicNum = {1, 1, 4, 5, 1};

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		var buf = ctx.alloc().buffer();
		var version = 1;
		var serializeWay = 1;

		var text=switch (serializeWay){
			case 0-> Serializer.Algorithm.JDK.serialize(msg);
			case 1->Serializer.Algorithm.JSON.serialize(msg);
			default -> new byte[0];
		};

		var len=text.length;

		buf.writeBytes(magicNum)
				//version
				.writeByte(version)
				//0jdk 1json read way
				.writeByte(serializeWay)
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
		var msgCode=in.readByte();
		in.readInt();
		var len=in.readInt();
		var bytes = new byte[len];
		in.readBytes(bytes,0,len);
		var object=switch (readWay){
			case 0->Serializer.Algorithm.JDK.deserialize(Message.class,bytes);
			case 1->{
				var type = MessageType.values()[msgCode].getClazz();
				yield Serializer.Algorithm.JSON.deserialize(type, bytes);
			}
			default -> throw new IllegalStateException("Unexpected value: " + readWay);
		};
		out.add(object);
	}
}
