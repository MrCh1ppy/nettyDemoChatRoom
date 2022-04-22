package pack.meaage;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends Message {

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
        this.msgMessageType=MessageType.GROUP_MEMBERS_RESPONSE;
    }


}
