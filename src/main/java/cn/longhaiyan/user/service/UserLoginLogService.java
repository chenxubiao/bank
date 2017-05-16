package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.UserLoginLog;

/**
 * Created by chenxb on 17-4-1.
 */
public interface UserLoginLogService {

    void save(UserLoginLog userLoginLog);

    void logout(int userId);

    UserLoginLog findTodayLoginLog(int userId);

    UserLoginLog findYesterdayLoginLog(int userId);
}
