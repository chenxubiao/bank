package cn.longhaiyan.user.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.HashUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.bean.LoginBean;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.AuthAttachment;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserLoginLog;
import cn.longhaiyan.user.enums.UserTypeEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-3-31.
 */
@RestController
public class UserLoginController extends GuestBaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthAttachmentService authAttachmentService;

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
        System.out.println("login name = " + loginBean.getName());
        System.out.println("login code = " + loginBean.getCode());
        System.out.println("login passwd = " + loginBean.getPassword());
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
        } else {
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


        int loginTime = 1;
        UserLoginLog todayLog = userLoginLogService.findTodayLoginLog(userInfo.getId());
        if (todayLog != null) {
            userLoginLog.setLoginTime(todayLog.getLoginTime());
        } else {
            UserLoginLog yesterdayLoginLog = userLoginLogService.findYesterdayLoginLog(userInfo.getId());
            if (yesterdayLoginLog == null) {
                userLoginLog.setLoginTime(loginTime);
            } else {
                loginTime = yesterdayLoginLog.getLoginTime() + loginTime;

                int money = loginTime * 5;
                Account account = accountService.findByUserId(userInfo.getId());
                account.setTotalMoney(account.getTotalMoney() + money);
                account.setModifyTime(new Date());
                accountService.save(account);

                AccountLog accountLog = new AccountLog();
                accountLog.setAccount(account);
                accountLog.setProjectId(loginTime);
                accountLog.setType(AccountLogTypeEnum.ADD_LOGIN.getCode());
                accountLog.setMoney(money);
                accountLog.setBalance(account.getTotalMoney());
                accountLog.setUserId(userInfo.getId());
                accountLog.setCreateTime(new Date());
                accountLog.setModifyTime(accountLog.getCreateTime());
                accountLogService.save(accountLog);

                Message message = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                        , BankConsts.USER_IS_SYSTEM, userInfo.getId(), accountLog.getId(), accountLog.getMessage());

                message.setModifyTime(message.getCreateTime());
                messageService.save(message);

                userLoginLog.setLoginTime(loginTime);
            }
        }

        userLoginLog.setUserId(userInfo.getId());
        userLoginLog.setCreateTime(new Date());
        userLoginLog.setModifyTime(userLoginLog.getCreateTime());
        userLoginLogService.save(userLoginLog);
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        List<Integer> pics = new ArrayList<>();
        if (userInfo.getUserType() != UserTypeEnum.UNKNOWN.getCode()) {
            List<AuthAttachment> authAttachments = authAttachmentService.findByUserId(userInfo.getId());
            if (CollectionUtil.isNotEmpty(authAttachments)) {
                for (AuthAttachment authAttachment : authAttachments) {
                    pics.add(authAttachment.getAttachmentId());
                }
            }
            userInfoBean.setPics(pics);
        }
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }
}
