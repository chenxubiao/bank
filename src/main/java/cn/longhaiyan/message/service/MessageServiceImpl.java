package cn.longhaiyan.message.service;

import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.message.bean.SenderInfo;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageStatusEnum;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.repository.MessageRepository;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public void save(Message message) {
        if (message == null) {
            return;
        }
        messageRepository.save(message);
    }

    @Override
    public int countUnLookMsg(int userId) {
        if (userId <= 0) {
            return 0;
        }
        return messageRepository.countByReceiverAndStatus(userId, MessageStatusEnum.SEND.getCode());
    }

    @Override
    public List<Message> findUnLookMsg(int senderId, int receiverId) {
        if (senderId <= 0 || receiverId <= 0) {
            return null;
        }
        List<Message> messageList = messageRepository.findAllBySenderAndReceiverAndStatusOrderByIdDesc
                (senderId, receiverId, MessageStatusEnum.SEND.getCode());


        if (CollectionUtil.isNotEmpty(messageList)) {
            for (Message message : messageList) {
                if (message.getSender() > 0) {
                    UserInfo userInfo = userInfoService.findById(message.getSender());
                    if (userInfo != null) {
                        SenderInfo senderInfo = new SenderInfo(userInfo);
                        message.setSenderInfo(senderInfo);
                    }
                } else {
                    if (message.getType() == MessageTypeEnum.ACCOUNT_CHANGE.getCode()) {
                        message.setMessage("账户变动通知：" + message.getMessage() + "朵小红花,欢迎使用～");
                    }
                }
            }
        }
        return messageList;
    }

    @Override
    public void updateUnlookMsg(int senderId, int receiverId) {
        if (receiverId <= 0 || senderId <= 0) {
            return;
        }
        List<Message> messageSendList = messageRepository.findAllBySenderAndReceiverAndStatusOrderByIdDesc
                (senderId, receiverId, MessageStatusEnum.SEND.getCode());
        if (CollectionUtil.isNotEmpty(messageSendList)) {
            List<Message> messageViewList = new ArrayList<>();
            for (Message message : messageSendList) {
                message.setStatus(MessageStatusEnum.VIEWED.getCode());
                message.setModifyTime(new Date());
                messageViewList.add(message);
            }
            messageRepository.save(messageViewList);
        }
    }

    @Override
    public List<Message> findUnLookSysMsg(int receiver) {
        if (receiver <= 0) {
            return null;
        }
        return messageRepository.findAllBySenderAndReceiverAndStatusOrderByIdDesc
                (BankConsts.USER_IS_SYSTEM, receiver, MessageStatusEnum.SEND.getCode());
    }

    @Override
    public List<Message> finViewedSysMsg(int receiver) {
        if (receiver <= 0) {
            return null;
        }
        return messageRepository.findAllBySenderAndReceiverAndStatusOrderByIdDesc
                (BankConsts.USER_IS_SYSTEM, receiver, MessageStatusEnum.VIEWED.getCode());
    }

    @Override
    public List<Message> findUnLookUserMsg(int receiver) {
        if (receiver <= 0) {
            return null;
        }
        return messageRepository.findAllByTypeAndReceiverAndStatusOrderByIdDesc
                (MessageTypeEnum.USER_MSG.getCode(), receiver, MessageStatusEnum.SEND.getCode());
    }

    @Override
    public List<Message> findViewedUserMsg(int receiver) {
        if (receiver <= 0) {
            return null;
        }
        return messageRepository.findAllByTypeAndReceiverAndStatusOrderByIdDesc
                (MessageTypeEnum.USER_MSG.getCode(), receiver, MessageStatusEnum.VIEWED.getCode());
    }

    @Override
    public List<Message> findViewedUserMsgNotInUserIds(int receiver, List<Integer> senderIds) {
        if (CollectionUtil.isEmpty(senderIds) || receiver <= 0) {
            return null;
        }
        return messageRepository.findAllByTypeAndReceiverAndStatusAndSenderNotInOrderByIdDesc
                (MessageTypeEnum.USER_MSG.getCode(), receiver, MessageStatusEnum.VIEWED.getCode(), senderIds);
    }

    @Override
    public int countBySenderAndReceiverUnlookMsg(int sender, int receiver) {
        return messageRepository.countBySenderAndReceiverAndStatusOrderByCreateTimeDesc(sender, receiver, MessageStatusEnum.SEND.getCode());
    }

    @Override
    public int countBySenderAndReceiverViewedMsg(int sender, int receiver) {
        if (sender <= 0 || receiver <= 0) {
            return 0;
        }
        return messageRepository.countBySenderAndReceiverAndStatusOrderByCreateTimeDesc(sender, receiver, MessageStatusEnum.VIEWED.getCode());
    }

    @Override
    public List<Message> findUserChatLog(int userId, int selfId) {
        if (userId <= 0 || selfId <= 0) {
            return null;
        }
        List<Message> messages = messageRepository.findUserChatLog(MessageTypeEnum.USER_MSG.getCode(), userId, selfId);
        if (CollectionUtil.isNotEmpty(messages)) {
            for (Message message : messages) {
                UserInfo senderInfo = userInfoService.findById(message.getSender());
                UserInfo receiverInfo = userInfoService.findById(message.getReceiver());
                message.setSenderInfo(new SenderInfo(senderInfo));
                message.setReceiverInfo(new SenderInfo(receiverInfo));
            }
        }
        return messages;
    }

}
