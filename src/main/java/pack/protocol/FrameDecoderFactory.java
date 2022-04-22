package pack.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class FrameDecoderFactory {
	private static final int MAX_FRAME_LEN =1024;
	private static final int LEN_FIELD_OFF_SET =12;
	private static final int LEN_FIELD_LENGTH =4;
	private static final int LEN_ADJUSTMENT =0;
	private static final int INITIAL_BYTE_TO_STRIP =0;

	private FrameDecoderFactory() {
	}

	public static LengthFieldBasedFrameDecoder getInstance(){
		return new LengthFieldBasedFrameDecoder(MAX_FRAME_LEN, LEN_FIELD_OFF_SET, LEN_FIELD_LENGTH, LEN_ADJUSTMENT, INITIAL_BYTE_TO_STRIP);
	}
}
