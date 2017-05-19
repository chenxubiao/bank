package cn.longhaiyan.task.bean;

import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.user.bean.User;

import java.util.Date;

/**
 * Created by chenxb on 17-5-19.
 */
public class TaskFinishBean {

    private int finishId;
    private Date finishTime;
    private String remark;
    private Date createTime;
    private String status;
    private int taskId;
    private User sender;
    private User taker;

    public TaskFinishBean() {
    }

    public TaskFinishBean(TaskFinish taskFinish, User senderInfo, User taker) {
        this.sender = senderInfo;
        this.taker = taker;
        this.taskId = taskFinish.getTaskId();
        this.finishId = taskFinish.getId();
        this.createTime = taskFinish.getCreateTime();
        this.finishTime = taskFinish.getFinishTime();
        this.remark = taskFinish.getRemark();
        this.status = TaskStatusEnum.getValue(taskFinish.getStatus());
    }

    public int getFinishId() {
        return finishId;
    }

    public void setFinishId(int finishId) {
        this.finishId = finishId;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getTaker() {
        return taker;
    }

    public void setTaker(User taker) {
        this.taker = taker;
    }
}
