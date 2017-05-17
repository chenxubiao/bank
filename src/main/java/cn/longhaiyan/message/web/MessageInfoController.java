package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.bean.MsgInfo;
import cn.longhaiyan.message.bean.SenderInfo;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class MessageInfoController extends CommonController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserInfoService userInfoService;


    /**
     * 系统消息列表、用户消息用户信息及数量列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/message/info/data", method = RequestMethod.GET)
    public ResponseEntity getSenderInfo(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        int receiver = userSession.getUserId();
        List<Message> sysUnlookMessageList = messageService.findUnLookSysMsg(receiver);
        List<Message> sysViewedMessageList = messageService.finViewedSysMsg(receiver);
        List<Message> userUnlookMessageList = messageService.findUnLookUserMsg(receiver);
        UserInfo sys = userInfoService.findById(BankConsts.USER_IS_SYSTEM);
        MsgInfo sysUnLook;
        if (CollectionUtil.isEmpty(sysUnlookMessageList)) {
            sysUnLook = new MsgInfo(new SenderInfo(sys), BankConsts.ZERO, null);
        } else {
            sysUnLook = new MsgInfo(new SenderInfo(sys), sysUnlookMessageList.size(), sysUnlookMessageList);
        }
        MsgInfo sysViewed;
        if (CollectionUtil.isEmpty(sysViewedMessageList)) {
            sysViewed = new MsgInfo(new SenderInfo(sys), BankConsts.ZERO, null);
        } else {
            sysViewed = new MsgInfo(new SenderInfo(sys), sysViewedMessageList.size(), sysViewedMessageList);
        }

        int userMsgUnlookCount = 0;
        List<MsgInfo> userUnlookMsgInfoList;
        Set<Integer> userUnlookIdSet;
        if (CollectionUtil.isEmpty(userUnlookMessageList)) {
            userUnlookMsgInfoList = null;
            userUnlookIdSet = null;
        } else {
            userMsgUnlookCount = userUnlookMessageList.size();
            userUnlookMsgInfoList = new ArrayList<>();
            userUnlookIdSet = new LinkedHashSet<>();
            for (Message message : userUnlookMessageList) {
                userUnlookIdSet.add(message.getSender());
            }
        }
        List<Message> userViewedMessageList = null;
        if (CollectionUtil.isNotEmpty(userUnlookIdSet)) {

            for (int id : userUnlookIdSet) {
                int count = messageService.countBySenderAndReceiverUnlookMsg(id, receiver);
                UserInfo sender = userInfoService.findById(id);
                MsgInfo msgInfo = new MsgInfo(new SenderInfo(sender), count, null);
                userUnlookMsgInfoList.add(msgInfo);
            }
            userViewedMessageList = messageService.findViewedUserMsgNotInUserIds(receiver, new ArrayList<>(userUnlookIdSet));
        }else {
            userViewedMessageList = messageService.findViewedUserMsg(receiver);
        }
        List<MsgInfo> userViewedMsgInfoList;
        Set<Integer> userViewedIdSet;
        if (CollectionUtil.isEmpty(userViewedMessageList)) {
            userViewedIdSet = null;
            userViewedMsgInfoList = null;
        } else {
            userViewedIdSet = new LinkedHashSet<>();
            userViewedMsgInfoList = new ArrayList<>();
            for (Message message : userViewedMessageList) {
                userViewedIdSet.add(message.getSender());
            }
        }
        if (CollectionUtil.isNotEmpty(userViewedIdSet)) {
            for (int id : userViewedIdSet) {
                //不需要统计数量
//                int count = messageService.countBySenderAndReceiverViewedMsg(id, receiver);
                UserInfo sender = userInfoService.findById(id);
                MsgInfo msgInfo = new MsgInfo(new SenderInfo(sender), BankConsts.ZERO, null);
                userViewedMsgInfoList.add(msgInfo);
            }
        } else {

        }

        return ResponseEntity.success()
                .set("userMsgUnlookCount", userMsgUnlookCount)
                .set("sysMsgUnlook", sysUnLook)
                .set("sysMsgViewed", sysViewed)
                .set("userMsgUnlook", userUnlookMsgInfoList)
                .set("userMsgViewed", userViewedMsgInfoList);
    }


}
