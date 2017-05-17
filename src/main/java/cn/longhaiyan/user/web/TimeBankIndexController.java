package cn.longhaiyan.user.web;


import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.user.bean.RegisterBean;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class TimeBankIndexController extends GuestBaseController {
    @Autowired
    private UserRegisterController registerController;
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = {"index", "/"}, method = RequestMethod.GET)
    public String getIndexPage(HttpServletRequest request, Map<String, Object> map) {

        // TODO: 17-5-16
        int count = userInfoService.countAll();
        if (count == 0) {
            RegisterBean registerBean = new RegisterBean();
            registerBean.setCode("abcde");
            registerBean.setEmail("bank@bank.cn");
            registerBean.setUserName("timebank");
            registerBean.setPassword("abcde");
            registerController.regester(request, registerBean);
        }
        UserInfo userInfo = userInfoService.findById(1);
        super.setUserSession(request, userInfo);

        map.put("hello", "timebank");
        return "/index";
    }
}
