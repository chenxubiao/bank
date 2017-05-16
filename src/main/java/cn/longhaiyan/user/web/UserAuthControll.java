package cn.longhaiyan.user.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.web.CommonController;
import cn.longhaiyan.user.bean.AuthBean;
import cn.longhaiyan.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenxb on 17-5-16.
 */
@RestController
public class UserAuthControll extends CommonController {
    @Autowired
    private UserInfoService userInfoService;

    public ResponseEntity authUser(HttpServletRequest request, AuthBean authBean) {
        if (authBean == null
                || StringUtil.isBlank(authBean.getAttachment())
                || StringUtil.isBlank(authBean.getName())
                || StringUtil.isBlank(authBean.getDept())
                || StringUtil.isBlank(authBean.getMajor())
                || authBean.getNo()<=0) {

        }
        // TODO: 17-5-16  
        return null;
    }
}
