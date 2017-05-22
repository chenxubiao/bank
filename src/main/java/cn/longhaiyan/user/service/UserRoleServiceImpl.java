package cn.longhaiyan.user.service;

import cn.longhaiyan.user.domain.UserRole;
import cn.longhaiyan.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxb on 17-4-1.
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void save(UserRole userRole) {
        if (userRole != null) {
            userRoleRepository.save(userRole);
        }
    }

    @Override
    public int countByRoleId(int roleId) {
        if (roleId <= 0) {
            return 0;
        }
        return userRoleRepository.countByRoleId(roleId);
    }
}
