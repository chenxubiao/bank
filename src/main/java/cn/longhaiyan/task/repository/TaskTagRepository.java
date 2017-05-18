package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskTag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenxb on 17-5-18.
 */
@Repository
@Transactional
public interface TaskTagRepository extends PagingAndSortingRepository<TaskTag, Long> {

}
