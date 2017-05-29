package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
public interface TaskInfoService {
    void save(TaskInfo taskInfo);

    Page<TaskInfo> findPublishingTask(Pageable pageable);

    Page<TaskInfo> findByStatus(int status, Pageable pageable);

    TaskInfo findById(int id);

    List<TaskInfo> findByUserId(int useId);

    int countByUserId(int userId);

    List<TaskInfo> findByStatusIn(List<Integer> statusList);

    List<TaskInfo> findByIdIn(List<Integer> ids);

    void setTaskTags(TaskInfo taskInfo);
}
