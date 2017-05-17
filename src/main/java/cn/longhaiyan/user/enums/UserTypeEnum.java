package cn.longhaiyan.user.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum UserTypeEnum {
    UNKNOWN(0,"未实名认证"), STUDENT(1,"学生"), TEACHER(2, "教师"), AUTHINT_STUDENT(3,"学生认证中"), AUTHINT_TEACHER(4, "教师认证中"), AUTH_FAILD(5, "认证不通过"),;

    private int code;
    private String value;

    UserTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static boolean isContain(int code) {
        for (UserTypeEnum typeEnum : UserTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return true;
            }

        }
        return false;
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
    }

    public static String getValue(int code) {
        for (UserTypeEnum type : UserTypeEnum.values()) {
            if (type.getCode() == code) {
                return type.getValue();
            }
        }
        return null;
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
