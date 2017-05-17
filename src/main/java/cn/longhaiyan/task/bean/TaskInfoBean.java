package cn.longhaiyan.task.bean;

import java.util.Date;

/**
 * Created by chenxb on 17-5-17.
 */
public class TaskInfoBean {
    private String title = "";       //主题    不可空
    private String desctiption = ""; //描述    不可空
    private String remark = "";      //要求
    private String demand = ""; //承接要求
    private int urgent;         //是否加急
    private int urgentMoney;    //加急钱数
    private String serviceTime; //服务时间
    private Date deadTime;      //截至日期
    private String address;     //服务地点
    private String personal;    //任务隐藏信息
    private int money;          //             非空
    private int categoryId;     //分类id

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
