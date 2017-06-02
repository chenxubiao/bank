package cn.longhaiyan.admin.web;

import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserStatusEnum;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxb on 17-6-1.
 */
@RestController
public class AdminUserInfoController extends CommonController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 搜索用户接口   精确搜索
     *
     * 支持：手机号、邮箱、用户名
     * @param request
     * @param name
     * @return
     */
    @RequestMapping(value = "/admin/pay/user/info/data")
    @Authority(privilege = BankConsts.UserRole.USER_IS_PAYER + "")
    public ResponseEntity getUserInfo(HttpServletRequest request, String name) {
        if (StringUtil.isBlank(name)) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        name = name.trim();
        UserInfo userInfo = null;
        if (StringUtil.isPhoneNumber(name)) {
            userInfo = userInfoService.findByCellphone(name);
            if (userInfo == null) {
                return ResponseEntity.failure(Errors.CELLPHONE_NULL_ERROR);
            }
        } else if (StringUtil.isEmail(name)) {
            userInfo = userInfoService.findByEmail(name);
            if (userInfo == null) {
                return ResponseEntity.failure(Errors.EMAIL_NOT_FOUNT);
            }
        } else {
            userInfo = userInfoService.findByUserName(name);
            if (userInfo == null) {
                return ResponseEntity.failure(Errors.ACCOUNT_NOT_FOUND);
            }
        }
        if (userInfo == null
                || userInfo.getStatus() != UserStatusEnum.NORMAL.getCode()) {
            return ResponseEntity.failure(Errors.USER_STATUS_ERROR);
        }
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }

    @RequestMapping(value = "/admin/user/info/list/data")
    @Authority(privilege = BankConsts.UserRole.USER_IS_PAYER + "")
    public ResponseEntity getUserList(HttpServletRequest request) {
        List<UserInfo> userInfoList = userInfoService.findAll();
        List<UserInfoBean> userInfoBeanList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getId() == 1) {
                continue;
            }
            UserInfoBean userInfoBean = new UserInfoBean(userInfo);
            userInfoBeanList.add(userInfoBean);
        }
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBeanList);
    }
}
