package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.CommonBean;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class MessageUserSendController extends CommonController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserInfoService userInfoService;


    /**
     * 用户发送消息接口
     * @param request
     * @param msg
     * @return
     */
    @RequestMapping(value = "/message/user/send/data", method = RequestMethod.POST)
    public ResponseEntity findUnLookMsg(HttpServletRequest request, CommonBean msg) {
        if (msg.getId() <= 0 || StringUtil.isBlank(msg.getName())) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        String messageString = msg.getName().trim();

        int receiverId = msg.getId();
        UserInfo receiver = userInfoService.findById(receiverId);
        if (receiver == null) {
            return ResponseEntity.failure(Errors.USER_INFO_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        int senderId = userSession.getUserId();

        //不允许用户自己给自己发消息
        if (senderId == receiverId) {
            return ResponseEntity.failure(Errors.MESSAGE_SEND_MYSELF);
        }

        Message message = new Message
                (MessageTypeEnum.USER_MSG.getCode(), senderId, receiverId, 0, messageString);
        message.setCreateTime(new Date());
        message.setModifyTime(message.getCreateTime());
        messageService.save(message);
        return ResponseEntity.success().set(BankConsts.DATA, message);
    }
}
