package cn.longhaiyan.message.service;

import cn.longhaiyan.message.domain.Message;

import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
public interface MessageService {
    void save(Message message);

    int countUnLookMsg(int userId);

    List<Message> findUnLookMsg(int sender, int receiverId);

    void updateUnlookMsg(int senderId, int receiverId);

    List<Message> findUnLookSysMsg(int receiver);

    List<Message> finViewedSysMsg(int receiver);

    List<Message> findUnLookUserMsg(int receiver);

    List<Message> findViewedUserMsg(int receiver);

    List<Message> findViewedUserMsgNotInUserIds(int receiver, List<Integer> senderIds);

    int countBySenderAndReceiverUnlookMsg(int sender, int receiver);

    int countBySenderAndReceiverViewedMsg(int sender, int receiver);

    List<Message> findUserChatLog(int userId, int selfId);
}
