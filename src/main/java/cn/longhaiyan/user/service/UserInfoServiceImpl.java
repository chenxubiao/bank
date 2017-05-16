package cn.longhaiyan.user.service;

import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.user.domain.Student;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.domain.UserRole;
import cn.longhaiyan.user.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenxb on 17-4-1.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @Override
    public UserInfo findByEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return null;
        }
        UserInfo userInfo = userInfoRepository.findByEmail(email);
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public UserInfo findByCellphone(String cellphone) {
        if (StringUtil.isEmpty(cellphone)) {
            return null;
        }
        UserInfo userInfo = userInfoRepository.findByCellphone(cellphone);
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public UserInfo findByUserName(String userName) {
        if (StringUtil.isEmpty(userName)) {
            return null;
        }
        UserInfo userInfo = userInfoRepository.findByUserName(userName);
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        userInfo = userInfoRepository.save(userInfo);
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public boolean isEmailExist(String email) {
        if (StringUtil.isBlank(email)) {
            return false;
        }
        return userInfoRepository.countByEmail(email) > 0;
    }

    @Override
    public boolean isCellphoneExist(String cellphone) {
        if (StringUtil.isBlank(cellphone)) {
            return false;
        }
        return userInfoRepository.countByCellphone(cellphone) > 0;
    }

    @Override
    public boolean isUserNameExist(String userName) {
        if (StringUtil.isBlank(userName)) {
            return false;
        }
        return userInfoRepository.countByUserName(userName) > 0;
    }

    @Override
    public UserInfo findById(int id) {
        if (id <= 0) {
            return null;
        }
        UserInfo userInfo = userInfoRepository.findById(id);
        if (userInfo == null) {
            return null;
        }
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public UserInfo findByIdAndNormal(int id) {
        if (id <= 0) {
            return null;
        }
        UserInfo userInfo = userInfoRepository.findByIdAndStatus(id, BankConsts.UserStatus.USER_IS_NORMAL);
        return userInfo;
//        return setUserProfile(userInfo);
    }

    @Override
    public int countAll() {
        return userInfoRepository.countAllByIdGreaterThan(0);
    }
//
//    private UserInfo setUserProfile(UserInfo userInfo) {
//        if (userInfo == null) {
//            return userInfo;
//        }
//        List<UserRole> userRoleList = userInfo.getUserRoleList();
//        userInfo.setUserRoleList(userRoleList);
//        if (userInfo.getUserType() == BankConsts.UserType.STUDENT) {
//            Student student = studentService.findByUserId(userInfo.getId());
//            userInfo.setStudent(student);
//        } else if (userInfo.getUserType() == BankConsts.UserType.TEACHER) {
//            Teacher teacher = teacherService.findByUserId(userInfo.getId());
//            userInfo.setTeacher(teacher);
//        }
//        return userInfo;
//    }

}
