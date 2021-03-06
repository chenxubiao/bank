package cn.longhaiyan.message.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class MessageUpdateControll extends CommonController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = "/message/unlook/update", method = RequestMethod.POST)
    public ResponseEntity disposeViewMessage(HttpServletRequest request,
                                             @RequestParam(value = "userId", defaultValue = "0") int userId) {

        if (userId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserInfo sender = userInfoService.findById(userId);
        if (sender == null) {
            return ResponseEntity.failure(Errors.USER_INFO_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        int receiveId = userSession.getUserId();
        messageService.updateUnlookMsg(sender.getId(), receiveId);
        return ResponseEntity.success();
    }


}
