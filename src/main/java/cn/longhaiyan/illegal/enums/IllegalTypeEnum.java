package cn.longhaiyan.illegal.enums;

/**
 * Created by chenxb on 17-5-31.
 */
public enum IllegalTypeEnum {

    TASK(1,"任务"), USER(2,"用户");

    private int code;
    private String value;

    IllegalTypeEnum(int code, String value) {
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
