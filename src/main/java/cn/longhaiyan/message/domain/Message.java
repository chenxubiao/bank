package cn.longhaiyan.message.domain;

import cn.longhaiyan.common.utils.DateStringFormatUtil;
import cn.longhaiyan.message.bean.SenderInfo;
import cn.longhaiyan.message.enums.MessageStatusEnum;
import cn.longhaiyan.message.enums.MessageTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxb on 17-5-11.
 */
@Entity
@Table(name = "tb_message")
public class Message implements Serializable {

    private static final long serialVersionUID = -6726439336174002333L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int type;
    private int projectId;  //相关id
    private int sender;     //发送者
    private int receiver;   //接收者
    private String message; //信息
    private int status;     //状态
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;
    @Transient
    private String time;
    @Transient
    private SenderInfo senderInfo;
    @Transient
    private SenderInfo receiverInfo;

    public Message() {

    }

    public Message(int type, int sender, int receiver, int projectId, String message) {
        this.sender = sender;
        this.status = MessageStatusEnum.SEND.getCode();
        this.type = type;
        this.receiver = receiver;
        this.projectId = projectId;
        this.message = message;
        this.createTime = new Date();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        if (this.type == MessageTypeEnum.TASK_PUBLISH.getCode()) {
            this.message = "「" + this.message + "」发布成功，请及时关注任务状态。";
        } else if (this.type == MessageTypeEnum.TASK_DEADTIME_OVER.getCode()) {
            this.message = "「" + this.message + "」任务已超期，请及时关注任务状态。";
        } else if (this.type == MessageTypeEnum.TASK_RECEIVED_TO_SENDER.getCode()) {
            this.message = "「" + this.message + "」已被接单，请及时关注任务状态。";
        } else if (this.type == MessageTypeEnum.TASK_RECEIVED_TO_RECEIVER.getCode()) {
            this.message = "「" + this.message + "」接单成功，请及时完成任务。";
        } else if (this.type == MessageTypeEnum.TASK_DONE.getCode()) {
            this.message = "「" + this.message + "」任务已完成，欢迎下次使用。";
        }else if (this.type == MessageTypeEnum.TASK_DONE_VERIFY.getCode()) {
            this.message = "「" + this.message + "」任务已被完成，请确认。";
        } else if (this.type == MessageTypeEnum.USER_AUTH_SUCCESS.getCode()) {
            this.message = "您的" + message + "认证，认证成功。";
        } else if (this.type == MessageTypeEnum.USER_AUTH_FAILED.getCode()) {
            this.message = "您的" + message + "认证，认证失败。";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public String getTime() {
        return DateStringFormatUtil.format(this.createTime);
    }

    public SenderInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(SenderInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }
}
