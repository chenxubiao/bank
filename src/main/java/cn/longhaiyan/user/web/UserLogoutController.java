package cn.longhaiyan.user.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by chenxb on 17-5-1.
 */
@RestController
public class UserLogoutController extends CommonController {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @RequestMapping(value = "/user/logout/data", method = RequestMethod.POST)
    public ResponseEntity logout(HttpServletRequest request,
                                 HttpSession session) {
        UserSession userSession = getUserSession(request);
        userLoginLogService.logout(userSession.getUserId());
        session.setAttribute(BankConsts.USER_SESSION_KEY, null);
        return ResponseEntity.success();
    }
}
