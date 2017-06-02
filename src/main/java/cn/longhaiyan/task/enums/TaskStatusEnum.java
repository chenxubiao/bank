package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-17.
 */
public enum TaskStatusEnum {

    PUBLISH(1, "发布中"),
    CLOSE_OWNER(2, "已取消,退款中"),        //发布者
    DONE_NOT_RECEIVE(3, "已关闭"),         //未接单，退款完成
    RECEIVE(4, "已接单,进行中"),
    SENDER_CLONING(13, "关闭中"),
    SENDER_AGREE_CLOSED(14, "接收方同意关闭，退款中"),
    //    DONE_SENDER_CLOSING(15, "已结束"),
    TAKER_CLONING(5, "关闭中"),            //已接单用户关闭,等待需求方确认
    TAKER_AGREE_CLOSED(6, "已同意关闭"),    //需求方同意关闭，退款中
    DONE_RECEIVE_CLOSE(7, "已结束"),       //接单后退款完成
    RECEIVE_COMPLETE(8, "已完成，核对付款中"),
    DONE_TASK(9, "已完成"),
    LOKING_PRESENT(10, "状态锁定中锁定中"),
    LOKING_TIME_OVER_PUBLISHING(11, "已超期，锁定中"), //未接单，任务超期
    LOKING_TIME_OVER_RECEIVE(12, "已超期，锁定中"),    //已接单，超期
    LOKING_TIME_DONE_PUB(16, "已完成（被重新发布）"),
    DELETE(20, "已删除"),  //完成任务已超期(已接单)
    LOKING(21, "举报次数过多，已屏蔽"),;


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
