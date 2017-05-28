package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.repository.TaskFinishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-5-17.
 */
@Service
public class TaskFinishServiceImpl implements TaskFinishService {
    @Autowired
    private TaskFinishRepository taskFinishRepository;

    @Override
    public void save(TaskFinish taskFinish) {
        if (taskFinish == null) {
            return;
        }
        taskFinishRepository.save(taskFinish);
    }

    @Override
    public TaskFinish findById(int id) {
        if (id <= 0) {
            return null;
        }
        return taskFinishRepository.findById(id);
    }

    @Override
    public int countByTakerId(int takerId) {
        if (takerId <= 0) {
            return 0;
        }
//        return taskFinishRepository.countByTakerId(takerId);
        return taskFinishRepository.countByTakerIdAndStatusIsNot(takerId, TaskStatusEnum.DELETE.getCode());
    }

}
