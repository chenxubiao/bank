package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.Student;

/**
 * Created by chenxb on 17-5-10.
 */
public interface StudentService {

    Student findByUserId(int userId);

    Student findByNoAndUserId(int no, int userId);

    boolean isExist(int no);

    void save(Student student);
}
