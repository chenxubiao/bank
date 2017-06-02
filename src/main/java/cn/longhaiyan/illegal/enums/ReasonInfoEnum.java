package cn.longhaiyan.illegal.enums;

/**
 * Created by chenxb on 17-5-31.
 */
public enum ReasonInfoEnum {


    SEND(1,"已发送"), VIEWED(2,"已查看");

    private int code;
    private String value;

    ReasonInfoEnum(int code, String value) {
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
