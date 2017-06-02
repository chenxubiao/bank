package cn.longhaiyan.admin.enums;

/**
 * Created by chenxb on 17-6-2.
 */
public enum PayTypeEnum {
    ADD(1, "充值"), DELETE(2, "扣除"),;

    private int code;
    private String value;

    PayTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(int code) {
        for (PayTypeEnum typeEnum : PayTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum.getValue();
            }
        }
        return "未知";
    }

    public static boolean isNotContain(int code) {
        return !isContain(code);
    }

    public static boolean isContain(int code) {
        for (PayTypeEnum typeEnum : PayTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return true;
            }
        }
        return false;
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
