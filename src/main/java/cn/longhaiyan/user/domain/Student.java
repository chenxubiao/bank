package cn.longhaiyan.user.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxb on 17-5-10.
 */
@Entity
@Table(name = "tb_user_student")
public class Student implements Serializable{

    private static final long serialVersionUID = -2238941008907021063L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;
    private String sno;
    private String name;
    private String dept;
    private String major;
    private String remark = "";

    public Student() {

    }

    public Student(String sno, String name, String dept, String major) {
        this.sno = sno;
        this.name = name;
        this.dept = dept;
        this.major = major;
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

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
