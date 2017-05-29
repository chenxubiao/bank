package cn.longhaiyan.common.interceptor;

import cn.longhaiyan.common.annotation.Authority;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.ConstStrings;
import cn.longhaiyan.common.utils.NumberUtil;
import cn.longhaiyan.common.utils.consts.BankConsts;
import cn.longhaiyan.common.utils.consts.Errors;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by chenxb on 17-3-4.
 */
@Component
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    @Override
    public final boolean preHandle(HttpServletRequest request,
                                   HttpServletResponse response, Object handler) throws Exception{

        Authority annotation = null;
        if(handler instanceof HandlerMethod){
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Authority.class);
        }else{
            return true;
        }
        if (annotation != null) {
            String privilege = annotation.privilege();
            Set<Integer> privilegeSet = null;
            if (privilege.length() > 0) {
                privilegeSet = NumberUtil.parseToIntSet(privilege);
            }

            if (privilegeSet != null && privilegeSet.size() >0 && annotation.checkAuth()) {
                UserSession session =  (UserSession)request.getSession().getAttribute(BankConsts.USER_SESSION_KEY);
                if(session.isAdmin()){
                    return true;
                }
                Set<Integer> roleSet = session.getRoleSet();
                if (roleSet != null && roleSet.size() > 0) {
                    for (int id : privilegeSet) {
                        if (roleSet.contains(id)) {
                            return true;
                        }
                    }
                }
                sendMessage(response, Errors.JSON_NOT_HAVE_ROLES);
                return false;
            }
        }
        return true;
    }
    private void sendMessage(HttpServletResponse response, String message) {

        response.setContentType(ConstStrings.CONTENT_TYPE_JSON);
        response.setCharacterEncoding(ConstStrings.CHARACTER_ENCOING_UTF8);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
