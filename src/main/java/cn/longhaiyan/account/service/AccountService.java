package cn.longhaiyan.account.service;


import cn.longhaiyan.account.domain.Account;

/**
 * Created by chenxb on 17-5-13.
 */
public interface AccountService {

    void save(Account account);

    Account findByUserId(int userId);
}
