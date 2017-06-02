package cn.longhaiyan.task.util;

import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.task.bean.TaskIndexBean;
import cn.longhaiyan.task.domain.TaskFinish;
import cn.longhaiyan.task.domain.TaskInfo;

/**
 * Created by chenxb on 17-5-31.
 */
public class TaskUtil {
    /**
     * 加密
     * @param value
     * @return
     */
    public static String encrypt(String value) {
        if (StringUtil.isBlank(value)) {
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    /**
     * 是否加密
     * @param userId
     * @param taskInfo
     * @param taskFinish
     * @return
     */
    public static boolean isEncrypt(int userId, TaskInfo taskInfo, TaskFinish taskFinish) {
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

    public static TaskIndexBean getTaskIndexBean(TaskInfo taskInfo, TaskFinish taskFinish, int sessionUserId) {
        if (taskInfo == null) {
            return null;
        }
        boolean isEncrypt = isEncrypt(sessionUserId, taskInfo, taskFinish);
        TaskIndexBean taskIndexBean = new TaskIndexBean(taskInfo);
        if (isEncrypt) {
            taskIndexBean.setAddress(encrypt(taskInfo.getAddress()));
        }
        return taskIndexBean;
    }

}
