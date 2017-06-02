package cn.longhaiyan.account.service;

import cn.longhaiyan.account.domain.AccountPay;

import java.util.List;

/**
 * Created by chenxb on 17-5-15.
 */
public interface AccountPayService {

    void save(AccountPay accountPay);

    List<AccountPay> findAll();
}
