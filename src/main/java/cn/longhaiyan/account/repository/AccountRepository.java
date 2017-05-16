package cn.longhaiyan.account.repository;

import cn.longhaiyan.account.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chenxb on 17-5-13.
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    int countByUserId(int userId);

    Account findByUserId(int userId);

}
