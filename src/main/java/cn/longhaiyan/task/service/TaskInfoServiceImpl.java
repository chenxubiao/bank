package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.repository.TaskInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Page<TaskInfo> findPublishingTask(Pageable pageable) {
        return taskInfoRepository.findByStatusOrderByUrgentMoneyDescIdDesc(TaskStatusEnum.PUBLISH.getCode(), pageable);
    }

    @Override
    public Page<TaskInfo> findByStatus(int status, Pageable pageable) {
        if (TaskStatusEnum.isNotContain(status)) {
            return null;
        }
        return taskInfoRepository.findByStatusOrderByUrgentMoneyDescIdDesc(status, pageable);
    }

    @Override
    public TaskInfo findById(int id) {
        if (id <= 0) {
            return null;
        }
        return taskInfoRepository.findById(id);
    }
}
