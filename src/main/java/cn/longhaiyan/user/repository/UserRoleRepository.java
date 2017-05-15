package cn.longhaiyan.user.repository;

import cn.longhaiyan.user.domain.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenxb on 17-4-1.
 */
@Repository
@Transactional
public interface UserRoleRepository extends PagingAndSortingRepository<UserRole, Long> {

}
