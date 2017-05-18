package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-17.
 */
public enum TaskStatusEnum {
    PUBLISH(1,"发布中")
    ,CLOSE_OWNER(2,"发布者已取消,退款中")
    ,DONE_NOT_RECEIVE(3,"未接单，已退款，已完成")
    ,REVEIVE(4,"已接单,进行中")
    ,RECEIVE_CLONING(5,"已接单用户关闭,等待需求方确认")
    ,RECEIVE_CLOSING(6,"需求方同意，退款中")
    ,DONE_RECEIVE_CLOSE(7, "接单后退款完成，已结束")
    ,RECEIVE_COMPLETE(8,"已完成，核对付款中")
    ,DONE_RECEIVE(9,"已完成");

    private int code;
    private String value;

    TaskStatusEnum(int code, String value) {
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
