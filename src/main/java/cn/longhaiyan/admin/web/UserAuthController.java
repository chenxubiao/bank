package cn.longhaiyan.admin.web;

import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.AuthAttachment;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserRole;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.AuthAttachmentService;
import cn.longhaiyan.user.service.UserInfoService;
import cn.longhaiyan.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-22.
 */
@RestController
public class UserAuthController extends CommonController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private AuthAttachmentService authAttachmentService;
    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "/admin/user/auth/list/data", method = RequestMethod.GET)
    @Authority(privilege = "" + BankConsts.UserRole.USER_IS_OPERATOR)
    public ResponseEntity getAuthingUser(HttpServletRequest request) {

        List<Integer> userTypeList = new ArrayList<>();
        userTypeList.add(UserTypeEnum.AUTHINT_STUDENT.getCode());
        userTypeList.add(UserTypeEnum.AUTHINT_TEACHER.getCode());
        List<UserInfo> userInfos = userInfoService.findByUserTypeIn(userTypeList);
        if (CollectionUtil.isEmpty(userInfos)) {
            return ResponseEntity.failure(Errors.USER_AUTH_NONE);
        }
        List<UserInfoBean> authBeanList = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            UserInfoBean userInfoBean = new UserInfoBean(userInfo);
            List<AuthAttachment> attachments = authAttachmentService.findByUserId(userInfo.getId());
            List<Integer> picIds = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(attachments)) {
                for (AuthAttachment authAttachment : attachments) {
                    if (authAttachment == null) {
                        continue;
                    }
                    picIds.add(authAttachment.getAttachmentId());
                }
            }
            userInfoBean.setPics(picIds);
            authBeanList.add(userInfoBean);
        }
        return ResponseEntity.success().set(BankConsts.DATA, authBeanList);
    }


    @Authority(privilege = "" + BankConsts.UserRole.USER_IS_OPERATOR)
    @RequestMapping(value = "/admin/user/auth/update/data", method = RequestMethod.POST)
    public ResponseEntity authUser(HttpServletRequest request, int userId, int auth) {

        if (userId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserInfo userInfo = userInfoService.findById(userId);
        if (userInfo == null) {
            return ResponseEntity.failure(Errors.USER_INFO_NOT_FOUND);
        }
        String msg;
        UserRole userRole = null;

        if (userInfo.getUserType() == UserTypeEnum.AUTHINT_TEACHER.getCode()) {
            msg = "教师";
            if (auth == 1) {
                userInfo.setUserType(UserTypeEnum.TEACHER.getCode());

                userRole = new UserRole();
                userRole.setUserInfo(userInfo);
                userRole.setRoleId(BankConsts.UserRole.USER_IS_TEACHER);
                userRole.setCreateTime(new Date());
                userRole.setModifyTime(userRole.getCreateTime());

            } else {
                userInfo.setUserType(UserTypeEnum.AUTH_FAILD.getCode());
            }
        } else if (userInfo.getUserType() == UserTypeEnum.AUTHINT_STUDENT.getCode()) {

            msg = "学生";
            if (auth == 1) {
                userInfo.setUserType(UserTypeEnum.STUDENT.getCode());

                userRole = new UserRole();
                userRole.setUserInfo(userInfo);
                userRole.setRoleId(BankConsts.UserRole.USER_IS_STUDENT);
                userRole.setCreateTime(new Date());
                userRole.setModifyTime(userRole.getCreateTime());

            } else {
                userInfo.setUserType(UserTypeEnum.AUTH_FAILD.getCode());
            }

        } else {
            return ResponseEntity.failure(Errors.USER_AUTH_NONE);
        }

        userInfo.setModifyTime(new Date());
        userInfoService.save(userInfo);
        if (userRole != null) {
            userRoleService.save(userRole);
        }

        Message message;
        if (auth == 1) {
            message = new Message(MessageTypeEnum.USER_AUTH_SUCCESS.getCode()
                    , BankConsts.USER_IS_SYSTEM, userInfo.getId()
                    , userInfo.getUserType(), msg);

        } else {
            message = new Message(MessageTypeEnum.USER_AUTH_FAILED.getCode()
                    , BankConsts.USER_IS_SYSTEM, userInfo.getId()
                    , userInfo.getUserType(), msg);
        }
        message.setModifyTime(new Date());
        messageService.save(message);

        // TODO: 17-5-22 认证后可奖励时间币
        return ResponseEntity.success();
    }
}
