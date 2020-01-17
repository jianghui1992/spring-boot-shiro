package com.edgewalk.springbootshiro.controller;

import com.edgewalk.springbootshiro.security.ApiResponse;
import com.edgewalk.springbootshiro.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description use for login
 * @Author jianghui
 * @Date 2020/1/17 15:42
 * @Version 1.0
 */
@Slf4j
public class LoginController {

    @PostMapping(value = "subLogin")
    @ResponseBody
    public ApiResponse subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
        } catch (Exception e) {
            log.error("用户登录异常:{}", e.getMessage(),e);
            return ApiResponse.error(ApiResponse.Status.NOT_LOGIN);
        }
        return ApiResponse.success(subject.getSession().getId().toString());
    }
}
