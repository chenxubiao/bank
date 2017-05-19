package cn.longhaiyan.task.web;

import cn.longhaiyan.common.bean.Pagination;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.tag.service.TagInfoService;
import cn.longhaiyan.task.bean.TaskIndexBean;
import cn.longhaiyan.task.bean.TaskInfoBean;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.domain.TaskTag;
import cn.longhaiyan.task.enums.TaskInfoUrgentEnum;
import cn.longhaiyan.task.enums.TaskRequestTypeEnum;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.enums.UserTypeEnum;
import cn.longhaiyan.user.service.UserInfoService;
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
public class TaskInfoController extends GuestBaseController {

    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TagInfoService tagInfoService;
    @Autowired
    private UserInfoService userInfoService;


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
            status = TaskStatusEnum.REVEIVE.getCode();
        } else if (type == TaskRequestTypeEnum.DONE_RECEIVE.getCode()) {
            status = TaskStatusEnum.DONE_RECEIVE.getCode();
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
            boolean isEncrypt = isEncrypt(userId, taskInfo);
            if (isEncrypt) {
                taskInfo.setAddress(convertAddress(taskInfo.getAddress()));
            }
            List<TagInfo> tagInfos = getTagInfoList(taskInfo);

            TaskIndexBean taskIndexBean = new TaskIndexBean(taskInfo, tagInfos);
            taskIndexBeanList.add(taskIndexBean);
        }
        Pagination pagination = new Pagination(page.getTotalPages()
                , new Long(page.getTotalElements()).intValue(), pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.success().set(BankConsts.DATA, taskIndexBeanList).set(BankConsts.PAGINATION, pagination);
    }


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

        //是否加密
        boolean isEncrypt = isEncrypt(userId, taskInfo);

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

        //加密
        if (isEncrypt) {
            taskInfo.setAddress(convertAddress(taskInfo.getAddress()));
            name = convertString(name);
            taskInfo.setPersonal(convertString(taskInfo.getPersonal()));
            taskInfo.setUrgent(TaskInfoUrgentEnum.NO_URGENT.getCode());
            taskInfo.setUrgentMoney(BankConsts.ZERO);
            name = convertString(name);
            sender = 0;
        }
        taskInfo.setTaskComplete(null);
        List<TagInfo> tags = getTagInfoList(taskInfo);
        taskInfo.setTags(tags);

        TaskInfoBean taskInfoBean = new TaskInfoBean(taskInfo, name, sender);

        return ResponseEntity.success().set(BankConsts.DATA, taskInfoBean);
    }


    private String convertAddress(String address) {
        if (StringUtil.isBlank(address)) {
            return address;
        }
        if (address.length() >= 2) {
            int length = address.length();
            int entry = length / 2;
            StringBuilder addr = new StringBuilder(address.substring(0, entry));
            for (int i = 0; i < entry; i++) {
                addr.append("*");
            }
            address = addr.toString();
        } else {
            address = "*";
        }
        return address;
    }

    private String convertString(String userName) {
        if (StringUtil.isBlank(userName)) {
            return userName;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < userName.length(); i++) {
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @param userId
     * @param taskInfo  非空
     * @return
     */
    private boolean isEncrypt(int userId, TaskInfo taskInfo) {
        //是否加密
        boolean isEncrypt = true;
        if (userId > 0 && userId == taskInfo.getUserId()) {
            //此时为需求发布方
            isEncrypt = false;
        } else if (userId > 0 && taskInfo.getTaskComplete() != null
                && userId == taskInfo.getTaskComplete().getReceiver()) {
            //此时为需求接收方
            isEncrypt = false;
        }
        return isEncrypt;
    }

    private List<TagInfo> getTagInfoList(TaskInfo taskInfo) {
        List<TagInfo> tagInfos = null;
        if (taskInfo != null && CollectionUtil.isNotEmpty(taskInfo.getTaskTags())) {
            tagInfos = new ArrayList<>();
            for (TaskTag taskTag : taskInfo.getTaskTags()) {
                TagInfo tagInfo = tagInfoService.findById(taskTag.getTagId());
                if (tagInfo != null) {
                    tagInfos.add(tagInfo);
                }
            }
        }
        return tagInfos;
    }
}
