package cn.longhaiyan.user.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum  UserSexEnum {
    USER_SEX_UNKNOWN(0,"未知"), USER_SEX_MAN(1,"男"), USER_SEX_FEMALE(2,"女");

    private int code;
    private String value;

    private UserSexEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static boolean isContain(int code) {
        for (UserSexEnum sexEnum : UserSexEnum.values()) {
            if (sexEnum.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
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
