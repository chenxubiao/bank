package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.bean.MessageBean;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
@RestController
public class MessageController extends CommonController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/message/unlook/data", method = RequestMethod.GET)
    public ResponseEntity findUnLookMsg(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        List<Message> messageList = messageService.findUnLookMsg(userSession.getUserId());
        MessageBean messageBean = null;
        if (CollectionUtil.isNotEmpty(messageList)) {
            messageBean = new MessageBean();
            messageBean.setMsgCount(messageList.size());
            messageBean.setMessages(messageList);
        }
        return ResponseEntity.success().set(BankConsts.DATA, messageBean);
    }

    @RequestMapping(value = "/message/unlook/update", method = RequestMethod.POST)
    public ResponseEntity disposeViewMessage(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        int count = messageService.countUnLookMsg(userSession.getUserId());
        if (count <= 0) {
            return ResponseEntity.failure(Errors.MESSAGE_NONE_UNLOOK);
        }
        messageService.updateUnLookMsg(userSession.getUserId());
        return ResponseEntity.success();
    }
}
