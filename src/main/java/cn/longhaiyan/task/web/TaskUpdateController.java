package cn.longhaiyan.task.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
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
import cn.longhaiyan.task.enums.TaskUpdateEnum;
import cn.longhaiyan.task.service.TaskFinishService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.task.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxb on 17-5-28.
 */
@RestController
public class TaskUpdateController extends CommonController {

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


    /**
     * 删除
     * 只有发布中和已完成的状态可删除
     * <p>
     * 用户本人                 他人
     * 发布中  可点击完成；      可接单
     * 已接单  可点击完成；      申请完成，
     * <p>
     * 已接单
     * 申请提前结束               确认提前结束
     * 确认提前结束               申请提前结束
     *
     * @param request
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/task/info/update/data", method = RequestMethod.POST)
    public ResponseEntity deleteTask(HttpServletRequest request,
                                     @RequestParam(value = "taskId", defaultValue = "0") int taskId,
                                     @RequestParam(value = "type", defaultValue = "0") int type) {

        if (taskId <= 0 || TaskUpdateEnum.isNotContain(type)) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
        int userId = userSession.getUserId();
        TaskInfo taskInfo = taskInfoService.findById(taskId);
        if (taskInfo == null || taskInfo.getStatus() == TaskStatusEnum.DELETE.getCode()) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
        if (taskFinish == null) {
            return ResponseEntity.failure(Errors.TASK_FINISH_NOT_FOUND);
        }
        List<Integer> userIds = new ArrayList<>();
        userIds.add(taskInfo.getUserId());
        if (taskFinish.getTakerId() > 0) {
            userIds.add(taskFinish.getTakerId());
        }
        if (!userIds.contains(userId)) {
            return ResponseEntity.failure(Errors.PERMISION_DENIED);
        }
        /**
         * 允许删除的状态  type == 1
         *
         * stu
         * 1    发布中的任务
         * 3    已结束，未接单
         * 7    已结束，接单后退款完成
         * 9    已完成
         * 11   未接单，任务超期
         *
         */
        if (type == TaskUpdateEnum.DELETE.getCode() && userId == taskInfo.getUserId()) {
            if (taskInfo.getStatus() == TaskStatusEnum.PUBLISH.getCode()
                    || taskInfo.getStatus() == TaskStatusEnum.DONE_NOT_RECEIVE.getCode()
                    || taskInfo.getStatus() == TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode()
                    || taskInfo.getStatus() == TaskStatusEnum.DONE_TASK.getCode()
                    || taskInfo.getStatus() == TaskStatusEnum.LOKING_TIME_OVER_PUBLISHING.getCode()) {

                int status = taskInfo.getStatus();

                taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskId, userId, TaskStatusEnum.DELETE.getCode());
                taskLog.setModifyTime(taskLog.getCreateTime());
                taskLogService.save(taskLog);

                taskFinish.setStatus(TaskStatusEnum.DELETE.getCode());
                taskFinish.setModifyTime(new Date());
                taskFinishService.save(taskFinish);

                taskInfo.setStatus(TaskStatusEnum.DELETE.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                Message message = new Message
                        (MessageTypeEnum.TASK_DELETE.getCode()
                                , BankConsts.USER_IS_SYSTEM, userId, taskLog.getId(), taskInfo.getTitle());
                message.setModifyTime(message.getCreateTime());
                messageService.save(message);

                if (status == TaskStatusEnum.PUBLISH.getCode()
                        || TaskStatusEnum.LOKING_TIME_OVER_PUBLISHING.getCode() == status) {
                    Account account = accountService.findByUserId(taskInfo.getUserId());
                    account.setTotalMoney(account.getTotalMoney() + taskInfo.getMoney());
                    AccountLog accountLog = new AccountLog(taskInfo.getUserId()
                            , AccountLogTypeEnum.ADD_TASK_CLOSE.getCode(), taskInfo.getMoney(), taskLog.getId(), taskInfo.getTitle(), account);
                    accountLog.setBalance(account.getTotalMoney());
                    accountLog.setModifyTime(accountLog.getCreateTime());
                    accountLogService.save(accountLog);

                    Message messageAcc = new Message(MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                            , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), accountLog.getId(), accountLog.getMessage());
                    messageAcc.setModifyTime(messageAcc.getCreateTime());
                    messageService.save(messageAcc);
                }

            } else {
                return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
            }
        } else if (type == TaskUpdateEnum.CLOSING.getCode()) {

            /**
             * 申请提前结束       type == 2
             *
             * 用户为需求发布者    userId == taskInfo.getUserId
             * stu
             * 1
             * 4
             *
             *
             */
            if (userId == taskInfo.getUserId()) {
                if (taskInfo.getStatus() == TaskStatusEnum.PUBLISH.getCode()) {
                    taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                    taskInfo.setModifyTime(new Date());
                    taskInfoService.save(taskInfo);

                    TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskId, userId, TaskStatusEnum.DONE_NOT_RECEIVE.getCode());
                    taskLog.setModifyTime(taskLog.getCreateTime());
                    taskLogService.save(taskLog);

                    taskFinish.setStatus(TaskStatusEnum.DONE_NOT_RECEIVE.getCode());
                    taskFinish.setModifyTime(new Date());
                    taskFinishService.save(taskFinish);

                    taskInfo.setStatus(TaskStatusEnum.DONE_NOT_RECEIVE.getCode());
                    taskInfo.setModifyTime(new Date());
                    taskInfoService.save(taskInfo);

                    Message message = new Message
                            (MessageTypeEnum.TASK_PAUSE.getCode()
                                    , BankConsts.USER_IS_SYSTEM, userId, taskLog.getId(), taskInfo.getTitle());
                    message.setModifyTime(message.getCreateTime());
                    messageService.save(message);

                    Account account = accountService.findByUserId(taskInfo.getUserId());
                    account.setTotalMoney(account.getTotalMoney() + taskInfo.getMoney());
                    account.setModifyTime(new Date());
                    accountService.save(account);
                    AccountLog accountLog = new AccountLog(taskInfo.getUserId()
                            , AccountLogTypeEnum.ADD_TASK_CLOSE.getCode()
                            , taskInfo.getMoney(), taskLog.getId(), taskInfo.getTitle(), account);
                    accountLog.setBalance(account.getTotalMoney());
                    accountLog.setModifyTime(accountLog.getCreateTime());
                    accountLogService.save(accountLog);

                    Message toSenderAccountMsg = new Message
                            (MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                                    , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), accountLog.getId(), accountLog.getMessage());
                    toSenderAccountMsg.setModifyTime(toSenderAccountMsg.getCreateTime());
                    messageService.save(toSenderAccountMsg);

                } else if (taskInfo.getStatus() == TaskStatusEnum.RECEIVE.getCode()
                        || taskInfo.getStatus() == TaskStatusEnum.LOKING_TIME_OVER_RECEIVE.getCode()) {

                    /**
                     * 任务发布者在接单后申请提前结束  type == 2
                     */
                    taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                    taskInfo.setModifyTime(new Date());
                    taskInfoService.save(taskInfo);

                    TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
                            , TaskStatusEnum.SENDER_CLONING.getCode());

                    taskLog.setModifyTime(taskLog.getCreateTime());
                    taskLogService.save(taskLog);

                    taskFinish.setStatus(TaskStatusEnum.SENDER_CLONING.getCode());
                    taskFinish.setModifyTime(new Date());
                    taskFinishService.save(taskFinish);

                    taskInfo.setStatus(TaskStatusEnum.SENDER_CLONING.getCode());
                    taskInfo.setModifyTime(new Date());
                    taskInfoService.save(taskInfo);

                    Message toSenderMsg = new Message
                            (MessageTypeEnum.TASK_CLOSING_BY_SENDER_TO_SENDER.getCode()
                                    , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), taskLog.getId(), taskInfo.getTitle());
                    toSenderMsg.setModifyTime(toSenderMsg.getCreateTime());
                    messageService.save(toSenderMsg);

                    Message toTakerMsg = new Message
                            (MessageTypeEnum.TASK_CLOSING_BY_SENDER_TO_TAKER.getCode()
                                    , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), taskLog.getId(), taskInfo.getTitle());
                    toTakerMsg.setModifyTime(toTakerMsg.getCreateTime());
                    messageService.save(toTakerMsg);
                } else {
                    return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
                }
            } else if (userId == taskFinish.getTakerId() && taskInfo.getStatus() == TaskStatusEnum.RECEIVE.getCode()) {
                /**
                 * 任务接收者申请结束        type ==2
                 *
                 * stu
                 * 4
                 */
                taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
                        , TaskStatusEnum.TAKER_CLONING.getCode());
                taskLog.setModifyTime(taskLog.getCreateTime());
                taskLogService.save(taskLog);

                taskFinish.setStatus(TaskStatusEnum.TAKER_CLONING.getCode());
                taskFinish.setModifyTime(new Date());
                taskFinishService.save(taskFinish);

                taskInfo.setStatus(TaskStatusEnum.TAKER_CLONING.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                Message toSenderMsg = new Message
                        (MessageTypeEnum.TASK_CLOSING_BY_TAKER_TO_SENDER.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), taskLog.getId(), taskInfo.getTitle());
                toSenderMsg.setModifyTime(toSenderMsg.getCreateTime());
                messageService.save(toSenderMsg);

                Message toTakerMsg = new Message
                        (MessageTypeEnum.TASK_CLOSING_BY_TAKER_TO_TAKER.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), taskLog.getId(), taskInfo.getTitle());
                toTakerMsg.setModifyTime(toTakerMsg.getCreateTime());
                messageService.save(toTakerMsg);
            } else {
                return ResponseEntity.failure(Errors.PERMISION_DENIED);
            }
        } else if (type == TaskUpdateEnum.CLOSED.getCode()) {
            /**
             * 同意提前结束 type == 3
             *
             * stu
             * 5
             * 13
             */
            if (userId == taskInfo.getUserId()
                    && (taskInfo.getStatus() == TaskStatusEnum.TAKER_CLONING.getCode())) {

                taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                TaskLog aggreeLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
                        , TaskStatusEnum.SENDER_AGREE_CLOSED.getCode());
                aggreeLog.setModifyTime(aggreeLog.getCreateTime());
                taskLogService.save(aggreeLog);

                TaskLog returnMoneyLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
                        , TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                returnMoneyLog.setModifyTime(new Date());
                taskLogService.save(returnMoneyLog);

                taskFinish.setStatus(TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                taskFinish.setModifyTime(new Date());
                taskFinishService.save(taskFinish);

                taskInfo.setStatus(TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                Account account = accountService.findByUserId(taskInfo.getUserId());
                account.setTotalMoney(account.getTotalMoney() + taskInfo.getMoney());
                account.setModifyTime(new Date());
                accountService.save(account);
                AccountLog accountLog = new AccountLog(taskInfo.getUserId()
                        , AccountLogTypeEnum.ADD_TASK_CLOSE.getCode(), taskInfo.getMoney(), returnMoneyLog.getId(), taskInfo.getTitle(), account);
                accountLog.setBalance(account.getTotalMoney());
                accountLog.setModifyTime(accountLog.getCreateTime());
                accountLogService.save(accountLog);

                Message toSenderAccountMsg = new Message
                        (MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), aggreeLog.getId(), accountLog.getMessage());
                toSenderAccountMsg.setModifyTime(toSenderAccountMsg.getCreateTime());
                messageService.save(toSenderAccountMsg);


                Message toSenderMsg = new Message
                        (MessageTypeEnum.TASK_CLOSED.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), aggreeLog.getId(), taskInfo.getTitle());
                toSenderMsg.setModifyTime(toSenderMsg.getCreateTime());
                messageService.save(toSenderMsg);

                Message toTakerMsg = new Message
                        (MessageTypeEnum.TASK_CLOSED.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), aggreeLog.getId(), taskInfo.getTitle());
                toTakerMsg.setModifyTime(toTakerMsg.getCreateTime());
                messageService.save(toTakerMsg);

            } else if (userId == taskFinish.getTakerId()
                    && taskInfo.getStatus() == TaskStatusEnum.SENDER_CLONING.getCode()) {

                taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                TaskLog aggreeLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
                        , TaskStatusEnum.TAKER_AGREE_CLOSED.getCode());
                aggreeLog.setModifyTime(aggreeLog.getCreateTime());
                taskLogService.save(aggreeLog);

                TaskLog returnMoneyLog = new TaskLog(taskInfo.getFinishId(), taskId, taskInfo.getUserId()
                        , TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                returnMoneyLog.setModifyTime(new Date());
                taskLogService.save(returnMoneyLog);

                taskFinish.setStatus(TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                taskFinish.setModifyTime(new Date());
                taskFinishService.save(taskFinish);

                taskInfo.setStatus(TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode());
                taskInfo.setModifyTime(new Date());
                taskInfoService.save(taskInfo);

                Account account = accountService.findByUserId(taskInfo.getUserId());
                account.setTotalMoney(account.getTotalMoney() + taskInfo.getMoney());
                account.setModifyTime(new Date());
                accountService.save(account);
                AccountLog accountLog = new AccountLog(taskInfo.getUserId()
                        , AccountLogTypeEnum.ADD_TASK_CLOSE.getCode(), taskInfo.getMoney(), returnMoneyLog.getId(), taskInfo.getTitle(), account);
                accountLog.setBalance(account.getTotalMoney());
                accountLog.setModifyTime(accountLog.getCreateTime());
                accountLogService.save(accountLog);

                Message toSenderAccountMsg = new Message
                        (MessageTypeEnum.ACCOUNT_CHANGE.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), aggreeLog.getId(), accountLog.getMessage());
                toSenderAccountMsg.setModifyTime(toSenderAccountMsg.getCreateTime());
                messageService.save(toSenderAccountMsg);


                Message toSenderMsg = new Message
                        (MessageTypeEnum.TASK_CLOSED.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), aggreeLog.getId(), taskInfo.getTitle());
                toSenderMsg.setModifyTime(toSenderMsg.getCreateTime());
                messageService.save(toSenderMsg);

                Message toTakerMsg = new Message
                        (MessageTypeEnum.TASK_CLOSED.getCode()
                                , BankConsts.USER_IS_SYSTEM, taskFinish.getTakerId(), aggreeLog.getId(), taskInfo.getTitle());
                toTakerMsg.setModifyTime(toTakerMsg.getCreateTime());
                messageService.save(toTakerMsg);
            } else {
                return ResponseEntity.failure(Errors.PERMISION_DENIED);
            }

        } else {
            return ResponseEntity.failure(Errors.PERMISION_DENIED);
        }
        return ResponseEntity.success();





//            } else if (type == TaskUpdateEnum.REPUBLISH.getCode()) {
//                if (taskInfo.getStatus() == TaskStatusEnum.LOKING_TIME_OVER_PUBLISHING.getCode()) {
//
//                    taskInfo.setStatus(TaskStatusEnum.LOKING_PRESENT.getCode());
//                    taskInfo.setModifyTime(new Date());
//                    taskInfoService.save(taskInfo);
//
//                    TaskLog taskLog = new TaskLog(taskInfo.getFinishId(), taskId, userId
//                            , TaskStatusEnum.LOKING_TIME_DONE_PUB.getCode());
//                    taskLog.setModifyTime(taskLog.getCreateTime());
//                    taskLogService.save(taskLog);
//
//                    taskFinish.setStatus(TaskStatusEnum.LOKING_TIME_DONE_PUB.getCode());
//                    taskFinish.setModifyTime(new Date());
//                    taskFinishService.save(taskFinish);
//
//                    TaskFinish reFinish = new TaskFinish(taskInfo, TaskStatusEnum.PUBLISH.getCode());
//                    reFinish.setModifyTime(reFinish.getCreateTime());
//                    taskFinishService.save(reFinish);
//
//                    TaskLog reLog = new TaskLog(reFinish.getId(), taskId, userId
//                            , TaskStatusEnum.PUBLISH.getCode());
//                    reLog.setModifyTime(reLog.getCreateTime());
//                    taskLogService.save(reLog);
//
//                    taskInfo.setStatus(TaskStatusEnum.PUBLISH.getCode());
//                    taskInfo.setFinishId(reFinish.getId());
//                    taskInfo.setModifyTime(new Date());
//                    taskInfoService.save(taskInfo);
//
//                    Message message = new Message(MessageTypeEnum.TASK_PUBLISH.getCode(), BankConsts.USER_IS_SYSTEM, taskInfo.getUserId(), reLog.getId(), taskInfo.getTitle());
//                    message.setModifyTime(message.getCreateTime());
//                    messageService.save(message);
//                }
//            }
    }
}
