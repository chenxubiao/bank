package cn.longhaiyan.user.repository;

import cn.longhaiyan.user.domain.AuthAttachment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenxb on 17-5-10.
 */
@Repository
public interface AuthAttachmentRepository extends PagingAndSortingRepository<AuthAttachment, Long> {


    List<AuthAttachment> findAllByUserId(int userId);

}
