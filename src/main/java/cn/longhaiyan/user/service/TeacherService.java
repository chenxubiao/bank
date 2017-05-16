package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.Teacher;

/**
 * Created by chenxb on 17-5-11.
 */
public interface TeacherService {
    Teacher findByUserId(int userId);

    boolean isExist(int no);

    void save(Teacher teacher);
}
