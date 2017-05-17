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

    List<Message> findAllBySenderAndReceiverAndStatusOrderByIdDesc(int senderId, int receiverId, int status);

    @Query(value = "update Message a set a.status = ?2 where a.receiver = ?1")
    void disposeUnLookMsg(int userId, int status);

    List<Message> findAllByReceiverAndSenderIsNotAndStatusOrderByIdDesc(int receiver, int sender, int status);

    List<Message> findAllByTypeAndReceiverAndStatusOrderByIdDesc(int type, int receiver, int status);

    @Query(value = "select distinct a.sender from Message a where a.receiver = ?1 and not (a.sender = ?2) group by a.sender order by a.id desc ")
    List<Object> findSender(int receiver, int sender, int status);

    List<Message> findAllByTypeAndReceiverAndStatusAndSenderNotInOrderByIdDesc(int type, int receiver, int status, List<Integer> senderIds);

    int countBySenderAndReceiverAndStatusOrderByCreateTimeDesc(int sender, int receiver, int status);

    @Query(value = "select a from Message a where a.type = ?1 and a.sender = ?2 or a.sender = ?3 and a.receiver = ?2 or a.receiver = ?3 ORDER BY a.id desc ")
    List<Message> findUserChatLog(int type, int sender, int receiver);

}
