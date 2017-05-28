package cn.longhaiyan.task.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenxb on 17-5-20.
 */
@RestController
public class TaskFinishController extends CommonController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private MessageService messageService;

    @Authority(privilege = BankConsts.UserRole.USER_IS_STUDENT + "," + BankConsts.UserRole.USER_IS_TEACHER)
    @RequestMapping(value = "/task/finish/done/update/data", method = RequestMethod.POST)
    public ResponseEntity takerFinish(HttpServletRequest request,
                                      @RequestParam(value = "taskId", defaultValue = "0") int taskId) {

        if (taskId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        TaskInfo taskInfo = taskInfoService.findById(taskId);
        if (taskInfo == null || taskInfo.getStatus() == TaskStatusEnum.DELETE.getCode()) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }

        if (taskInfo.getStatus() != TaskStatusEnum.RECEIVE.getCode() && taskInfo.getStatus() != TaskStatusEnum.RECEIVE_COMPLETE.getCode()) {
            return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
        }
        TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
        if (taskFinish == null) {
            return ResponseEntity.failure(Errors.TASK_FINISH_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        if (userSession.getUserId() != taskFinish.getTakerId() && userSession.getUserId() != taskInfo.getUserId()) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        if (taskInfo.getStatus() == TaskStatusEnum.RECEIVE_COMPLETE.getCode() && userSession.getUserId() != taskInfo.getUserId()) {
            return ResponseEntity.failure(Errors.PERMISION_DENIED);
        }
        if (userSession.getUserId() == taskInfo.getUserId()) {
            taskInfo.setStatus(TaskStatusEnum.DONE_TASK.getCode());
            taskInfo.setModifyTime(new Date());
            taskInfoService.save(taskInfo);

            taskFinish.setStatus(TaskStatusEnum.DONE_TASK.getCode());
            taskFinish.setFinishTime(taskFinish.getStatus() == TaskStatusEnum.RECEIVE_COMPLETE.getCode() ? taskFinish.getModifyTime() : new Date());
            taskFinish.setModifyTime(new Date());
            taskFinishService.save(taskFinish);

            TaskLog taskLog = new TaskLog(taskFinish.getId()
                    , taskId, taskInfo.getUserId(), TaskStatusEnum.DONE_TASK.getCode());
            taskLog.setTakerId(taskFinish.getTakerId());
            taskLog.setModifyTime(taskLog.getCreateTime());
            taskLogService.save(taskLog);

            Message taskMsgToSender = new Message(MessageTypeEnum.TASK_DONE.getCode()
                    , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), taskLog.getId(), taskInfo.getTitle());
            taskMsgToSender.setModifyTime(new Date());
            messageService.save(taskMsgToSender);
            Message taskMsgToTaker = new Message(MessageTypeEnum.TASK_DONE.getCode()
                    , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), taskLog.getId(), taskInfo.getTitle());
            taskMsgToTaker.setModifyTime(new Date());
            messageService.save(taskMsgToTaker);

            Account takerAccount = accountService.findByUserId(taskFinish.getTakerId());
            takerAccount.setTotalMoney(takerAccount.getTotalMoney() + taskInfo.getMoney());
            takerAccount.setModifyTime(new Date());

            AccountLog accountLog = new AccountLog(taskFinish.getTakerId()
                    , AccountLogTypeEnum.ADD_TASK_DONE.getCode(), taskInfo.getMoney(), taskId, taskInfo.getTitle(), takerAccount);
            accountLog.setBalance(takerAccount.getTotalMoney());
            accountLog.setModifyTime(new Date());
            accountLogService.save(accountLog);

            Message accountMsgToTaker = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                    , BankConsts.USER_IS_SYSTEM, takerAccount.getUserId(), accountLog.getId(), accountLog.getMessage());
            accountMsgToTaker.setModifyTime(new Date());
            messageService.save(accountMsgToTaker);

            accountService.save(takerAccount);
        } else {

            //等待需求方确认
            taskInfo.setStatus(TaskStatusEnum.RECEIVE_COMPLETE.getCode());
            taskInfo.setModifyTime(new Date());
            taskInfoService.save(taskInfo);

            taskFinish.setStatus(TaskStatusEnum.RECEIVE_COMPLETE.getCode());
            taskFinish.setFinishTime(new Date());
            taskFinish.setModifyTime(taskFinish.getFinishTime());
            taskFinishService.save(taskFinish);

            TaskLog taskLog = new TaskLog(taskFinish.getId()
                    , taskId, taskInfo.getUserId(), TaskStatusEnum.RECEIVE_COMPLETE.getCode());
            taskLog.setTakerId(taskFinish.getTakerId());
            taskLog.setModifyTime(taskLog.getCreateTime());
            taskLogService.save(taskLog);

            Message taskMsgToSender = new Message(MessageTypeEnum.TASK_DONE_VERIFY.getCode()
                    , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), taskLog.getId(), taskInfo.getTitle());
            taskMsgToSender.setModifyTime(new Date());
            messageService.save(taskMsgToSender);

            taskInfo.setStatus(TaskStatusEnum.RECEIVE_COMPLETE.getCode());
            taskInfo.setModifyTime(new Date());
            taskInfoService.save(taskInfo);
        }
        return ResponseEntity.success();
    }
}
