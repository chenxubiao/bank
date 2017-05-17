package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by chenxb on 17-5-11.
 */
@RestController
public class MessageUserChatController extends CommonController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/message/user/chat/data", method = RequestMethod.GET)
    public ResponseEntity findUserChatMessage(HttpServletRequest request,
                                              @RequestParam(value = "userId", defaultValue = "0") int userId) {

        if (userId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserInfo userInfo = userInfoService.findById(userId);
        if (userInfo == null) {
            return ResponseEntity.failure(Errors.USER_INFO_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        int selfId = userSession.getUserId();
        if (userId == selfId) {
            return ResponseEntity.failure(Errors.MESSAGE_NONE_RECEIVE);
        }
        List<Message> chatLog = messageService.findUserChatLog(userId, selfId);
        return ResponseEntity.success().set(BankConsts.DATA, chatLog).set("selfId", selfId);
    }

}
