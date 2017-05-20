package cn.longhaiyan.task.schedule;

import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.domain.TaskLog;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.service.TaskFinishService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.task.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-20.
 */
@Component
public class TaskDeadTimeSchedule {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private MessageService messageService;

    @Scheduled(fixedDelayString = "60000")
    public void freashTaskStatus() {
        //todo 启用多线程，更新task status
        Thread thread = new TaskStatusUpdateThread();
        thread.start();
    }

    class TaskStatusUpdateThread extends Thread {

        @Override
        public void run() {
            System.out.println("****** Thread running, start update task status ******");
            List<Integer> statusList = new ArrayList<>();
            statusList.add(TaskStatusEnum.PUBLISH.getCode());
            statusList.add(TaskStatusEnum.RECEIVE.getCode());
            List<TaskInfo> taskInfoList = taskInfoService.findByStatusIn(statusList);
            if (CollectionUtil.isEmpty(taskInfoList)) {
                System.out.println("****** Thread end, none task list ******");
                return;
            }
            int i = 0;
            for (TaskInfo taskInfo : taskInfoList) {
                //现在时间小于 deadTime 合法
                if (new Date().compareTo(taskInfo.getDeadTime()) < 0) {
                    continue;
                }
                i++;
                //此时 task 已过期
                taskInfo.setStatus(TaskStatusEnum.LOKING_TIME_OVER.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);
                TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskInfo.getId(), taskInfo.getUserId(), TaskStatusEnum.LOKING_TIME_OVER.getCode());
                taskLog.setModifyTime(taskLog.getCreateTime());

                TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
                if (taskFinish != null) {
                    taskLog.setTakerId(taskFinish.getTakerId());
                    taskLogService.save(taskLog);
                    taskFinish.setStatus(TaskStatusEnum.LOKING_TIME_OVER.getCode());
                    taskFinish.setModifyTime(new Date());
                    taskFinishService.save(taskFinish);

                    Message messageToTaker = new Message(MessageTypeEnum.TASK_DEADTIME_OVER.getCode()
                            , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), taskLog.getId(), taskInfo.getTitle());
                    messageToTaker.setModifyTime(new Date());

                    messageService.save(messageToTaker);
                } else {
                    taskLogService.save(taskLog);
                }

                Message messageToUser = new Message(MessageTypeEnum.TASK_DEADTIME_OVER.getCode()
                        , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), taskLog.getId(), taskInfo.getTitle());
                messageToUser.setModifyTime(new Date());

                messageService.save(messageToUser);
            }

            System.out.println("****** Thread end, done update task num = " + i + " ******");
        }
    }
}
