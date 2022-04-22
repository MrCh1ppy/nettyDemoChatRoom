package pack.meaage;

import java.io.Serializable;

public abstract class Message implements Serializable {

	MessageType msgMessageType;
	int sequenceId;

	public byte getMsgCode(){
		return msgMessageType.code;
	}
	public int getSequenceId(){
		return sequenceId;
	}

	public MessageType getMsgMessageType() {
		return msgMessageType;
	}
}
