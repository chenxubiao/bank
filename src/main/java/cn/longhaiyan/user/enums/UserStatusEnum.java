package cn.longhaiyan.user.enums;

/**
 * Created by chenxb on 17-5-16.
 */
public enum UserStatusEnum {
    LOCKING(0, "锁定"), NORMAL(1, "正常"), CLOSE(2, "关闭"),;

    private int code;
    private String value;

    UserStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(int code) {
        for (UserStatusEnum statusEnum : UserStatusEnum.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum.getValue();
            }
        }
        return "未知";
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
