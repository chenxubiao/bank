package cn.longhaiyan.task.domain;

import cn.longhaiyan.common.utils.DateStringFormatUtil;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.task.bean.TaskInfoBean;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.enums.TaskInfoUrgentEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-16.
 */
@Entity
@Table(name = "tb_task_info")
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = 8658410027785369602L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;


    private int userId;
    private String title = "";          //主题
    private String desctiption = "";    //描述
    private String demand = "";         //承接要求
    private String remark = "";         //备注
    private String personal = "";       //任务隐藏信息
    private Date deadTime;      //有效期至
    private String address;     //服务地点
    private int urgent;         //是否加急
    private int urgentMoney;    //加急钱数
    private String serviceTime = "";    //服务时间
    private int money;
    private int status;
    private int finishId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taskInfo")
    List<TaskTag> taskTags = new ArrayList<>();
    private String time;
    @Transient
    private List<TagInfo> tags;

    public TaskInfo() {

    }

    public TaskInfo(TaskInfoBean taskInfoBean,int userId) {
        this.title = taskInfoBean.getTitle().trim();
        this.desctiption = taskInfoBean.getDescription().trim();
        this.remark = taskInfoBean.getRemark();
        this.demand = taskInfoBean.getDemand();
        this.money = taskInfoBean.getMoney();
        this.urgent = taskInfoBean.getUrgent();
        this.urgent = taskInfoBean.getUrgent() == TaskInfoUrgentEnum.NO_URGENT.getCode() ? 0 : taskInfoBean.getUrgentMoney();
        this.personal = taskInfoBean.getPersonal();
        this.deadTime = taskInfoBean.getDeadTime();
        this.address = taskInfoBean.getAddress();
        this.serviceTime = taskInfoBean.getServiceTime();
        this.status = TaskStatusEnum.PUBLISH.getCode();
        this.createTime = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TaskTag> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(List<TaskTag> taskTags) {
        this.taskTags = taskTags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TagInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagInfo> tags) {
        this.tags = tags;
    }

    public int getFinishId() {
        return finishId;
    }

    public void setFinishId(int finishId) {
        this.finishId = finishId;
    }
}
