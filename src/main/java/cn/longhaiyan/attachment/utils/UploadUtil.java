package cn.longhaiyan.attachment.utils;

import cn.longhaiyan.attachment.domain.Attachment;
import cn.longhaiyan.common.utils.*;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.longhaiyan.common.utils.consts.BankConsts.PROTECTED_BASE_PATH;


/**
 * Created by chenxb on 17-4-27.
 */
public class UploadUtil {

    public static Map<String, Object> uploadPicture(HttpServletRequest request, List<String> permitExtList) {

        Map<String, Object> map = new HashMap<>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (CollectionUtil.isEmpty(fileMap)) {
                map.put(BankConsts.FALSE, Errors.UNKNOWN_ERROR);
                return map;
            }
            MultipartFile multipartFile = fileMap.get(BankConsts.UPLOAD_NAME);
            if (multipartFile == null || multipartFile.isEmpty()) {
                map.put(BankConsts.FALSE, Errors.UNKNOWN_ERROR);
                return map;
            }
            String fileName = WebUtil.escapeHtml(getfileNameNoExt(multipartFile.getOriginalFilename()));
            if (StringUtil.isNotBlank(fileName)) {
                if (fileName.length() > BankConsts.PICTURE_UPLOAD_NAME_LEN) {
                    fileName = fileName.substring(0, BankConsts.PICTURE_UPLOAD_NAME_LEN);
                }
            } else {
                map.put(BankConsts.FALSE, Errors.FILE_NAME_ERROR);
                return map;
            }
            String ext = FileUtil.getExtension(multipartFile.getOriginalFilename());
            if (StringUtil.isEmpty(ext) || !permitExtList.contains(ext.toLowerCase())) {
                map.put(BankConsts.FALSE, Errors.FILE_EXT_TYPE_ERROR + ext);
                return map;
            }
            long length = multipartFile.getSize();
            if (length > BankConsts.PICTURE_UPLOAD_MAX_SIZE) {
                map.put(BankConsts.FALSE, Errors.FILE_LENGTH_TOO_LOGN);
                return map;
            }
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setExt(ext);
            String filePath = new SimpleDateFormat(ConstStrings.DATE_PATTERN).format(new Date());
            String uid = String.valueOf(NumberUtil.random(8));
            attachment.setFilePath(filePath);
            attachment.setUid(uid);
            attachment.setCreateTime(new Date());
            attachment.setModifyTime(attachment.getCreateTime());
            attachment.setLength(length);
            File baseFile = new File(PROTECTED_BASE_PATH + attachment.getRelativePath());
            FileUtil.createDirs(baseFile);
            try {
                multipartFile.transferTo(baseFile);
            } catch (IOException e) {
                e.printStackTrace();
                map.put(BankConsts.FALSE, Errors.UNKNOWN_ERROR);
                return map;
            }
            boolean isImage = ImageUtil.checkImage(baseFile.toPath());
            if (!isImage) {
                baseFile.delete();
                System.out.println("not image");
                map.put(BankConsts.FALSE, Errors.FILE_TYPE_ERROR);
                return map;
            }
            map.put(BankConsts.TRUE, attachment);
            return map;
        }
        System.out.println("request not instanceof MultipartHttpServletRequest");
        map.put(BankConsts.FALSE, Errors.FILE_TYPE_ERROR);
        return map;
    }

    private static String getfileNameNoExt(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }
}
