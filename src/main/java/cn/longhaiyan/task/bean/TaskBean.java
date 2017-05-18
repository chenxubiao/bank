package cn.longhaiyan.task.bean;

import cn.longhaiyan.tag.domain.TagInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-18.
 */
public class TaskBean {

//    private String description; //描述    不可空
//    private String remark;      //备注
//    private String demand; //承接要求
//    private int urgent;         //是否加急
//    private String serviceTime; //服务时间
//    private Date deadTime;      //截至日期
//    private String personal;    //任务隐藏信息

    private String title;       //主题    不可空
    private String address;     //服务地点
    private int money;          //             非空
    private Date createTime;
    private List<TagInfo> tags;     //分类id

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
}
