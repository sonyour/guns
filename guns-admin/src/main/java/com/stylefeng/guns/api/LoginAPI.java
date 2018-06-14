package com.stylefeng.guns.api;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogManager;
import com.stylefeng.guns.core.log.factory.LogTaskFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.shiro.ShiroUser;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.stylefeng.guns.core.support.HttpKit.getIp;


@RestController
@RequestMapping("/api")
public class LoginAPI extends BaseController {

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public Map<String,String> loginVali(@RequestBody Map<String, String> params) {
        Map<String,String> result = new HashMap<>();
        String username = params.get("userName");
        String password = params.get("password");
        String type = params.get("type");
        //String username = super.getPara("userName").trim();
        //String password = super.getPara("password").trim();
        //String type = super.getPara("type");
        //String remember = super.getPara("remember");
        String remember = "";
        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);
        result.put("status","ok");
        result.put("type",type);
        result.put("currentAuthority",shiroUser.getAccount());
        return result;
    }
}