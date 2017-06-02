package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.Pagination;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.task.bean.TaskIndexBean;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.enums.TaskRequestTypeEnum;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.service.TaskFinishService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.task.util.TaskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class TaskIndexController extends GuestBaseController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskFinishService taskFinishService;


    /**
     * 获取首页 task 列表数据
     * @param request
     * @param pageable  分页 page、size
     * @param type      请求类型：0：发布中，1：服务中，2：已完成
     * @return
     */
    @RequestMapping(value = "/task/index/data", method = RequestMethod.GET)
    public ResponseEntity getTaskList(HttpServletRequest request, Pageable pageable
            , @RequestParam(value = "type", defaultValue = "0") int type) {

        if (TaskRequestTypeEnum.isNotContain(type)) {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }

        Page<TaskInfo> page;
        int status;
        if (type == TaskRequestTypeEnum.PUBLISHING.getCode()) {
            status = TaskStatusEnum.PUBLISH.getCode();
        } else if (type == TaskRequestTypeEnum.SERVICEING.getCode()) {
            status = TaskStatusEnum.RECEIVE.getCode();
        } else if (type == TaskRequestTypeEnum.DONE_RECEIVE.getCode()) {
            status = TaskStatusEnum.DONE_TASK.getCode();
        } else {
            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        page = taskInfoService.findByStatus(status, pageable);
        if (CollectionUtil.isEmpty(page.getContent())) {
            return ResponseEntity.failure(Errors.TASK_PUBLISHING_NONE);
        }

        UserSession userSession = super.getUserSession(request);
        int userId = userSession == null ? 0 : userSession.getUserId();

        List<TaskIndexBean> taskIndexBeanList = new ArrayList<>();
        List<TaskInfo> taskInfoList = page.getContent();
        for (TaskInfo taskInfo : taskInfoList) {
            taskInfoService.setTaskTags(taskInfo);
            TaskFinish taskFinish = null;
            if (type != TaskRequestTypeEnum.PUBLISHING.getCode()) {
                taskFinish = taskFinishService.findById(taskInfo.getFinishId());
            }
            taskIndexBeanList.add(TaskUtil.getTaskIndexBean(taskInfo, taskFinish, userId));
        }
        Pagination pagination = new Pagination(page.getTotalPages()
                , new Long(page.getTotalElements()).intValue(), pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.success().set(BankConsts.DATA, taskIndexBeanList).set(BankConsts.PAGINATION, pagination);
    }
}
