package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
@Repository
@Transactional
public interface TaskInfoRepository extends PagingAndSortingRepository<TaskInfo, Long> {

    Page<TaskInfo> findByStatusOrderByUrgentMoneyDescIdDesc(int status, Pageable pageable);

    TaskInfo findById(int id);

    List<TaskInfo> findAllByUserIdOrderByIdDesc(int useId);

    int countByUserId(int userId);

    List<TaskInfo> findAllByStatusIn(List<Integer> status);
}
