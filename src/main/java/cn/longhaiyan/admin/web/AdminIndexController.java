package cn.longhaiyan.admin.web;

import cn.longhaiyan.common.web.GuestBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by chenxb on 17-5-22.
 */
@Controller
public class AdminIndexController extends GuestBaseController {

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String gotoAdmin(HttpServletRequest request, Map<String, Object> map) {

        map.put("hello", "timebank");
        return "/admin";
    }
}
