package cn.longhaiyan.user.web;

import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.BankMapping;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.bean.UserProfileBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserSexEnum;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping(value = "/user/setting/update", method = RequestMethod.POST)
    public ResponseEntity userProfile(HttpServletRequest request, UserProfileBean userProfile) {

        if (userProfile == null
                || UserSexEnum.isNotContain(userProfile.getSex())) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
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
//        if (userProfile.getBirthday() != null) {
//            userInfo.setBirthday(userProfile.getBirthday());
//            isUserInfoModify = true;
//        }
        if (StringUtil.isNotBlank(userProfile.getCellphone()) && StringUtil.isPhoneNumber(userProfile.getCellphone().trim())) {
            if (!userInfo.getCellphone().equals(userProfile.getCellphone().trim())) {
                userInfo.setCellphone(userProfile.getCellphone().trim());
                isUserInfoModify = true;
            }
        }
        if (isUserInfoModify) {
            userInfo.setModifyTime(new Date());
            userInfoService.save(userInfo);

            super.setUserSession(request, userInfo);
        }
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }

    @RequestMapping(value = "/user/info/data", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(HttpServletRequest request,
                                      @RequestParam(value = "userId", defaultValue = "0") int userId) {
        if (userId <= 0) {

            return ResponseEntity.failure(Errors.USER_INFO_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        UserInfoBean userInfoBean = new UserInfoBean(userSession);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }

}
