package cn.longhaiyan.attachment.service;


import cn.longhaiyan.attachment.domain.Attachment;

/**
 * Created by chenxb on 17-4-1.
 */
public interface AttachmentService {
    Attachment save(Attachment attachment);

    Attachment findById(int id);

    boolean isPictureExist(int id);
}
