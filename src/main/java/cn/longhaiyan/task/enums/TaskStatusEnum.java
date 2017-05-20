package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-17.
 */
public enum TaskStatusEnum {

    PUBLISH(1, "发布中"),
    CLOSE_OWNER(2, "发布者已取消,退款中"),
    DONE_NOT_RECEIVE(3, "未接单，已退款，已完成"),
    RECEIVE(4, "已接单,进行中"),
    RECEIVE_CLONING(5, "已接单用户关闭,等待需求方确认"),
    RECEIVE_CLOSED(6, "需求方同意关闭"),
    DONE_RECEIVE_CLOSE(7, "接单后退款完成，已结束"),
    RECEIVE_COMPLETE(8, "已完成，核对付款中"),
    DONE_RECEIVE(9, "已完成"),
    LOKING_RECEIVING(10, "接单锁定中"),
    LOKING_TIME_OVER(11, "完成任务已超期，锁定中"),;

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
