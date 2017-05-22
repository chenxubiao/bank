package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.task.bean.TaskFinishBean;
import cn.longhaiyan.task.bean.TaskInfoBean;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.service.TaskFinishService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.user.bean.User;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-22.
 */
@RestController
public class TaskDetailController extends GuestBaseController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TaskFinishService taskFinishService;


    /**
     * 根据 taskId 获取任务详情
     * @param request
     * @param taskId    任务id
     * @return
     */
    @RequestMapping(value = "/task/info/data", method = RequestMethod.GET)
    public ResponseEntity getTaskDetailData(HttpServletRequest request,
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
        if (taskInfo == null) {
            return ResponseEntity.failure(Errors.TASK_NOT_FOUNT);
        }
        UserInfo senderInfo = userInfoService.findById(taskInfo.getUserId());
        String name;
        int sender = senderInfo.getId();
        if (senderInfo.getUserType() == UserTypeEnum.STUDENT.getCode()) {
            if (senderInfo.getStudent() == null) {
                return ResponseEntity.failure(Errors.TASK_SENDER_ERROR);
            }
            name = senderInfo.getStudent().getName();
        } else if (senderInfo.getUserType() == UserTypeEnum.TEACHER.getCode()) {
            if (senderInfo.getTeacher() == null) {
                return ResponseEntity.failure(Errors.TASK_SENDER_ERROR);
            }
            name = senderInfo.getTeacher().getName();
        } else {
            return ResponseEntity.failure(Errors.TASK_SENDER_ERROR);
        }
        TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
        User taker = null;
        UserInfo takerInfo = null;
        //是否加密
        boolean isEncrypt = isEncrypt(userId, taskInfo, taskFinish);

        if (taskFinish != null) {
            takerInfo = userInfoService.findById(taskFinish.getTakerId());;
            if (isEncrypt) {
                taskFinish.setUserId(0);
                taskFinish.setTakerId(0);
                taskFinish.setRemark(StringUtil.convertEncrypt(taskFinish.getRemark()));
            }
        }
        if (takerInfo != null) {
            taker = new User(takerInfo, getName(takerInfo));
        }
        //加密
        User puber = new User();
        if (isEncrypt) {
            if (taker != null) {
                taker.setName(StringUtil.convertEncrypt(taker.getName()));
                taker.setId(BankConsts.ZERO);
                taker.setUserName(StringUtil.convertEncrypt(taker.getUserName()));
            }
            puber.setName(StringUtil.convertEncrypt(name));
        } else {
            puber.setId(sender);
            puber.setName(name);
            puber.setUserName(senderInfo.getUserName());
        }
        TaskInfoBean taskInfoBean = new TaskInfoBean(taskInfo, puber, taker, isEncrypt);
        TaskFinishBean taskFinishBean = null;
        if (taskFinish != null) {
            taskFinishBean = new TaskFinishBean(taskFinish, puber, taker);
        }
        return ResponseEntity.success().set(BankConsts.DATA, taskInfoBean).set("finishInfo", taskFinishBean);
    }

    /**
     * @param userId
     * @param taskInfo 非空
     * @return
     */
    private boolean isEncrypt(int userId, TaskInfo taskInfo, TaskFinish taskFinish) {
        //是否加密
        boolean isEncrypt = true;
        if (userId > 0 && userId == taskInfo.getUserId()) {
            //此时为需求发布方
            isEncrypt = false;
        } else if (userId > 0 && taskInfo.getFinishId() > 0
                && taskFinish != null && taskFinish.getTakerId() == userId) {

            //此时为需求接收方
            isEncrypt = false;
        }
        return isEncrypt;
    }
}
