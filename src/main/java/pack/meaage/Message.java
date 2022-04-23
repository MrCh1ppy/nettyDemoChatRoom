package pack.meaage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize
public abstract class Message implements Serializable {

	MessageType msgMessageType;
	int sequenceId;

	byte msgCode;

	public Message(MessageType msgMessageType, int sequenceId) {
		this.msgMessageType = msgMessageType;
		this.sequenceId = sequenceId;
		this.msgCode=msgMessageType.code;
	}

	public Message() {
	}

	public byte getMsgCode(){
		return msgCode;
	}
	public int getSequenceId(){
		return sequenceId;
	}

	public MessageType getMsgMessageType() {
		return msgMessageType;
	}
}
