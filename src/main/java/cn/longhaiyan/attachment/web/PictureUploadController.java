package cn.longhaiyan.attachment.web;

import cn.longhaiyan.attachment.utils.ExifUtil;
import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.*;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.BankMapping;
import cn.longhaiyan.common.utils.consts.Errors;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.attachment.domain.Attachment;
import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.attachment.utils.UploadUtil;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

import static cn.longhaiyan.common.utils.consts.BankConsts.PROTECTED_BASE_PATH;
import static cn.longhaiyan.common.utils.consts.BankConsts.PROTECTED_PIC_DISPOSE_PTTH;

/**
 * Created by chenxb on 17-4-2.
 */
@RestController
public class PictureUploadController extends CommonController {

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/picture/upload/project", method = RequestMethod.POST)
    public ResponseEntity uploadPicture(HttpServletRequest request) {

        Map<String, Object> map = UploadUtil.uploadPicture(request, BankMapping.PICTURE_PROJECT_LIST);
        if (CollectionUtil.isEmpty(map)) {
            return ResponseEntity.failure(Errors.UNKNOWN_ERROR);
        }
        String error = (String) map.get(BankConsts.FALSE);
        if (StringUtil.isNotEmpty(error)) {
            return ResponseEntity.failure(error);
        }
        Attachment attachment = (Attachment) map.get(BankConsts.TRUE);
        if (attachment == null) {
            return ResponseEntity.failure(Errors.UPLOAD_ERROR);
        }

        File file = new File(PROTECTED_BASE_PATH + attachment.getRelativePath());
        Map<String, Integer> size = ExifUtil.getPicSize(file);
        Integer width = size.get("width");
        Integer height = size.get("height");
        if (width == null || height == null || width <= 60 || height <= 60) {
            return ResponseEntity.failure(Errors.PICTURE_SIZE_ERROR);
        }

        UserSession userSession = getUserSession(request);
        int userId = userSession.getUserId();
        attachment.setUserId(userId);
        attachmentService.save(attachment);
        return ResponseEntity.success().set(BankConsts.DATA, attachment);
    }
}

