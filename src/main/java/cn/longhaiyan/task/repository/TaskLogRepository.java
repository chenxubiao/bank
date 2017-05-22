package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenxb on 17-5-17.
 */
@Transactional
@Repository
public interface TaskLogRepository extends PagingAndSortingRepository<TaskLog, Long> {

    TaskLog findByFinishIdAndStatus(int finishId, int status);
}
