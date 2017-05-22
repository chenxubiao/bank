package cn.longhaiyan.task.schedule;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.TimeUtil;
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
 * Created by chenxb on 17-5-22.
 */
@Component
public class TaskFinishVerifySchedule {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;

    @Scheduled(cron = "* 0/10 * * * ? ")  //每10min执行一次
    public void freashTaskStatus() {

        // 启用多线程，更新task status
        Thread thread = new TaskVerify();
        thread.start();
    }

    class TaskVerify extends Thread {

        @Override
        public void run() {
            List<Integer> statusList = new ArrayList<>();
            statusList.add(TaskStatusEnum.RECEIVE_COMPLETE.getCode());
            List<TaskInfo> taskInfoList = taskInfoService.findByStatusIn(statusList);
            int i = 0;
            if (CollectionUtil.isNotEmpty(taskInfoList)) {
                for (TaskInfo taskInfo : taskInfoList) {
                    TaskLog taskLog = taskLogService.findByFinishIdAndStatus(taskInfo.getFinishId()
                            , TaskStatusEnum.RECEIVE_COMPLETE.getCode());
                    if (taskLog == null) {
//                        Exception exception = new Exception("未找到 taskLog ...");
//                        exception.printStackTrace();
                        continue;
                    }
                    Date tenMinLater = TimeUtil.disposeMin(taskLog.getCreateTime(), 5, TimeUtil.DATE_TYPE_AFTER);
                    //现在时间小于  合法
                    if (new Date().compareTo(tenMinLater) < 0) {
                        continue;
                    }
                    i++;

                    TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
                    taskInfo.setStatus(TaskStatusEnum.DONE_TASK.getCode());
                    taskInfo.setModifyTime(new Date());
                    taskInfoService.save(taskInfo);

                    taskFinish.setStatus(TaskStatusEnum.DONE_TASK.getCode());
                    taskFinish.setModifyTime(new Date());
                    taskFinishService.save(taskFinish);

                    TaskLog taskLogDone = new TaskLog(taskFinish.getId()
                            , taskFinish.getTaskId(), taskInfo.getUserId(), TaskStatusEnum.DONE_TASK.getCode());
                    taskLogDone.setTakerId(taskFinish.getTakerId());
                    taskLogDone.setModifyTime(taskLog.getCreateTime());
                    taskLogService.save(taskLogDone);


                    Account takerAccount = accountService.findByUserId(taskFinish.getTakerId());
                    takerAccount.setTotalMoney(takerAccount.getTotalMoney() + taskInfo.getMoney());
                    takerAccount.setModifyTime(new Date());

                    AccountLog accountLog = new AccountLog(taskFinish.getTakerId()
                            , AccountLogTypeEnum.ADD_TASK_DONE.getCode(), taskInfo.getMoney(), taskInfo.getId(), taskInfo.getTitle(), takerAccount);
                    accountLog.setBalance(takerAccount.getTotalMoney());
                    accountLog.setModifyTime(new Date());
                    accountLogService.save(accountLog);

                    Message accountMsgToTaker = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                            , BankConsts.USER_IS_SYSTEM, takerAccount.getUserId(), accountLog.getId(), accountLog.getMessage());
                    accountMsgToTaker.setModifyTime(new Date());
                    messageService.save(accountMsgToTaker);

                    accountService.save(takerAccount);
                }
            }

            if (i > 0) {
                System.out.println("****** TaskVerify Thread end, done update task num = " + i + " ******");
            }
        }
    }
}
