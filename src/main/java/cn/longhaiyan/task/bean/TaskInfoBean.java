package cn.longhaiyan.task.bean;

import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.user.bean.User;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
public class TaskInfoBean {

    private User senderInfo;
    private User finisherInfo;
    private String title = "";       //主题    不可空
    private String description = ""; //描述    不可空
    private String remark = "";      //要求
    private String demand = ""; //承接要求
    private int urgent;         //是否加急
    private int urgentMoney;    //加急钱数
    private String serviceTime = ""; //服务时间
    private Date deadTime;      //截至日期
    private String address = "";     //服务地点
    private String personal = "";    //任务隐藏信息
    private int money;          //             非空
    private String tagIds;     //标签id         非空
    private List<TagInfo> tags;
    private String status;


    public TaskInfoBean() {

    }

    public TaskInfoBean(TaskInfo taskInfo, User senderInfo, User finisherInfo, boolean isEncrypt) {

        this.finisherInfo = finisherInfo;
        this.senderInfo = senderInfo;
        this.title = taskInfo.getTitle();
        this.address = isEncrypt ? StringUtil.convertEncrypt(taskInfo.getAddress()) : taskInfo.getAddress();
        this.status = TaskStatusEnum.getValue(taskInfo.getStatus());
        this.money = taskInfo.getMoney();
        this.remark = taskInfo.getRemark();
        this.personal = isEncrypt ? StringUtil.convertEncrypt(taskInfo.getPersonal()) : taskInfo.getPersonal();
        this.demand = taskInfo.getDemand();
        this.serviceTime = taskInfo.getServiceTime();
        this.deadTime = taskInfo.getDeadTime();
        this.urgentMoney = isEncrypt ? BankConsts.ZERO : taskInfo.getUrgentMoney();
        this.tags = taskInfo.getTags();
        this.description = taskInfo.getDesctiption();
        this.urgent = isEncrypt ? BankConsts.ZERO : taskInfo.getUrgent();
        this.tags = taskInfo.getTags();
    }

    public User getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(User senderInfo) {
        this.senderInfo = senderInfo;
    }

    public User getFinisherInfo() {
        return finisherInfo;
    }

    public void setFinisherInfo(User finisherInfo) {
        this.finisherInfo = finisherInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public int getUrgentMoney() {
        return urgentMoney;
    }

    public void setUrgentMoney(int urgentMoney) {
        this.urgentMoney = urgentMoney;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public List<TagInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagInfo> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
