package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskLog;
import cn.longhaiyan.task.repository.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-5-17.
 */
@Service
public class TaskLogServiceImpl implements TaskLogService {
    @Autowired
    private TaskLogRepository taskLogRepository;

    @Override
    public void save(TaskLog taskLog) {
        if (taskLog != null) {
            taskLogRepository.save(taskLog);
        }
    }
}
