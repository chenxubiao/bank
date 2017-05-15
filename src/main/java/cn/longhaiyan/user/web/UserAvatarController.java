package cn.longhaiyan.user.web;

import cn.longhaiyan.attachment.domain.Attachment;
import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-1.
 */
@RestController
public class UserAvatarController extends CommonController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AttachmentService attachmentService;
    @RequestMapping(value = "/user/avatar/update", method = RequestMethod.POST)
    public ResponseEntity updateUserAvatar(@RequestParam(value = "picId", defaultValue = "0") int picId,
                                           HttpServletRequest request) {

        if (picId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        Attachment attachment = attachmentService.findById(picId);
        if (attachment == null) {
            return ResponseEntity.failure(Errors.PICTURE_NOT_FOUND);
        }
        UserSession userSession = getUserSession(request);
        UserInfo userInfo = userInfoService.findById(userSession.getUserId());
        userInfo.setAvatarId(picId);
        userInfoService.save(userInfo);
        userSession = buildUserSession(userInfo);
        setUserSession(request, userSession);
        return ResponseEntity.success();
    }
}
