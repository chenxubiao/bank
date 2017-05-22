package cn.longhaiyan.account.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum AccountLogTypeEnum {

    ADD_REGESTER(1, "注册奖励")
    , ADD_LOGIN(2, "登录奖励")
    , DEL_TASK_PUB(3, "需求发布")
    , DEL_TASK_URGENT(4, "公益资助")
    , ADD_TASK_DONE(5, "需求完成"),;

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
