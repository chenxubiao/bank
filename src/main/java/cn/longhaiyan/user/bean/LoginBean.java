package cn.longhaiyan.user.bean;

import cn.longhaiyan.common.utils.consts.Errors;
import javax.validation.constraints.Size;

/**
 * Created by chenxb on 17-3-7.
 */
public class LoginBean {
    @Size(min = 1, max = 32, message = Errors.USER_USERNAME_NULL)
    private String name;
    @Size(min = 1, max = 32, message = Errors.LOGIN_PASSWORD_NULL_ERROR)
    private String password;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
