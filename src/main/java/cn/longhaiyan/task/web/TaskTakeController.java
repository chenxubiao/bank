package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.PojoBean;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.StringUtil;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenxb on 17-5-19.
 */
@RestController
public class TaskTakeController extends CommonController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TaskLogService taskLogService;



    /**
     * 用户接单接口
     *
     * @param request
     * @param info
     * @return
     */
    @RequestMapping(value = "/task/finish/take/order/data", method = RequestMethod.POST)
    public ResponseEntity orderReceiving(HttpServletRequest request, PojoBean info) {

        if (info.getId() <= 0 || StringUtil.isBlank(info.getValue())) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
        int receiverId = userSession.getUserId();
        int taskId = info.getId();
        TaskInfo taskInfo = taskInfoService.findById(taskId);
        if (taskInfo == null) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        if (taskInfo.getStatus() != TaskStatusEnum.PUBLISH.getCode()) {
            return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
        }
        if (receiverId == taskInfo.getUserId()) {
            return ResponseEntity.failure(Errors.TASK_RECEIVER_ERROR);
        }
        taskInfo.setStatus(TaskStatusEnum.LOKING_RECEIVING.getCode());
        taskInfoService.save(taskInfo);

        String value = info.getValue().trim();
        TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
        taskInfo.setStatus(TaskStatusEnum.RECEIVE.getCode());
        taskInfo.setModifyTime(new Date());
        taskInfoService.save(taskInfo);

        TaskLog taskLog = new TaskLog
                (taskFinish.getId(), taskId, taskInfo.getUserId(), TaskStatusEnum.RECEIVE.getCode());
        taskLog.setTakerId(userSession.getUserId());
        taskLog.setModifyTime(taskLog.getCreateTime());
        taskLog.setRemark(value);
        taskLogService.save(taskLog);

        Message toSenderMsg = new Message(MessageTypeEnum.TASK_RECEIVED_TO_SENDER.getCode(),
                BankConsts.USER_IS_SYSTEM,
                taskInfo.getUserId(),
                taskLog.getId(), value);
        toSenderMsg.setModifyTime(new Date());
        messageService.save(toSenderMsg);

        Message toReceiverMsg = new Message(MessageTypeEnum.TASK_REVEIVED_TO_RECEIVER.getCode(),
                BankConsts.USER_IS_SYSTEM,
                userSession.getUserId(),
                taskLog.getId(),
                "接单成功");
        toReceiverMsg.setModifyTime(toReceiverMsg.getCreateTime());

        messageService.save(toReceiverMsg);
        return ResponseEntity.success();
    }

}
