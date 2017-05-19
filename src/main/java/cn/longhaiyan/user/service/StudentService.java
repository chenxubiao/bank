package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.Student;

/**
 * Created by chenxb on 17-5-10.
 */
public interface StudentService {

    boolean isExist(int no);

    void save(Student student);
}
