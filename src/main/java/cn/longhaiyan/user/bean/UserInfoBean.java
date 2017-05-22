package cn.longhaiyan.user.bean;

import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.user.domain.Student;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.domain.UserInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
public class UserInfoBean {

    private int userId;
    private String userName;
    private int avatarId;   //头像id
    private String cellphone;
    private int sex;    //性别
    private int userType;   //用户类型
    private String description = "";
    private String email;
    private Date birthday;
    private Student student;
    private Teacher teacher;
    private List<Integer> pics;
    public UserInfoBean() {
    }

    public UserInfoBean(UserSession userInfo) {
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserName();
        this.avatarId = userInfo.getAvatarId();
        this.birthday = userInfo.getBirthday();
        this.cellphone = userInfo.getCellphone();
        this.sex = userInfo.getSex();
        this.userType = userInfo.getUserType();
        this.description = userInfo.getDescription();
        this.email = userInfo.getEmail();
        this.teacher = userInfo.getTeacher();
        this.student = userInfo.getStudent();
    }

    public UserInfoBean(UserInfo userInfo) {
        this.userId = userInfo.getId();
        this.userName = userInfo.getUserName();
        this.avatarId = userInfo.getAvatarId();
        this.birthday = userInfo.getBirthday();
        this.cellphone = userInfo.getCellphone();
        this.description = userInfo.getDescription();
        this.userType = userInfo.getUserType();
        this.email = userInfo.getEmail();
        this.teacher = userInfo.getTeacher();
        this.sex = userInfo.getSex();
        this.student = userInfo.getStudent();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Integer> getPics() {
        return pics;
    }

    public void setPics(List<Integer> pics) {
        this.pics = pics;
    }
}
