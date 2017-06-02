package cn.longhaiyan.admin.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.domain.AccountPay;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountPayService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.admin.enums.PayTypeEnum;
import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.user.bean.UserInfoBean;
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
import java.util.List;

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

    /**
     * 账户充值接口
     *
     * @param request
     * @param userId
     * @param money
     * @param remark
     * @return
     */
    @RequestMapping(value = "/admin/account/pay/user/data", method = RequestMethod.POST)
    @Authority(privilege = "" + BankConsts.UserRole.USER_IS_PAYER)
    public ResponseEntity pay(HttpServletRequest request,
                              @RequestParam(value = "userId", defaultValue = "0") int userId,
                              @RequestParam(value = "money", defaultValue = "0") int money,
                              @RequestParam(value = "remark", defaultValue = "") String remark,
                              @RequestParam(value = "type", defaultValue = "0") int type) {

        if (userId <= 0 || money <= 0 || PayTypeEnum.isNotContain(type)) {
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
        if (account.getTotalMoney() < money && type == PayTypeEnum.DELETE.getCode()) {
            return ResponseEntity.failure(Errors.ACCOUNT_BALANCE);
        }
        if (type == PayTypeEnum.ADD.getCode()) {
            AccountPay accountPay = new AccountPay(account, userSession.getUserId(), money, PayTypeEnum.ADD.getCode());
            accountPay.setRemark(remark);
            accountPay.setModifyTime(accountPay.getCreateTime());
            accountPayService.save(accountPay);

            account.setTotalMoney(account.getTotalMoney() + money);
            account.setModifyTime(new Date());
            accountService.save(account);

            AccountLog accountLog = new AccountLog(userId,
                    AccountLogTypeEnum.ADD_ACCOUNT_PAY.getCode(), money, accountPay.getId(), remark, account);
            accountLog.setBalance(account.getTotalMoney());
            accountLog.setModifyTime(new Date());
            accountLogService.save(accountLog);

            Message msgToUser = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                    , BankConsts.USER_IS_SYSTEM, userId, accountLog.getId(), accountLog.getMessage());
            msgToUser.setModifyTime(msgToUser.getCreateTime());
            messageService.save(msgToUser);

            Message msgToPayer = new Message(MessageTypeEnum.ADMIN_PAY_ACCOUNT.getCode()
                    , BankConsts.USER_IS_SYSTEM, userSession.getUserId(), accountLog.getId(), "您已为用户「" + userInfo.getUserName() + "」成功充值时间币" + money + "枚，备注：" + accountLog.getMessage());
            msgToPayer.setModifyTime(msgToPayer.getCreateTime());
            messageService.save(msgToPayer);
            return ResponseEntity.success();
        } else {

            AccountPay accountDel = new AccountPay(account, userSession.getUserId(), money, PayTypeEnum.DELETE.getCode());
            accountDel.setRemark(remark);
            accountDel.setModifyTime(accountDel.getCreateTime());
            accountPayService.save(accountDel);

            account.setTotalMoney(account.getTotalMoney() - money);
            account.setModifyTime(new Date());
            accountService.save(account);

            AccountLog accountLog = new AccountLog(userId,
                    AccountLogTypeEnum.DEL_ACCOUNT_PAY.getCode(), money, accountDel.getId(), remark, account);
            accountLog.setBalance(account.getTotalMoney());
            accountLog.setModifyTime(new Date());
            accountLogService.save(accountLog);

            Message msgToUser = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                    , BankConsts.USER_IS_SYSTEM, userId, accountLog.getId(), accountLog.getMessage());
            msgToUser.setModifyTime(msgToUser.getCreateTime());
            messageService.save(msgToUser);

            Message msgToPayer = new Message(MessageTypeEnum.ADMIN_PAY_ACCOUNT_DEL.getCode()
                    , BankConsts.USER_IS_SYSTEM, userSession.getUserId(), accountLog.getId(), "您已成功扣除用户「" + userInfo.getUserName() + "」时间币" + money + "枚。备注：" + accountLog.getMessage());
            msgToPayer.setModifyTime(msgToPayer.getCreateTime());
            messageService.save(msgToPayer);
            return ResponseEntity.success();
        }
    }


    @RequestMapping(value = "/admin/account/pay/user/list")
    @Authority(privilege = "" + BankConsts.UserRole.USER_IS_PAYER)
    public ResponseEntity getAdminAccountLog(HttpServletRequest request) {

        List<AccountPay> accountPayList = accountPayService.findAll();
        if (CollectionUtil.isNotEmpty(accountPayList)) {
            for (AccountPay accountPay : accountPayList) {
                Account account = accountPay.getAccount();
                UserInfo userInfo = userInfoService.findById(account.getUserId());
                UserInfoBean userInfoBean = new UserInfoBean(userInfo);
                UserInfo payer = userInfoService.findById(accountPay.getPayer());
                UserInfoBean payerInfo = new UserInfoBean(payer);
                accountPay.setPayerInfo(payerInfo);
                accountPay.setUserInfo(userInfoBean);
            }
        }
        return ResponseEntity.success().set(BankConsts.DATA, accountPayList);
    }
}
