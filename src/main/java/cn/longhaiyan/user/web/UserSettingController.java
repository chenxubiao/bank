package cn.longhaiyan.user.web;

import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankMapping;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.bean.UserProfileBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenxb on 17-5-1.
 */
@RestController
public class UserSettingController extends CommonController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AttachmentService attachmentService;

    /**
     * 用户信息完善接口
     */
    @RequestMapping(value = "/user/profile/add", method = RequestMethod.POST)
    public ResponseEntity userProfile(HttpServletRequest request, UserProfileBean userProfile) {

        if (userProfile == null
                || !BankMapping.USER_SEX_MAPPING.keySet().contains(userProfile.getSex())) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = getUserSession(request);
        UserInfo userInfo = userInfoService.findById(userSession.getUserId());
        boolean isUserInfoModify = false;
        int avatarId = userProfile.getAvatarId();
        if (avatarId > 0) {
            boolean isAvatarIdExist = attachmentService.isPictureExist(avatarId);
            if (isAvatarIdExist) {
                userInfo.setAvatarId(avatarId);
                isUserInfoModify = true;
            }
        }
        if (userInfo.getSex() != userProfile.getSex()) {
            userInfo.setSex(userProfile.getSex());
            isUserInfoModify = true;
        }
        if (userProfile.getBirthday() != null) {
            userInfo.setBirthday(userProfile.getBirthday());
            isUserInfoModify = true;
        }
        if (isUserInfoModify) {
            userInfo.setModifyTime(new Date());
            userInfoService.save(userInfo);
        }
        return ResponseEntity.success();
    }

}
