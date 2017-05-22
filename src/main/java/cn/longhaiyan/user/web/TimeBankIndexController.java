package cn.longhaiyan.user.web;

import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.user.bean.RegisterBean;
import cn.longhaiyan.user.domain.Student;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserRole;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.StudentService;
import cn.longhaiyan.user.service.TeacherService;
import cn.longhaiyan.user.service.UserInfoService;
import cn.longhaiyan.user.service.UserRoleService;
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
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private StudentService studentService;


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
            registerBean.setPassword("timebank");
            registerController.regester(request, registerBean);
            System.out.println("系统账户已创建，name = " + registerBean.getUserName() + " ,psswd = " + registerBean.getPassword());
            sys = userInfoService.findByEmail("bank@bank.cn");
            sys.setUserType(UserTypeEnum.TEACHER.getCode());
            sys.setUserRole(BankConsts.CRM_ADMIN);
            sys.setTeacher(teacher);
            userInfoService.save(sys);

            UserRole userRole = new UserRole();
            userRole.setRoleId(BankConsts.UserRole.USER_IS_TEACHER);
            userRole.setUserInfo(sys);
            userRole.setCreateTime(new Date());
            userRole.setModifyTime(userRole.getCreateTime());
            userRoleService.save(userRole);

            System.out.println("已授权用户角色为teacher：" + teacher.getName());
        }
        sys = userInfoService.findByEmail("bank@bank.cn");
        super.setUserSession(request, sys);

        map.put("hello", "timebank");
        return "/index";
    }

    @RequestMapping(value = "/sys/admin/operate")
    public String addOperator(HttpServletRequest request, Map<String, Object> map) {

        int count = userRoleService.countByRoleId(BankConsts.UserRole.USER_IS_OPERATOR);
        String userName = "赵东来";
        String email = "zdl@bank.cn";
        String psd = "zdl_password";
        UserInfo sys;
        if (count == 0) {

            RegisterBean registerBean = new RegisterBean();
            registerBean.setCode("abcde");
            registerBean.setEmail(email);
            registerBean.setUserName(userName);
            registerBean.setPassword(psd);
            registerController.regester(request, registerBean);
            System.out.println("系统账户已创建，name = " + registerBean.getUserName() + " ,psswd = " + registerBean.getPassword());
            sys = userInfoService.findByEmail(email);
            UserRole userRole = new UserRole();
            userRole.setRoleId(BankConsts.UserRole.USER_IS_OPERATOR);
            userRole.setUserInfo(sys);
            userRole.setCreateTime(new Date());
            userRole.setModifyTime(userRole.getCreateTime());
            userRoleService.save(userRole);

            System.out.println("已授权用户角色为operate,userName : " + sys.getUserName());
        }
        sys = userInfoService.findByEmail(email);
        super.setUserSession(request, sys);

        map.put("hello", "timebank");
        return "/index";
    }


    @RequestMapping(value = "/sys/admin/noauth")
    public String addUser(HttpServletRequest request, Map<String, Object> map) {

        String userName = "陈岩石";
        String email = "cys@bank.cn";
        String psd = "cys_password";
        UserInfo sys = userInfoService.findByEmail(email);
        if (sys == null) {

            RegisterBean registerBean = new RegisterBean();
            registerBean.setCode("abcde");
            registerBean.setEmail(email);
            registerBean.setUserName(userName);
            registerBean.setPassword(psd);
            registerController.regester(request, registerBean);
            System.out.println("系统账户已创建，name = " + registerBean.getUserName() + " ,psswd = " + registerBean.getPassword());
        }
        sys = userInfoService.findByEmail(email);
        super.setUserSession(request, sys);

        map.put("hello", "timebank");
        return "/index";
    }


    @RequestMapping(value = "/sys/admin/student")
    public String addStu(HttpServletRequest request, Map<String, Object> map) {

        int count = userInfoService.countAll();
        UserInfo sys;
        String email = "hlp@bank.cn";
        if (count == 0) {

            Student s = new Student();
            s.setName("侯亮平");
            s.setDept("反贪局局长");
            s.setRemark("666");
            s.setMajor("政法系");
            s.setCreateTime(new Date());
            s.setModifyTime(s.getCreateTime());
            s.setSno(110);
            studentService.save(s);

            RegisterBean registerBean = new RegisterBean();
            registerBean.setCode("abcde");
            registerBean.setEmail(email);
            registerBean.setUserName("猴子");
            registerBean.setPassword("hlp_password");
            registerController.regester(request, registerBean);
            System.out.println("系统账户已创建，name = " + registerBean.getUserName() + " ,psswd = " + registerBean.getPassword());
            sys = userInfoService.findByEmail(email);
            sys.setUserType(UserTypeEnum.STUDENT.getCode());
            sys.setUserRole(BankConsts.CRM_ADMIN);
            sys.setStudent(s);
            userInfoService.save(sys);

            UserRole userRole = new UserRole();
            userRole.setRoleId(BankConsts.UserRole.USER_IS_STUDENT);
            userRole.setUserInfo(sys);
            userRole.setCreateTime(new Date());
            userRole.setModifyTime(userRole.getCreateTime());
            userRoleService.save(userRole);

            System.out.println("已授权用户角色为student：" + s.getName());
        }
        sys = userInfoService.findByEmail(email);
        super.setUserSession(request, sys);

        map.put("hello", "timebank");
        return "/index";
    }
}