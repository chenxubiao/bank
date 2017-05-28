package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-17.
 */
public enum TaskStatusEnum {

    PUBLISH(1, "发布中"),
    CLOSE_OWNER(2, "已取消,退款中"),      //发布者
    DONE_NOT_RECEIVE(3, "已结束"),     //未接单，已退款，已结束
    RECEIVE(4, "已接单,进行中"),
    RECEIVE_CLONING(5, "关闭中"),      //已接单用户关闭,等待需求方确认
    RECEIVE_CLOSED(6, "已同意关闭"),   //需求方同意关闭，退款中
    DONE_RECEIVE_CLOSE(7, "已结束"),   //接单后退款完成
    RECEIVE_COMPLETE(8, "已完成，核对付款中"),
    DONE_TASK(9, "已完成"),
    LOKING_RECEIVING(10, "接单锁定中"),
    LOKING_TIME_OVER_PUBLISHING(11, "已超期，锁定中"), //未接单，任务超期
    LOKING_TIME_OVER_RECEIVE(12, "已超期，锁定中"),
    DELETE(20, "已删除"),;  //完成任务已超期(已接单)


    private int code;
    private String value;

    TaskStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
    }

    public static boolean isContain(int code) {
        for (TaskStatusEnum statusEnum : TaskStatusEnum.values()) {
            if (statusEnum.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    public static String getValue(int code) {
        for (TaskStatusEnum statusEnum : TaskStatusEnum.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum.getValue();
            }
        }
        return "未知";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
