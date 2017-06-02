package cn.longhaiyan.illegal.enums;

/**
 * Created by chenxb on 17-5-31.
 */
public enum IllegalStatusEnum {

    SEND(1, "已举报"),
    LOKING(2, "系统已屏蔽"),
    VIEWED(3, "处理中"),
    SUCCESS(4, "审核通过"),
    FAILURE(5, "举报无效"),;

    private int code;
    private String value;

    IllegalStatusEnum(int code, String value) {
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
