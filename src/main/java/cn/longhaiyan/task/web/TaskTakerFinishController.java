package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;
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

/**
 * Created by chenxb on 17-5-20.
 */
@RestController
public class TaskTakerFinishController extends CommonController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private TaskLogService taskLogService;

    @RequestMapping(value = "/task/finish/take/done/data", method = RequestMethod.POST)
    public ResponseEntity takerFinish(HttpServletRequest request,
                                      @RequestParam(value = "taskId", defaultValue = "0") int taskId) {
        if (taskId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        TaskInfo taskInfo = taskInfoService.findById(taskId);
        if (taskInfo == null) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }

        if (taskInfo.getStatus() != TaskStatusEnum.RECEIVE.getCode()) {
            return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
        }
        TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
        if (taskFinish == null) {
            return ResponseEntity.failure(Errors.TASK_FINISH_NOT_FOUND);
        }
        UserSession userSession = super.getUserSession(request);
        if (userSession.getUserId() != taskFinish.getTakerId()) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        // TODO: 17-5-20
        return null;
    }
}
