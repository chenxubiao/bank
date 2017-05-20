package cn.longhaiyan.task.service;

import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.task.domain.TaskTag;
import cn.longhaiyan.task.repository.TaskTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
@Service
public class TaskTagServiceImpl implements TaskTagService {
    @Autowired
    private TaskTagRepository taskTagRepository;


    @Override
    public void save(TaskTag taskTag) {
        if (taskTag == null) {
            return;
        }
        taskTagRepository.save(taskTag);
    }

    @Override
    public void save(List<TaskTag> taskTags) {
        if (CollectionUtil.isNotEmpty(taskTags)) {
            taskTagRepository.save(taskTags);
        }
    }

    @Override
    public List<TaskTag> findByTaskId(int taskId) {
        if (taskId <= 0) {
            return null;
        }
        return taskTagRepository.findAllByTaskId(taskId);
    }
}
