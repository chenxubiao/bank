package cn.longhaiyan.account.service;


import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;

import java.util.List;

/**
 * Created by chenxb on 17-5-13.
 */
public interface AccountLogService {
    void save(AccountLog accountLog);

    AccountLog findByPicAuth(int userId, int type, int projectId, Account account);

    void saveAll(List<AccountLog> accountLogList);

}
