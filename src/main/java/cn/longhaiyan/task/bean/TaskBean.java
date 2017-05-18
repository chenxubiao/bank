package cn.longhaiyan.task.bean;

import java.util.Date;

/**
 * Created by chenxb on 17-5-18.
 */
public class TaskBean {
    private String title;       //主题    不可空
    private String description; //描述    不可空
    private String remark;      //要求
    private String demand; //承接要求
    private int urgent;         //是否加急
    private String serviceTime; //服务时间
    private Date deadTime;      //截至日期
    private String address;     //服务地点
    private String personal;    //任务隐藏信息
    private int money;          //             非空
    private String tagIds;     //分类id
}
