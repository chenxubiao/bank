package cn.longhaiyan.user.service;

import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.user.domain.Student;
import cn.longhaiyan.user.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-5-10.
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public boolean isExist(String no) {
        if (StringUtil.isBlank(no)) {
            return false;
        }
        return studentRepository.countBySno(no) > 0;
    }

    @Override
    public void save(Student student) {
        if (student == null) {
            return;
        }
        studentRepository.save(student);
    }

}
