package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.UserInfo;

import java.util.List;

/**
 * Created by chenxb on 17-4-1.
 */
public interface UserInfoService {
    UserInfo findByEmail(String email);

    UserInfo findByCellphone(String cellphone);

    UserInfo findByUserName(String userName);

    UserInfo save(UserInfo userInfo);

    boolean isEmailExist(String email);

    boolean isCellphoneExist(String cellphone);

    boolean isUserNameExist(String userName);

    UserInfo findById(int id);

    UserInfo findByIdAndNormal(int id);

    int countAll();

    List<UserInfo> findByUserTypeIn(List<Integer> userTypeList);

    List<UserInfo> findRecommondUserList();
}
