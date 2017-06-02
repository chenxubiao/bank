package cn.longhaiyan.account.service;

import cn.longhaiyan.account.domain.AccountPay;
import cn.longhaiyan.account.repository.AccountPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenxb on 17-5-15.
 */
@Service
public class AccountPayServiceImpl implements AccountPayService {
    @Autowired
    private AccountPayRepository accountPayRepository;

    @Override
    public void save(AccountPay accountPay) {
        if (accountPay == null) {
            return;
        }
        accountPayRepository.save(accountPay);
    }

    @Override
    public List<AccountPay> findAll() {
        return accountPayRepository.findAll();
    }
}
