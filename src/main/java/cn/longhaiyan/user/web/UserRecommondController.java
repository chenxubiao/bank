package cn.longhaiyan.user.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.CollectionUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.web.GuestBaseController;
import cn.longhaiyan.user.bean.User;
import cn.longhaiyan.user.domain.UserInfo;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxb on 17-5-28.
 */
@RestController
public class UserRecommondController extends GuestBaseController {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/user/recommond/list/data", method = RequestMethod.GET)
    public ResponseEntity getUserList(HttpServletRequest request) {

        List<UserInfo> userInfoList = userInfoService.findRecommondUserList();
        List<User> userList = null;
        if (CollectionUtil.isNotEmpty(userInfoList)) {
            userList = new ArrayList<>();
            for (UserInfo userInfo : userInfoList) {
                userList.add(new User(userInfo, ""));
            }
        }
        return ResponseEntity.success().set(BankConsts.DATA, userList);
    }
}
