package cn.longhaiyan.admin.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.domain.AccountPay;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountPayService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserStatusEnum;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenxb on 17-6-1.
 */
@RestController
public class AdminAccountPayController extends CommonController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private AccountPayService accountPayService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/admin/account/pay/user/data", method = RequestMethod.POST)
    @Authority(privilege = "" + BankConsts.UserRole.USER_IS_PAYER)
    public ResponseEntity pay(HttpServletRequest request,
                              @RequestParam(value = "userId", defaultValue = "0") int userId,
                              @RequestParam(value = "money", defaultValue = "0") int money) {

        if (userId <= 0 || money <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
        if (userSession.getUserId() == userId
                || userId == BankConsts.USER_IS_SYSTEM) {

            return ResponseEntity.failure(Errors.ACCOUNT_PAY_SELF_ERROR);
        }
        UserInfo userInfo = userInfoService.findById(userId);
        if (userInfo == null
                || userInfo.getStatus() != UserStatusEnum.NORMAL.getCode()) {
            return ResponseEntity.failure(Errors.USER_STATUS_ERROR);
        }
        Account account = accountService.findByUserId(userId);
        if (account == null) {
            return ResponseEntity.failure(Errors.ACCOUNT_NOT_FOUND);
        }
        AccountPay accountPay = new AccountPay(account, userSession.getUserId(), money);
        accountPay.setModifyTime(accountPay.getCreateTime());
        accountPayService.save(accountPay);

        account.setTotalMoney(account.getTotalMoney() + money);
        account.setModifyTime(new Date());
        accountService.save(account);

        AccountLog accountLog = new AccountLog(userId,
                AccountLogTypeEnum.ADD_ACCOUNT_PAY.getCode(), money, accountPay.getId(), "", account);
        accountLog.setBalance(account.getTotalMoney());
        accountLog.setModifyTime(new Date());
        accountLogService.save(accountLog);


        Message msgToUser = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                , BankConsts.USER_IS_SYSTEM, userId, accountLog.getId(), accountLog.getMessage());
        msgToUser.setModifyTime(msgToUser.getCreateTime());
        messageService.save(msgToUser);


        Message msgToPayer = new Message(MessageTypeEnum.ADMIN_PAY_ACCOUNT.getCode()
                , BankConsts.USER_IS_SYSTEM, userSession.getUserId(), accountLog.getId(), accountLog.getMessage());
        msgToPayer.setModifyTime(msgToPayer.getCreateTime());
        messageService.save(msgToPayer);
        return ResponseEntity.success();
    }
}
