package cn.longhaiyan.account.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by chenxb on 17-5-22.
 */
@RestController
public class AcccountController extends CommonController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;

    /**
     * 获取用户账户信息
     * @param request
     * @return
     */
    @Authority(privilege = BankConsts.UserRole.USER_IS_STUDENT + "," + BankConsts.UserRole.USER_IS_TEACHER)
    @RequestMapping(value = "/account/info/data", method = RequestMethod.GET)
    public ResponseEntity getAccountInfo(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        int userId = userSession.getUserId();
        Account account = accountService.findByUserId(userId);
        int balance = account.getTotalMoney();
        List<AccountLog> accountLogs = accountLogService.findByAccount(account);
        return ResponseEntity.success().set(BankConsts.DATA, accountLogs).set("balance", balance);
    }
}
