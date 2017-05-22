package cn.longhaiyan.user.service;

import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.user.domain.AuthAttachment;
import cn.longhaiyan.user.repository.AuthAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenxb on 17-5-11.
 */
@Service
public class AuthAttachmentServiceImpl implements AuthAttachmentService {
    @Autowired
    private AuthAttachmentRepository authAttachmentRepository;

    @Override
    public void save(List<AuthAttachment> authAttachmentList) {
        if (CollectionUtil.isEmpty(authAttachmentList)) {
            return;
        }
        authAttachmentRepository.save(authAttachmentList);
    }

    @Override
    public List<AuthAttachment> findByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }
        return authAttachmentRepository.findAllByUserId(userId);
    }

}
