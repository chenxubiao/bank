package cn.longhaiyan.user.service;

import cn.longhaiyan.common.utils.TimeUtil;
import cn.longhaiyan.user.domain.UserLoginLog;
import cn.longhaiyan.user.repository.UserLoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by chenxb on 17-4-1.
 */
@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Override
    public void save(UserLoginLog userLoginLog) {
        if (userLoginLog == null) {
            return;
        }
        userLoginLogRepository.save(userLoginLog);
    }

    @Override
    public void logout(int userId) {
        if (userId <= 0) {
            return;
        }
        UserLoginLog userLoginLog = userLoginLogRepository.findFirstByUserIdOrderByIdDesc(userId);
        if (userLoginLog == null) {
            return;
        }
        userLoginLog.setLogoutTime(new Date());
        userLoginLog.setModifyTime(userLoginLog.getLogoutTime());
        userLoginLogRepository.save(userLoginLog);
    }

    @Override
    public UserLoginLog findTodayLoginLog(int userId) {
        if (userId <= 0) {
            return null;
        }
        Date todayBegin = TimeUtil.getTodayBegin();
        Date todayEnd = TimeUtil.disposeDate(todayBegin, 1, TimeUtil.DATE_TYPE_AFTER);
        return userLoginLogRepository.findFirstByUserIdAndAndCreateTimeBetweenOrderByIdAsc
                (userId, todayBegin, todayEnd);
    }

    @Override
    public UserLoginLog findYesterdayLoginLog(int userId) {
        if (userId <= 0) {
            return null;
        }
        Date todayBegin = TimeUtil.getTodayBegin();
        Date yesterdayBegin = TimeUtil.disposeDate(todayBegin, 1, TimeUtil.DATE_TYPE_BEFORE);
        return userLoginLogRepository.findFirstByUserIdAndAndCreateTimeBetweenOrderByIdAsc(userId, yesterdayBegin, todayBegin);
    }
}
