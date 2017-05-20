package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskTag;

import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
public interface TaskTagService {

    void save(TaskTag taskTag);

    void save(List<TaskTag> taskTags);

    List<TaskTag> findByTaskId(int taskId);
}
