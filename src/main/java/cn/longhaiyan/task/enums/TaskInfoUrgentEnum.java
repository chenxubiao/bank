package cn.longhaiyan.task.enums;

/**
 * Created by chenxb on 17-5-17.
 */
public enum TaskInfoUrgentEnum {
    NO_URGENT(0, "不加急"),URGENT(1,"加急");

    private int code;
    private String value;

    TaskInfoUrgentEnum(int code, String value) {

        this.code = code;
        this.value = value;
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
    }

    public static boolean isContain(int code) {
        for (TaskInfoUrgentEnum urgentEnum : TaskInfoUrgentEnum.values()) {
            if (urgentEnum.getCode() == code) {
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
