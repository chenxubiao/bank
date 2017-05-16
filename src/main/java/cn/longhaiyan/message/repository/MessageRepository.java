package cn.longhaiyan.message.repository;

import cn.longhaiyan.message.domain.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
@Repository
@Transactional
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    int countByReceiverAndStatus(int userId, int status);

    List<Message> findAllByReceiverAndStatusOrderByIdDesc(int userId, int status);

    @Query(value = "update Message a set a.status = ?2 where a.receiver = ?1")
    void disposeUnLookMsg(int userId, int status);

}
