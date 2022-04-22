package pack.meaage;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupMembersRequestMessage extends Message {
    private String groupName;

    public GroupMembersRequestMessage(String groupName) {
        this.groupName = groupName;
        this.msgMessageType=MessageType.GROUP_MEMBERS_REQUEST;
    }


}
