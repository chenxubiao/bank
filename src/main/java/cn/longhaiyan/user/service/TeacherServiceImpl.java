package cn.longhaiyan.user.service;

import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.user.domain.Teacher;
import cn.longhaiyan.user.repository.TeacherRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-5-11.
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepositoy teacherRepositoy;

    @Override
    public boolean isExist(String no) {
        if (StringUtil.isBlank(no)) {
            return false;
        }
        return teacherRepositoy.countByTno(no) > 0;
    }

    @Override
    public void save(Teacher teacher) {
        if (teacher == null) {
            return;
        }
        teacherRepositoy.save(teacher);
    }
}
