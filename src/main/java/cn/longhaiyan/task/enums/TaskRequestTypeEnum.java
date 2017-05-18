package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-18.
 */
public enum TaskRequestTypeEnum {
    PUBLISHING(0, "发布中"), SERVICEING(1, "服务中"), DONE_RECEIVE(2, "已完成"),;
    private int code;
    private String value;

    TaskRequestTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
    }

    public static boolean isContain(int code) {
        for (TaskRequestTypeEnum typeEnum : TaskRequestTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return true;
            }
        }
        return false;
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
