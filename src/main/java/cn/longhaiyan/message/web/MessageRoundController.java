package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class MessageRoundController extends CommonController {
    @Autowired
    private MessageService messageService;


    /**
     * 消息轮循接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/message/unlook/count", method = RequestMethod.GET)
    public ResponseEntity findUnLookMsgCount(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        int count = messageService.countUnLookMsg(userSession.getUserId());
        return ResponseEntity.success().set(BankConsts.DATA, count);
    }


}
