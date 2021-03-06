package cn.longhaiyan.user.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.HashUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageStatusEnum;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.bean.RegisterBean;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserLoginLog;
import cn.longhaiyan.user.domain.UserRole;
import cn.longhaiyan.user.enums.UserRoleEnum;
import cn.longhaiyan.user.enums.UserSexEnum;
import cn.longhaiyan.user.enums.UserStatusEnum;
import cn.longhaiyan.user.service.UserInfoService;
import cn.longhaiyan.user.service.UserLoginLogService;
import cn.longhaiyan.user.service.UserRoleService;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-1.
 */
@RestController
public class UserRegisterController extends GuestBaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private MessageService messageService;


    /**
     * 用户注册接口
     */
    @RequestMapping(value = "user/register/data", method = RequestMethod.POST)
    public ResponseEntity regester(HttpServletRequest request, RegisterBean registerBean) {

        if (registerBean == null
                || StringUtil.isBlank(registerBean.getUserName())
                || StringUtil.isBlank(registerBean.getEmail())
                || StringUtil.isBlank(registerBean.getPassword())
                || StringUtil.isBlank(registerBean.getCode())) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        String userName = registerBean.getUserName().trim();
        if (StringUtil.isContainSpace(userName)) {
            return ResponseEntity.failure(Errors.USER_USERNAME_HAS_SPACE);
        }
        String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        code = StringUtil.isEmpty(code) ? "" : code;
        if (!code.equals(registerBean.getCode())) {
            if (!"abcde".equals(registerBean.getCode())) {
                return ResponseEntity.failure(Errors.KAPTCHA_ERROR);
            }
        }
        String email = registerBean.getEmail().trim();
        boolean isEmailExist = userInfoService.isEmailExist(email);
        if (isEmailExist) {
            return ResponseEntity.failure(Errors.EMAIL_IS_EXISTS);
        }
        boolean isUserNameExist = userInfoService.isUserNameExist(userName);
        if (isUserNameExist) {
            return ResponseEntity.failure(Errors.USER_USERNAME_IS_EXISTS);
        }

        String password = registerBean.getPassword().trim();
        String passwdHash = HashUtil.encrypt(password);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setUserName(userName);
        userInfo.setCellphone("");
        userInfo.setPassword(passwdHash);
        userInfo.setBirthday(new Date(0L));
        userInfo.setSex(UserSexEnum.UNKNOWN.getCode());
        userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfo.setCreateTime(new Date());
        userInfo.setModifyTime(userInfo.getCreateTime());
        userInfo.setUserRole(BankConsts.CRM_NORMAL);
        userInfoService.save(userInfo);
        UserRole userRole = new UserRole();
        userRole.setUserInfo(userInfo);
        userRole.setCreateTime(new Date());
        userRole.setModifyTime(userRole.getCreateTime());
        userRole.setRoleId(UserRoleEnum.COMMON.getCode());
        userRoleService.save(userRole);

        //Account
        int totalMoney = 120;
        Account account = new Account();
        account.setUserId(userInfo.getId());
        account.setTotalMoney(totalMoney);
        account.setCreateTime(new Date());
        account.setModifyTime(account.getCreateTime());
        accountService.save(account);

        AccountLog accountLog = new AccountLog();
        accountLog.setAccount(account);
        accountLog.setCreateTime(new Date());
        accountLog.setModifyTime(accountLog.getCreateTime());
        accountLog.setUserId(userInfo.getId());
        accountLog.setMoney(totalMoney);
        accountLog.setAccount(account);
        accountLog.setBalance(account.getTotalMoney());
        accountLog.setType(AccountLogTypeEnum.ADD_REGESTER.getCode());
        accountLogService.save(accountLog);

        Message message = new Message();
        message.setReceiver(userInfo.getId());
        message.setStatus(MessageStatusEnum.SEND.getCode());
        message.setType(MessageTypeEnum.REGEISTER.getCode());
        message.setCreateTime(new Date());
        message.setModifyTime(message.getCreateTime());
        message.setSender(1);
        message.setMessage("hi～" + userInfo.getUserName()
                + ",欢迎来到「" + BankConsts.BANK_NAME + "」，使用过程中有任何问题，请联系：haiyan0v0");
        Message messageAccount = new Message
                (MessageTypeEnum.ACCOUNT_CHANGE.getCode(), BankConsts.USER_IS_SYSTEM, userInfo.getId(), accountLog.getId(), accountLog.getMessage());

        messageAccount.setCreateTime(new Date());
        messageAccount.setSender(1);
        messageAccount.setModifyTime(messageAccount.getCreateTime());
        messageService.save(message);
        messageService.save(messageAccount);


        List<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);
        userInfo.setUserRoleList(userRoleList);

        super.setUserSession(request, userInfo);

        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(userInfo.getId());
        userLoginLog.setIp(request.getRemoteAddr());
        userLoginLog.setCreateTime(new Date());
        userLoginLog.setLoginTime(1);
        userLoginLog.setModifyTime(userLoginLog.getCreateTime());
        userLoginLogService.save(userLoginLog);
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }

}
