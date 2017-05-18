package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskComplete;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenxb on 17-5-17.
 */
@Transactional
@Repository
public interface TaskCompleteRepository extends PagingAndSortingRepository<TaskComplete, Long> {

}
