package cn.longhaiyan.task.bean;


/**
 * Created by chenxb on 17-5-20.
 */
public class TaskBean {
    private TaskInfoBean taskInfo;
    private TaskFinishBean taskFinish;

    public TaskBean() {

    }

    public TaskBean(TaskInfoBean taskInfoBean, TaskFinishBean taskFinish) {
        this.taskInfo = taskInfoBean;
        this.taskFinish = taskFinish;
    }

    public TaskInfoBean getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfoBean taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskFinishBean getTaskFinish() {
        return taskFinish;
    }

    public void setTaskFinish(TaskFinishBean taskFinish) {
        this.taskFinish = taskFinish;
    }

}
