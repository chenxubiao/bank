package cn.longhaiyan.task.service;

import cn.longhaiyan.task.domain.TaskFinish;

/**
 * Created by chenxb on 17-5-17.
 */
public interface TaskFinishService {
    void save(TaskFinish taskFinish);

    TaskFinish findById(int id);

    int countByTakerId(int takerId);
}
