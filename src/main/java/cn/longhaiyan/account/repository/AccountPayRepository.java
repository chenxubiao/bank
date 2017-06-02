package cn.longhaiyan.account.repository;

import cn.longhaiyan.account.domain.AccountPay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenxb on 17-5-15.
 */
@Repository
@Transactional
public interface AccountPayRepository extends PagingAndSortingRepository<AccountPay, Long> {

    @Query(value = "select a from AccountPay a ORDER BY a.id desc ")
    List<AccountPay> findAll();
}
