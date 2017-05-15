package cn.longhaiyan.common.web;

import cn.longhaiyan.common.bean.ResponseEntity;
import cn.longhaiyan.common.bean.UserSession;
import cn.longhaiyan.common.utils.StringUtil;
import cn.longhaiyan.common.utils.consts.BankMapping;
import cn.longhaiyan.common.utils.consts.Errors;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import static cn.longhaiyan.common.utils.consts.BankConsts.REDIS_STAT_TOTAL_KEY;
import static cn.longhaiyan.common.utils.consts.BankConsts.REDIS_TIMESTAMP_KEY;

/**
 * Created by chenxb on 17-3-5.
 */
public class BankBaseController extends RootController {

//    @Autowired
//    private RedisService redisService;

    @ModelAttribute("htmlTitle")
    public String getHtmlTitle() {
        return htmlTitle();
    }

    public static int STAT_TOTAL = 0;

    @ModelAttribute("statTotal")
    public int getStatTotal() {
        String key = REDIS_STAT_TOTAL_KEY;
//        String statTotal = redisService.get(key);
//        if (StringUtil.isEmpty(statTotal)) {
//            statTotal = "1";
//            redisService.set(key, statTotal);
//        }
//        int totalDB = Integer.parseInt(statTotal);
//        STAT_TOTAL = ++totalDB;
//        redisService.set(key, String.valueOf(STAT_TOTAL));
        return STAT_TOTAL;
    }

    @ModelAttribute("userRoleMap")
    public Map<Integer, String> getUserRoleMap() {

        return BankMapping.USER_ROLE_MAPPING;
    }

    @ModelAttribute("staticVersion")
    public String getStaticVersion() {
        String key = REDIS_TIMESTAMP_KEY;
//        String tbTimestamp = redisService.get(key);
//        if (StringUtil.isEmpty(tbTimestamp)) {
//            tbTimestamp = String.valueOf(System.currentTimeMillis());
//            redisService.set(key, tbTimestamp);
//        }
//        return tbTimestamp;
        return null;
    }

    @ModelAttribute("isLogin")
    public boolean isLogin(HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        if (userSession == null) {
            return false;
        }
        return true;
    }

    public ResponseEntity success(HttpServletRequest request) {
        return ResponseEntity.success();
    }

    public static ResponseEntity success(String message) {
        return ResponseEntity.success(message);
    }

    public ResponseEntity failure(HttpServletRequest request, String message) {
        return ResponseEntity.failure(message);
    }


    public static boolean isErrorPageSize(ModelMap mmap, int page, int size) {
        boolean error = false;
        if (page <= 0 || size <= 0) {
            mmap.addAttribute("message", Errors.FILE_TYPE_ERROR);
            error = true;
        }

        return error;
    }

    public String htmlTitle() {
        return "时间银行";
    }


    public boolean checkLogin() {
        return false;
    }

    public boolean isPasswordGood(String password) {
        if (StringUtil.isBlank(password)) {
            return false;
        }
        if (password.length() > 32 || password.length() < 6) {
//TODO           return false;
        }
        return true;
    }
}
