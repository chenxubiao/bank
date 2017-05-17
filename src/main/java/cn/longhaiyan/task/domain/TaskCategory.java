package cn.longhaiyan.task.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxb on 17-5-16.
 */
@Entity
@Table(name = "tb_task_category_log")
public class TaskCategory implements Serializable {

    private static final long serialVersionUID = -577636118035692212L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;
//    private int taskId;
    private int cateogryId;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = true)
    @JoinColumn(name = "taskId")
    private TaskInfo taskInfo;

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
//
//    public int getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(int taskId) {
//        this.taskId = taskId;
//    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getCateogryId() {
        return cateogryId;
    }

    public void setCateogryId(int cateogryId) {
        this.cateogryId = cateogryId;
    }
}
