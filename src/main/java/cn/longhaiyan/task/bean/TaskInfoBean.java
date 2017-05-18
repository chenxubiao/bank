package cn.longhaiyan.task.bean;

import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.task.domain.TaskInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
public class TaskInfoBean {

    private String name;        //taskDetail回显用
    private int userId;

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

    public TaskInfoBean() {

    }

    public TaskInfoBean(TaskInfo taskInfo, String name, int userId) {
        this.userId = userId;
        this.name = name;
        this.title = taskInfo.getTitle();
        this.address = taskInfo.getAddress();
        this.money = taskInfo.getMoney();
        this.remark = taskInfo.getRemark();
        this.personal = taskInfo.getPersonal();
        this.demand = taskInfo.getDemand();
        this.serviceTime = taskInfo.getServiceTime();
        this.deadTime = taskInfo.getDeadTime();
        this.urgentMoney = taskInfo.getUrgentMoney();
        this.tags = taskInfo.getTags();
        this.description = taskInfo.getDesctiption();
        this.urgent = taskInfo.getUrgent();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
