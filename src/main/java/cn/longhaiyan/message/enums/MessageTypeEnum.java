package cn.longhaiyan.message.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum MessageTypeEnum {

    REGEISTER(1, "注册"),
    LOGIN(2, "连续登录"),
    ACCOUNT_CHANGE(3, "账户变动"),
    USER_MSG(4, "用户私信"),
    TASK_PUBLISH(5, "任务通知"),
    TASK_RECEIVED_TO_SENDER(6, "任务通知"),     //任务已被接单
    TASK_REVEIVED_TO_RECEIVER(7, "任务通知"),  //任务抢单成功
    TASK_DEADTIME_OVER(8, "任务通知"),;           //任务超期


    private int code;
    private String value;

    MessageTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
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
