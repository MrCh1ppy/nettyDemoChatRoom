package pack.meaage;

import lombok.Data;
import lombok.ToString;
import pack.meaage.Message;

@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    private boolean success;
    private String reason;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}
