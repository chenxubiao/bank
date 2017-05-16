package cn.longhaiyan.user.web;

import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.NumberUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.BankMapping;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.bean.AuthBean;
import cn.longhaiyan.user.bean.UserInfoBean;
import cn.longhaiyan.user.domain.AuthAttachment;
import cn.longhaiyan.user.domain.Student;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.AuthAttachmentService;
import cn.longhaiyan.user.service.StudentService;
import cn.longhaiyan.user.service.TeacherService;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by chenxb on 17-5-16.
 */
@RestController
public class UserAuthControll extends CommonController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AuthAttachmentService authAttachmentService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "user/auth/data", method = RequestMethod.POST)
    public ResponseEntity authUser(HttpServletRequest request, AuthBean authBean) {

        if (authBean == null
                || StringUtil.isBlank(authBean.getAttachment())
                || StringUtil.isBlank(authBean.getName())
                || StringUtil.isBlank(authBean.getDept())
                || UserTypeEnum.isNotContain(authBean.getType())
                || authBean.getNo() <= 0) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        int authType = authBean.getType();
        UserSession userSession = super.getUserSession(request);
        if (BankMapping.USER_AUTHED_TYPE_MAPPINT.keySet()
                .contains(userSession.getUserType())) {

            return ResponseEntity.failure("当前不允许该操作，账户状态："
                    + UserTypeEnum.getValue(userSession.getUserType()));
        }


        Set<Integer> attachmentIds = NumberUtil.parseToIntSet(authBean.getAttachment());
        if (CollectionUtil.isEmpty(attachmentIds)) {
            return ResponseEntity.failure(Errors.PICTURE_NOT_FOUND);
        }
        List<AuthAttachment> authAttachments = new ArrayList<>();
        for (int id : attachmentIds) {
            boolean isExist = attachmentService.isPictureExist(id);
            if (!isExist) {
                return ResponseEntity.failure(Errors.PICTURE_NOT_FOUND);
            }
            AuthAttachment authAttachment = new AuthAttachment();
            authAttachment.setAttachmentId(id);
            authAttachment.setUserId(userSession.getUserId());
            authAttachment.setCreateTime(new Date());
            authAttachment.setModifyTime(authAttachment.getCreateTime());
            authAttachments.add(authAttachment);
        }


        int userId = userSession.getUserId();
        String name = authBean.getName().trim();
        String dept = authBean.getDept().trim();
        int no = authBean.getNo();

        boolean isNoExist;
        boolean isAuthed = false;
        if (authType == UserTypeEnum.STUDENT.getCode()) {

            if (StringUtil.isBlank(authBean.getMajor())) {
                return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
            }
            Student student = studentService.findByUserId(userId);
            if (student != null) {
                isAuthed = true;
            }
            isNoExist = studentService.isExist(no);
        } else {
            Teacher teacher = teacherService.findByUserId(userId);
            if (teacher != null) {
                isAuthed = true;
            }
            isNoExist = teacherService.isExist(no);
        }
        if (isAuthed) {
            return ResponseEntity.failure(Errors.AUTH_ALREADY_EXISTS);
        }
        if (isNoExist) {
            return ResponseEntity.failure(Errors.AUTH_ALREADY_EXISTS);
        }

        UserInfo userInfo = userInfoService.findById(userId);
        if (authType == UserTypeEnum.STUDENT.getCode()) {
            Student student = new Student(no, userId, name, dept, authBean.getMajor().trim());
            student.setModifyTime(student.getCreateTime());
            studentService.save(student);

            userInfo.setUserType(UserTypeEnum.AUTHINT_STUDENT.getCode());
            userInfo.setStudent(student);
            userInfo.setModifyTime(new Date());
            userInfoService.save(userInfo);
        } else {
            Teacher teacher = new Teacher(no, userId, name, dept);
            teacher.setModifyTime(teacher.getCreateTime());
            teacherService.save(teacher);

            userInfo.setTeacher(teacher);
            userInfo.setUserType(UserTypeEnum.AUTHINT_TEACHER.getCode());
            userInfo.setModifyTime(new Date());
            userInfoService.save(userInfo);
        }

        super.setUserSession(request, userInfo);
        authAttachmentService.save(authAttachments);
        UserInfoBean userInfoBean = new UserInfoBean(userInfo);
        return ResponseEntity.success().set(BankConsts.DATA, userInfoBean);
    }
}
