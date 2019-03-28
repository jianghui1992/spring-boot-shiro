package com.edgewalk.springbootshiro.controller;

import com.edgewalk.springbootshiro.security.ApiResponse;
import com.edgewalk.springbootshiro.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
@Slf4j
public class IndexController {
    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "hello:" + SecurityUtils.getSubject().getPrincipal().toString();
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping(value = "subLogin")
    @ResponseBody
    public ApiResponse subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
        } catch (Exception e) {
            log.info("用户登录异常:{}", e);
            return ApiResponse.error(ApiResponse.Status.NOT_LOGIN);
        }
        return ApiResponse.success(subject.getSession().getId().toString());
    }

    //@RequiresRoles("admin")
    @GetMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "testRole success!";
    }

    //@RequiresRoles("admin1")
    @GetMapping("/testRole1")
    @ResponseBody
    public String testRole1() {
        return "testRole1 success!";
    }

}
