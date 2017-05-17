package cn.longhaiyan.message.bean;

import cn.longhaiyan.message.domain.Message;
import java.util.List;

/**
 * Created by chenxb on 17-5-16.
 */
public class MsgInfo {
    private SenderInfo user;
    private int msgCount;
    private List<Message> messages;

    public MsgInfo() {
    }

    public MsgInfo(SenderInfo user, int msgCount, List<Message> messages) {
        this.messages = messages;
        this.user = user;
        this.msgCount = msgCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public SenderInfo getUser() {
        return user;
    }

    public void setUser(SenderInfo user) {
        this.user = user;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }
}
