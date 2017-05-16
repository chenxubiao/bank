package cn.longhaiyan.user.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum UserRoleEnum {
    COMMON(1,"普通用户"), STUDENT(2,"学生"), TEACHER(3,"教职工"), OPERATOR(4, "用户管理员");

    private int code;
    private String value;

    UserRoleEnum(int code, String value) {
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
