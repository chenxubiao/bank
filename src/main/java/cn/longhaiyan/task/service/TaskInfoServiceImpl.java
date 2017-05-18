package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.repository.TaskInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-5-17.
 */
@Service
public class TaskInfoServiceImpl implements TaskInfoService {
    @Autowired
    private TaskInfoRepository taskInfoRepository;


    @Override
    public void save(TaskInfo taskInfo) {
        if (taskInfo == null) {
            return;
        }
        taskInfoRepository.save(taskInfo);
    }
}
