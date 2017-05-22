package cn.longhaiyan.user.service;


import cn.longhaiyan.user.domain.UserRole;

/**
 * Created by chenxb on 17-4-1.
 */
public interface UserRoleService {

    void save(UserRole userRole);

    int countByRoleId(int roleId);
}