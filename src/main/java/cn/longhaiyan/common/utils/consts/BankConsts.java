package cn.longhaiyan.common.utils.consts;

/**
 * Created by chenxb on 17-3-4.
 */
public class BankConsts {

    public static final String REDIS_PROJECT_PREFIX = "tb_";
    public static final String REDIS_STAT_TOTAL_KEY = REDIS_PROJECT_PREFIX + "stat_total";
    public static final String REDIS_TIMESTAMP_KEY = REDIS_PROJECT_PREFIX + "timestamp";
    public static final String PICTURE_PREFIX = "photo@";
    public static final String DATA = "data";
    public static final String UPLOAD_NAME = "uploadFile";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String USER_SESSION_KEY = "thisUser";
    public static final int PICTURE_UPLOAD_NAME_LEN = 128;
    public static final String PROTECTED_BASE_PATH = "/var/upload/bank/pictures";
    public static final String PROTECTED_PIC_DISPOSE_PTTH = "/var/upload/bank/pics";
    public static final long PICTURE_UPLOAD_MAX_SIZE = 3145728L;    //3M
    public static final String BANK_NAME = "时间银行";
    public static final String PAGINATION = "pagination";
    public static final int USER_IS_SYSTEM = 1;
    public static final int ZERO = 0;

    public static final int CRM_NORMAL = 0;
    public static final int CRM_ADMIN = 1;

    public static final class UserSelf {
        public static final int NOT_SELF = 0;
        public static final int SELF = 1;
    }

    public static final class UserFollow {
        public static final int NOT_FOLLOW = 0;
        public static final int FOLLOW = 1;
    }

    public static final class UserRole {
        public static final int USER_IS_COMMON = 1;
        public static final int USER_IS_STUDENT = 2;
        public static final int USER_IS_TEACHER = 3;
        public static final int USER_IS_OPERATOR = 4;
    }

    public static final class UserStatus{
        public static final int USER_IS_LOCKING = 0;
        public static final int USER_IS_NORMAL = 1;
        public static final int USER_IS_CLOSE = 2;
    }

    public static final class UserSex{
        public static final int SEX_UNKNOWN = 0;
        public static final int SEX_MALE = 1;
        public static final int SEX_FEMALE = 2;
    }

    public static final class AuthColumn {
        public static final int DEPT = 1;
        public static final int MAJOR = 2;
        public static final int UNKNOWN = 0;
    }

}
