package pack.meaage;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupJoinResponseMessage extends AbstractResponseMessage {

    public GroupJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
        this.msgMessageType=MessageType.GROUP_JOIN_RESPONSE;
    }


}
