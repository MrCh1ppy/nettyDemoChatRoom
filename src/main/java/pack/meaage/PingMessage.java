package pack.meaage;

import lombok.Data;

@Data
public class PingMessage extends Message{

	public PingMessage() {
		this.msgMessageType=MessageType.PING_MESSAGE;
	}
}
