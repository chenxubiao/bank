package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-28.
 */
public enum TaskUpdateEnum {


    DELETE(1, "删除"),
    //    DONE(2, "完成"),      //发布者
    CLOSING(2, "申请关闭"),
    CLOSED(3, "确认关闭"),
    REPUBLISH(4, "重新发布"),;


    private int code;
    private String value;

    TaskUpdateEnum(int code, String value) {
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
