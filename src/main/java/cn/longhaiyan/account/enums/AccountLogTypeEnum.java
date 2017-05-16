package cn.longhaiyan.account.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum AccountLogTypeEnum {

    ADD_REGESTER(1, "注册奖励"), ADD_LOGIN(2, "登录奖励"),;

    private int code;
    private String value;

    AccountLogTypeEnum(int code, String value) {
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
