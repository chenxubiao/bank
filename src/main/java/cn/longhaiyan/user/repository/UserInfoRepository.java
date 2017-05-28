package cn.longhaiyan.user.repository;


import cn.longhaiyan.user.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long> {

    UserInfo findByEmail(String email);

    UserInfo findByCellphone(String cellphone);

    UserInfo findByUserName(String userName);

    @Query(value = "select count (a) from UserInfo a where a.email = ?1")
    int countByEmail(String email);

    @Query(value = "select count (a) from UserInfo a where a.cellphone = ?1")
    int countByCellphone(String cellphone);

    @Query(value = "select count (a) from UserInfo a where a.userName = ?1")
    int countByUserName(String userName);

    UserInfo findById(int id);

    UserInfo findByIdAndStatus(int id, int status);

    int countAllByIdGreaterThan(int id);

    List<UserInfo> findAllByUserTypeInOrderByModifyTimeDesc(List<Integer> userTypeList);

    Page<UserInfo> findAllByIdGreaterThanOrderByIdDesc(int id, Pageable pageable);

}
