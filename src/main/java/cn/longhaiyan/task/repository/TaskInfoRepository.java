package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenxb on 17-5-17.
 */
@Repository
@Transactional
public interface TaskInfoRepository extends PagingAndSortingRepository<TaskInfo, Long> {

}
