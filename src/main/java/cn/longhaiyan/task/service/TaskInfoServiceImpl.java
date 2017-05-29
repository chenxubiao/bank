package cn.longhaiyan.task.service;

import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.tag.service.TagInfoService;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.domain.TaskTag;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.repository.TaskInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
@Service
public class TaskInfoServiceImpl implements TaskInfoService {
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private TaskTagService taskTagService;
    @Autowired
    private TagInfoService tagInfoService;


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
        TaskInfo taskInfo = taskInfoRepository.findById(id);
        setTaskTags(taskInfo);
        return taskInfo;
    }

    @Override
    public List<TaskInfo> findByUserId(int useId) {
        if (useId <= 0) {
            return null;
        }
//        List<TaskInfo> taskInfoList = taskInfoRepository.findAllByUserIdOrderByIdDesc(useId);
        List<TaskInfo> taskInfoList = taskInfoRepository.findAllByUserIdAndStatusIsNotOrderByIdDesc
                (useId, TaskStatusEnum.DELETE.getCode());
        if (CollectionUtil.isEmpty(taskInfoList)) {
            return taskInfoList;
        }
        for (TaskInfo taskInfo : taskInfoList) {
            setTaskTags(taskInfo);
        }
        return taskInfoList;
    }

    @Override
    public int countByUserId(int userId) {
        if (userId <= 0) {
            return 0;
        }
//        return taskInfoRepository.countByUserId(userId);
        return taskInfoRepository.countByUserIdAndStatusIsNot(userId, TaskStatusEnum.DELETE.getCode());
    }

    @Override
    public List<TaskInfo> findByStatusIn(List<Integer> statusList) {
        if (CollectionUtil.isEmpty(statusList)) {
            return null;
        }
        List<TaskInfo> taskInfoList = taskInfoRepository.findAllByStatusIn(statusList);
        if (CollectionUtil.isEmpty(taskInfoList)) {
            return taskInfoList;
        }
        for (TaskInfo taskInfo : taskInfoList) {
            setTaskTags(taskInfo);
        }
        return taskInfoList;
    }

    @Override
    public List<TaskInfo> findByIdIn(List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return null;
        }
        List<TaskInfo> taskInfoList = taskInfoRepository.findAllByIdInOrderByIdDesc(ids);
        if (CollectionUtil.isEmpty(taskInfoList)) {
            return taskInfoList;
        }
        for (TaskInfo taskInfo : taskInfoList) {
            setTaskTags(taskInfo);
        }
        return taskInfoList;
    }

    private void setTaskTags(TaskInfo taskInfo) {
        if (taskInfo == null) {
            return;
        }
        List<TaskTag> taskTags = taskTagService.findByTaskId(taskInfo.getId());
        if (CollectionUtil.isEmpty(taskTags)) {
            return;
        }
        List<TagInfo> tagInfoList = new ArrayList<>();
        for (TaskTag taskTag : taskTags) {
            TagInfo tagInfo = tagInfoService.findById(taskTag.getTagId());
            tagInfoList.add(tagInfo);
        }
        taskInfo.setTags(tagInfoList);
    }

}
