package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskFinish;
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

}
