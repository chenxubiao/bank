package cn.longhaiyan.task.web;

import cn.longhaiyan.account.domain.Account;
import cn.longhaiyan.account.domain.AccountLog;
import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.account.service.AccountLogService;
import cn.longhaiyan.account.service.AccountService;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.NumberUtil;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.message.domain.Message;
import cn.longhaiyan.message.enums.MessageTypeEnum;
import cn.longhaiyan.message.service.MessageService;
import cn.longhaiyan.tag.domain.TagInfo;
import cn.longhaiyan.tag.service.TagInfoService;
import cn.longhaiyan.task.bean.TaskInfoBean;
import cn.longhaiyan.task.domain.TaskInfo;
import cn.longhaiyan.task.domain.TaskLog;
import cn.longhaiyan.task.domain.TaskTag;
import cn.longhaiyan.task.enums.TaskInfoUrgentEnum;
import cn.longhaiyan.task.enums.TaskStatusEnum;
import cn.longhaiyan.task.service.TaskTagService;
import cn.longhaiyan.task.service.TaskInfoService;
import cn.longhaiyan.task.service.TaskLogService;
import cn.longhaiyan.user.enums.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by chenxb on 17-5-17.
 */
@RestController
public class TaskPublishController extends CommonController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private TaskTagService taskTagService;
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TagInfoService tagInfoService;


    @RequestMapping(value = "/task/publish/data", method = RequestMethod.POST)
    public ResponseEntity publisTask(HttpServletRequest request, TaskInfoBean task) {
        if (StringUtil.isBlank(task.getTitle())
                || StringUtil.isBlank(task.getDescription())
                || StringUtil.isBlank(task.getTagIds())
                || TaskInfoUrgentEnum.isNotContain(task.getUrgent())
                || StringUtil.isBlank(task.getAddress())
                || task.getDeadTime() == null
                || task.getMoney() <= 0) {

            return ResponseEntity.failure(Errors.PARAMETER_ILLEGAL);
        }
        UserSession userSession = super.getUserSession(request);
        if (UserTypeEnum.isNotEnjoyTask(userSession.getUserType())) {
            return ResponseEntity.failure(Errors.USER_NOT_AUTH);
        }
        if (new Date().compareTo(task.getDeadTime()) <= 0) {
            return ResponseEntity.failure(Errors.TASK_DEADTIME_ERROR);
        }
        int userId = userSession.getUserId();
        int money = task.getMoney();
        int urgentMoney = task.getUrgentMoney();
        Account account = accountService.findByUserId(userId);
        if (account.getTotalMoney() < task.getMoney()) {
            return ResponseEntity.failure(Errors.ACCOUNT_BALANCE);
        }

        if (task.getUrgent() == TaskInfoUrgentEnum.URGENT.getCode()) {
            if (task.getMoney() < task.getUrgentMoney() || task.getUrgentMoney() <= 0) {
                return ResponseEntity.failure(Errors.TASK_MONEY_URGENT_ERROR);
            }
            if (urgentMoney > account.getTotalMoney()) {
                return ResponseEntity.failure(Errors.ACCOUNT_BALANCE);
            }

        }
        TaskInfo taskInfo = new TaskInfo(task);
        taskInfo.setUserId(userId);
        taskInfo.setCreateTime(new Date());
        taskInfo.setModifyTime(taskInfo.getCreateTime());
        taskInfoService.save(taskInfo);

        String title = taskInfo.getTitle();
        TaskLog taskLog = new TaskLog(TaskStatusEnum.PUBLISH.getCode(), userId, taskInfo);
        taskLog.setModifyTime(taskLog.getCreateTime());
        taskLogService.save(taskLog);
        taskInfo.addTaskLog(taskLog);

        Message message = new Message
                (MessageTypeEnum.TASK_PUBLISH.getCode(), BankConsts.USER_IS_SYSTEM, userId, taskLog.getId(), title);
        message.setModifyTime(new Date());
        messageService.save(message);

        account.setTotalMoney(account.getTotalMoney() - money);
        AccountLog taskAccountLog = new AccountLog
                (userId, AccountLogTypeEnum.DEL_TASK_PUB.getCode(), money, taskInfo.getId(), title, account);

        taskAccountLog.setBalance(account.getTotalMoney());
        accountLogService.save(taskAccountLog);
        Message pubMsg = new Message
                (MessageTypeEnum.ACCOUNT_CHANGE.getCode(), BankConsts.USER_IS_SYSTEM, userId
                        , taskAccountLog.getId(), taskAccountLog.getMessage());

        message.setModifyTime(new Date());
        messageService.save(pubMsg);

        if (task.getUrgent() == TaskInfoUrgentEnum.URGENT.getCode()) {
            account.setTotalMoney(account.getTotalMoney() - urgentMoney);

            AccountLog urgentAccountLog = new AccountLog(userId
                    , AccountLogTypeEnum.DEL_TASK_URGENT.getCode()
                    , urgentMoney, taskInfo.getId(), title, account);
            urgentAccountLog.setBalance(account.getTotalMoney());
            accountLogService.save(urgentAccountLog);

            Message urgentMsg = new Message
                    (MessageTypeEnum.ACCOUNT_CHANGE.getCode(), BankConsts.USER_IS_SYSTEM, userId
                            , urgentAccountLog.getId(), urgentAccountLog.getMessage());
            urgentMsg.setModifyTime(urgentMsg.getCreateTime());
            messageService.save(urgentMsg);
        }
        accountService.save(account);

        String tagIds = task.getTagIds();
        if (StringUtil.isNotBlank(tagIds)) {
            String[] tagStrings = tagIds.split(",");
            Set<Integer> tagIdSet = new HashSet<>();
            for (String tagId : tagStrings) {
                if (StringUtil.isNotBlank(tagId)) {
                    if (NumberUtil.is(tagId)) {
                        int id = NumberUtil.parseIntQuietly(tagId.trim());
                        if (id > 0) {
                            TagInfo tagInfo = tagInfoService.findById(id);
                            if (tagInfo != null) {
                                tagIdSet.add(id);
                            }
                        }
                    } else {
                        String tagName = tagId.trim();
                        if (StringUtil.isBlank(tagName)) {
                            continue;
                        }
                        TagInfo tagInfo = new TagInfo();
                        tagInfo.setName(tagName);
                        tagInfo.setCreateTime(new Date());
                        tagInfo.setModifyTime(tagInfo.getCreateTime());
                        tagInfoService.save(tagInfo);
                        tagIdSet.add(tagInfo.getId());
                    }
                }
            }
            if (tagIdSet.size() > 0) {
                List<TaskTag> taskTags = new ArrayList<>();
                for (int id : tagIdSet) {
                    TaskTag taskTag = new TaskTag(id, userId, taskInfo);
                    taskTag.setModifyTime(taskTag.getCreateTime());
                    taskTags.add(taskTag);
                }
                taskTagService.save(taskTags);
                taskInfo.setTaskTags(taskTags);
            }
        }

        return ResponseEntity.success().set(BankConsts.DATA, taskInfo);
    }
}
