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
    TASK_RECEIVED_TO_RECEIVER(7, "任务通知"),  //任务抢单成功
    TASK_DEADTIME_OVER(8, "任务通知"),
    TASK_DEADTIME_OVER_REMIND(9, "任务通知"),
    TASK_DONE(10, "任务通知"),           //任务超期
    TASK_DONE_VERIFY(11, "任务通知"),
    TASK_DELETE(20, "任务通知"),
    TASK_PAUSE(14, "任务通知"),         //任务未接单就结束
    TASK_CLOSING_BY_SENDER_TO_SENDER(15, "任务通知"),
    TASK_CLOSING_BY_SENDER_TO_TAKER(16, "任务通知"),
    TASK_CLOSING_BY_TAKER_TO_SENDER(17, "任务通知"),
    TASK_CLOSING_BY_TAKER_TO_TAKER(18, "任务通知"),
    TASK_CLOSED(19, "任务通知"),        //申请关闭流程结束，已关闭，已退款
    USER_AUTH_SUCCESS(12, "认证通知"),  //认证成功
    USER_AUTH_FAILED(13, "认证通知"),
    ADMIN_PAY_ACCOUNT(14, "账户充值"),
    ADMIN_PAY_ACCOUNT_DEL(15, "账户扣除"),;


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
