package cn.longhaiyan.user.web;


import cn.longhaiyan.common.web.GuestBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class TimeBankIndexController extends GuestBaseController {

    @RequestMapping(value = {"index", "/"}, method = RequestMethod.GET)
    public String getIndexPage(Map<String, Object> map) {
        map.put("hello", "timebank");
        return "/index";
    }
}
