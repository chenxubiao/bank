package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.AuthAttachment;

import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
public interface AuthAttachmentService {
    void save(List<AuthAttachment> authAttachmentList);


    List<AuthAttachment> findByUserId(int userId);

}
