package cn.longhaiyan.task.web;

import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.task.bean.TaskBean;
import cn.longhaiyan.task.bean.TaskFinishBean;
import cn.longhaiyan.task.bean.TaskInfoBean;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.service.TaskFinishService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.task.service.TaskTagService;
import cn.longhaiyan.user.bean.User;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chenxb on 17-5-20.
 */
@RestController
public class TaskInfoController extends CommonController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;
    @Autowired
    private UserInfoService userInfoService;


    /**
     * 获取用户历史任务信息列表、发布任务数、接单数
     *
     * @return
     */
    @Authority(privilege = BankConsts.UserRole.USER_IS_STUDENT + "," + BankConsts.UserRole.USER_IS_TEACHER)
    @RequestMapping(value = "/task/user/list/data", method = RequestMethod.GET)
    public ResponseEntity findTaskLog(HttpServletRequest request) {

        UserSession userSession = super.getUserSession(request);
        int userId = userSession.getUserId();
        int taskCount = taskInfoService.countByUserId(userId);
        int takeCount = taskFinishService.countByTakerId(userId);
        List<TaskInfo> taskInfoList = null;
        List<TaskInfo> taskTakeList = null;
        if (taskCount > 0) {
            taskInfoList = taskInfoService.findByUserId(userId);
        }
        if (takeCount > 0) {
            List<TaskFinish> taskFinishList = taskFinishService.findByTakerId(userId);
            Set<Integer> taskIds = null;
            if (CollectionUtil.isNotEmpty(taskFinishList)) {
                taskIds = new HashSet<>();
                for (TaskFinish taskFinish : taskFinishList) {
                    taskIds.add(taskFinish.getTaskId());
                }
            }
            if (CollectionUtil.isNotEmpty(taskIds)) {
                taskTakeList = taskInfoService.findByIdIn(new ArrayList<>(taskIds));
            }
        }
        List<TaskBean> taskBeanList = getTaskBeanList(taskInfoList);
        List<TaskBean> taskBeanTakeList = getTaskBeanList(taskTakeList);

        return ResponseEntity.success()
                .set(BankConsts.DATA, taskBeanList)
                .set("taskNum", taskCount)
                .set("takeNum", takeCount)
                .set("takeTask", taskBeanTakeList);
    }

    private List<TaskBean> getTaskBeanList(List<TaskInfo> taskInfoList) {
        List<TaskBean> taskBeanList = null;
        if (CollectionUtil.isNotEmpty(taskInfoList)) {
            taskBeanList = new ArrayList<>();
            for (TaskInfo taskInfo : taskInfoList) {
                UserInfo senderInfo = userInfoService.findById(taskInfo.getUserId());
                String senderName;
                String takerName;
                senderName = getName(senderInfo);
                TaskFinish taskFinish = taskFinishService.findById(taskInfo.getFinishId());
                TaskBean taskBean;
                if (taskFinish == null || taskFinish.getTakerId() == 0) {
                    taskBean = new TaskBean(new TaskInfoBean(taskInfo, new User(senderInfo, senderName), null, false), null);
                } else {
                    UserInfo finisherInfo = userInfoService.findById(taskFinish.getTakerId());
                    if (finisherInfo == null) {
                        continue;
                    }
                    takerName = getName(finisherInfo);
                    taskBean = new TaskBean(new TaskInfoBean(taskInfo, new User(senderInfo, senderName), new User(finisherInfo, takerName), false)
                            , new TaskFinishBean(taskFinish, new User(senderInfo, senderName), new User(finisherInfo, takerName)));
                }
                taskBeanList.add(taskBean);
            }
        }
        return taskBeanList;
    }
}
