package pack.meaage;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
        this.msgMessageType=MessageType.GROUP_QUIT_RESPONSE;
    }

}
