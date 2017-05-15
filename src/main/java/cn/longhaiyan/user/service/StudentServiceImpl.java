package cn.longhaiyan.user.service;

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
    public Student findByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }
        return studentRepository.findByUserId(userId);
    }
}
