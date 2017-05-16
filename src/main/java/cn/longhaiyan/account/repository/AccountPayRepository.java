package cn.longhaiyan.account.repository;

import cn.longhaiyan.account.domain.AccountPay;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenxb on 17-5-15.
 */
@Repository
@Transactional
public interface AccountPayRepository extends PagingAndSortingRepository<AccountPay, Long> {

}
