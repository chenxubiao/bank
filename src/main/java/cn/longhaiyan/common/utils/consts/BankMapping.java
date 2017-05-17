package cn.longhaiyan.common.utils.consts;


import cn.longhaiyan.common.utils.ConstStrings;
import cn.longhaiyan.user.enums.UserTypeEnum;

import java.util.*;

/**
 * Created by chenxb on 17-3-5.
 */
public class BankMapping {

    public static final Map<Integer, String> USER_AUTHED_TYPE_MAPPINT = new HashMap<>();
    static {
        USER_AUTHED_TYPE_MAPPINT.put(UserTypeEnum.STUDENT.getCode(), UserTypeEnum.STUDENT.getValue());
        USER_AUTHED_TYPE_MAPPINT.put(UserTypeEnum.AUTHINT_STUDENT.getCode(), UserTypeEnum.AUTHINT_STUDENT.getValue());
        USER_AUTHED_TYPE_MAPPINT.put(UserTypeEnum.TEACHER.getCode(), UserTypeEnum.TEACHER.getValue());
        USER_AUTHED_TYPE_MAPPINT.put(UserTypeEnum.AUTHINT_TEACHER.getCode(), UserTypeEnum.AUTHINT_TEACHER.getValue());
        USER_AUTHED_TYPE_MAPPINT.put(UserTypeEnum.AUTH_FAILD.getCode(), UserTypeEnum.AUTH_FAILD.getValue());
    }

    public static final Map<Integer, String> USER_ROLE_MAPPING = new HashMap<>();

    static {
        USER_ROLE_MAPPING.put(BankConsts.UserRole.USER_IS_COMMON, "注册用户");
        USER_ROLE_MAPPING.put(BankConsts.UserRole.USER_IS_OPERATOR, "系统管理员");
    }

    public static final List<String> VALID_EXTENSIONS_LIST = new ArrayList<>();

    static {
        VALID_EXTENSIONS_LIST.add(ConstStrings.PICTURE_BMP);
        VALID_EXTENSIONS_LIST.add(ConstStrings.PICTURE_GIF);
        VALID_EXTENSIONS_LIST.add(ConstStrings.PICTURE_JPEG);
        VALID_EXTENSIONS_LIST.add(ConstStrings.PICTURE_JPG);
        VALID_EXTENSIONS_LIST.add(ConstStrings.PICTURE_PNG);
    }

    public static final Map<Integer, String> USER_SEX_MAPPING = new LinkedHashMap<>();
    static {
        USER_SEX_MAPPING.put(BankConsts.UserSex.SEX_UNKNOWN, "未知");
        USER_SEX_MAPPING.put(BankConsts.UserSex.SEX_MALE, "男");
        USER_SEX_MAPPING.put(BankConsts.UserSex.SEX_FEMALE, "女");
    }

    public static final List<String> PICTURE_PROJECT_LIST = new ArrayList<>();
    static {
        PICTURE_PROJECT_LIST.add(ConstStrings.PICTURE_JPEG);
        PICTURE_PROJECT_LIST.add(ConstStrings.PICTURE_JPG);
    }
}
