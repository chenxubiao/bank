package cn.longhaiyan.attachment.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.ConstStrings;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.attachment.domain.Attachment;
import cn.longhaiyan.attachment.service.AttachmentService;
import cn.longhaiyan.attachment.utils.DownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by chenxb on 17-4-28.
 */
@Controller
public class PictureDownloadController extends CommonController {
    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value = "/attachment/download", method = RequestMethod.GET)
    public ResponseEntity downloadattachment(@RequestParam(value = "id", defaultValue = "0") int id,
                                          HttpServletResponse response) {

        if (id <= 0) {
            return null;
        }
        Attachment attachment = attachmentService.findById(id);
        if (attachment == null) {
            return null;
        }
        String relativePath = BankConsts.PROTECTED_BASE_PATH + attachment.getRelativePath();
        response.setContentType(ConstStrings.CONTENT_TYPE_DOWNLOAD);//图片下载
        DownloadUtil.downloadPicture(response, relativePath);
        return null;
    }
}
