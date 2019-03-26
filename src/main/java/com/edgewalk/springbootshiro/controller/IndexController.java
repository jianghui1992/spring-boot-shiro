package com.edgewalk.springbootshiro.controller;

import com.edgewalk.springbootshiro.security.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by: edgewalk
 * 2019-03-26 21:44
 */
@Controller
public class IndexController {
    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "hello";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping(value = "subLogin")
    @ResponseBody
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "用户名或密码错误";
        }
        return "登录成功";
    }
}
