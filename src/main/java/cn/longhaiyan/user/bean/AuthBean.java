package cn.longhaiyan.user.bean;

/**
 * Created by chenxb on 17-5-16.
 */
public class AuthBean {
    private int no;         //学号或教师号
    private String name;    //真实姓名
    private String dept;    //学院或部门
    private String major;   //专业
    private int type;       //类型,0未知，1学生，2老师
    private String attachment;//附件，以逗号隔开

}
