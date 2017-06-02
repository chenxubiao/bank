package cn.longhaiyan.illegal.repository;

import cn.longhaiyan.illegal.domain.IllegalInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chenxb on 17-5-31.
 */
@Repository
public interface IllegalInfoRepository extends PagingAndSortingRepository<IllegalInfo, Long> {

}
