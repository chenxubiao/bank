package cn.longhaiyan.task.bean;

import cn.longhaiyan.common.utils.DateStringFormatUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.task.domain.TaskInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-18.
 */
public class TaskIndexBean {

//    private String description; //描述    不可空
//    private String remark;      //备注
//    private String demand; //承接要求
//    private int urgent;         //是否加急
//    private String serviceTime; //服务时间
//    private String personal;    //任务隐藏信息

    private int taskId;
    private Date deadTime;      //截至日期
    private String title;       //主题    不可空
    private String address;     //服务地点
    private int money;          //             非空
    private Date createTime;
    private List<TagInfo> tags;     //分类id
    private String time;

    public TaskIndexBean() {

    }

    public TaskIndexBean(TaskInfo taskInfo) {
        this.title = taskInfo.getTitle();
        this.address = taskInfo.getAddress();
        this.createTime = taskInfo.getCreateTime();
        this.money = taskInfo.getMoney();
        this.tags = taskInfo.getTags();
        this.taskId = taskInfo.getId();
        this.deadTime = taskInfo.getDeadTime();
    }

    public TaskIndexBean(TaskInfo taskInfo, boolean isEncrypt) {
        this.title = taskInfo.getTitle();
        this.address = isEncrypt ? convertString(taskInfo.getAddress()) : taskInfo.getAddress();
        this.createTime = taskInfo.getCreateTime();
        this.money = taskInfo.getMoney();
        this.tags = taskInfo.getTags();
        this.taskId = taskInfo.getId();
        this.deadTime = taskInfo.getDeadTime();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<TagInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagInfo> tags) {
        this.tags = tags;
    }

    public String getTime() {
        return DateStringFormatUtil.format(this.createTime);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }


    private String convertString(String value) {
        if (StringUtil.isBlank(value)) {
            return value;
        }
        return StringUtil.convertEncrypt(value);
    }

}
