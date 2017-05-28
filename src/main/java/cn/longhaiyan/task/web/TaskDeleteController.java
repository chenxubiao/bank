package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-28.
 */
@RestController
public class TaskDeleteController extends CommonController {

    @Autowired
    private TaskInfoService taskInfoService;

    @RequestMapping(value = "/task/info/delete/data", method = RequestMethod.POST)
    public ResponseEntity deleteTask(HttpServletRequest request,
                                     @RequestParam(value = "taskId", defaultValue = "0") int taskId) {

        if (taskId <= 0) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
        int userId = 0;
        if (userSession != null) {
            userId = userSession.getUserId();
        }
        TaskInfo taskInfo = taskInfoService.findById(taskId);
        if (taskInfo == null || taskInfo.getStatus() == TaskStatusEnum.DELETE.getCode()) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        if (taskInfo.getStatus() != TaskStatusEnum.PUBLISH.getCode()
                || taskInfo.getStatus() != TaskStatusEnum.DONE_NOT_RECEIVE.getCode()
                || taskInfo.getStatus() != TaskStatusEnum.DONE_TASK.getCode()
                || taskInfo.getStatus() != TaskStatusEnum.LOKING_TIME_OVER_PUBLISHING.getCode()
                || taskInfo.getStatus() != TaskStatusEnum.DONE_RECEIVE_CLOSE.getCode()) {

            return ResponseEntity.failure(Errors.TASK_STATUS_ERROR + TaskStatusEnum.getValue(taskInfo.getStatus()));
        }

        // TODO: 17-5-28
        return null;
    }
}
