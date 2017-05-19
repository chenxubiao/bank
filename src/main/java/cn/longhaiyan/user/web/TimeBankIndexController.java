package cn.longhaiyan.user.web;


import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.user.bean.RegisterBean;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.TeacherService;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
public class TimeBankIndexController extends GuestBaseController {
    @Autowired
    private UserRegisterController registerController;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TeacherService teacherService;


    @RequestMapping(value = {"index", "/"}, method = RequestMethod.GET)
    public String getIndexPage(Map<String, Object> map) {

        map.put("hello", "timebank");
        return "/index";
    }

    @RequestMapping(value = "/sys/init")
    public String sys(HttpServletRequest request, Map<String, Object> map) {

        int count = userInfoService.countAll();
        UserInfo sys;
        if (count == 0) {

            Teacher teacher = new Teacher();
            teacher.setName("高育良");
            teacher.setDept("汉东大学政法系主任");
            teacher.setRemark("名师出高徒");
            teacher.setCreateTime(new Date());
            teacher.setModifyTime(teacher.getCreateTime());
            teacher.setTno(110);
            teacherService.save(teacher);

            RegisterBean registerBean = new RegisterBean();
            registerBean.setCode("abcde");
            registerBean.setEmail("bank@bank.cn");
            registerBean.setUserName("timebank");
            registerBean.setPassword("abcde");
            registerController.regester(request, registerBean);
            System.out.println("系统账户已创建，name = " + registerBean.getUserName() + " ,psswd = " + registerBean.getPassword());
            sys = userInfoService.findById(BankConsts.USER_IS_SYSTEM);
            sys.setUserType(UserTypeEnum.TEACHER.getCode());
            sys.setUserRole(BankConsts.CRM_ADMIN);
            sys.setTeacher(teacher);
            userInfoService.save(sys);

            System.out.println("已授权用户角色为teacher：" + teacher.getName());
        }
        sys = userInfoService.findById(BankConsts.USER_IS_SYSTEM);
        super.setUserSession(request, sys);

        map.put("hello", "timebank");
        return "/index";
    }
}