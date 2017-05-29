package cn.longhaiyan.task.repository;

import cn.longhaiyan.task.domain.TaskFinish;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
@Transactional
@Repository
public interface TaskFinishRepository extends PagingAndSortingRepository<TaskFinish, Long> {

    TaskFinish findById(int id);

    int countByTakerId(int takerId);

    int countByTakerIdAndStatusIsNot(int userId, int status);

    List<TaskFinish> findAllByUserIdAndStatusIsNot(int takerId, int status);
}
