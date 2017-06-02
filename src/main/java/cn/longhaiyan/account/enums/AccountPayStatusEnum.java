package cn.longhaiyan.account.enums;

/**
 * Created by chenxb on 17-6-1.
 */
public enum AccountPayStatusEnum {

    PAYING(1, "已圈存"),
    SUCCESS(2, "充值成功"),
    FAILURE(3, "充值失败"),;

    private int code;
    private String value;

    AccountPayStatusEnum(int code, String value) {
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
