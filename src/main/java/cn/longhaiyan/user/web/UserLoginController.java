package cn.longhaiyan.user.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.HashUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.user.bean.LoginBean;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserLoginLog;
import cn.longhaiyan.user.service.*;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by chenxb on 17-3-31.
 */
@RestController
public class UserLoginController extends GuestBaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserLoginLogService userLoginLogService;

    /**
     * 用户登录接口
     */
    @RequestMapping(value = "user/login/data", method = RequestMethod.POST)
    public ResponseEntity login(HttpSession session, HttpServletResponse response,
                                HttpServletRequest request,
                                @Valid LoginBean loginBean, BindingResult result) {

        if (StringUtil.isBlank(loginBean.getName())
                || StringUtil.isBlank(loginBean.getPassword())
                || StringUtil.isBlank(loginBean.getCode())) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        String name = loginBean.getName().trim();
        String passowrd = loginBean.getPassword().trim();
        String loginCode = loginBean.getCode().trim();

        System.out.println("----------------------------------------------------------------");
        System.out.println("login name = "+loginBean.getName());
        System.out.println("login code = "+loginBean.getCode());
        System.out.println("login passwd = "+loginBean.getPassword());
        System.out.println("----------------------------------------------------------------");
        if (result.hasErrors()) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println("login session：" + String.valueOf(code) + " id = " + session.getId());
        code = StringUtil.isEmpty(code) ? "" : code;
        System.out.println("code = " + code + " loginBean.getCode() = " + loginBean.getCode());
        if (!code.equals(loginCode)) {
            return ResponseEntity.failure(Errors.KAPTCHA_ERROR);
        }
        passowrd = HashUtil.encrypt(passowrd);
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
        }else {
            userInfo = userInfoService.findByUserName(name);
            if (userInfo == null) {
                return ResponseEntity.failure(Errors.ACCOUNT_NOT_FOUND);
            }
        }
        if (!passowrd.equals(userInfo.getPassword())) {
            return ResponseEntity.failure(Errors.PASSWORD_ERROR);
        }
        if (userInfo.getStatus() == BankConsts.UserStatus.USER_IS_LOCKING ||
                userInfo.getStatus() == BankConsts.UserStatus.USER_IS_CLOSE) {
            return ResponseEntity.failure(Errors.USER_IS_LOCKING);
        }
        UserSession userSession = super.buildUserSession(userInfo);
        super.setUserSession(request, userSession);
        UserLoginLog userLoginLog = new UserLoginLog();
        String ip = request.getHeader("X-Real-IP") == null ? "" : request.getHeader("X-Real-IP");
        userLoginLog.setIp(ip);
        userLoginLog.setUserId(userInfo.getId());
        userLoginLog.setCreateTime(new Date());
        userLoginLog.setModifyTime(userLoginLog.getCreateTime());
        userLoginLogService.save(userLoginLog);
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }
}
