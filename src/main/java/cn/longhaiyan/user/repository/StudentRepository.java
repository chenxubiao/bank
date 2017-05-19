package cn.longhaiyan.user.repository;

import cn.longhaiyan.user.domain.Student;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chenxb on 17-5-10.
 */
@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

    int countBySno(int sno);
}
