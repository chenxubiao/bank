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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
