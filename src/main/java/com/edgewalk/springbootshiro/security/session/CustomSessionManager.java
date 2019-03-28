package com.edgewalk.springbootshiro.security.session;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * Created by: edgewalk
 * 2019-03-28 22:28
 */
@Slf4j
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 获取session对象
     * @param key
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey key) throws UnknownSessionException {
        Serializable sessionId = super.getSessionId(key);
        ServletRequest request = WebUtils.getRequest(key);
        //把session放在request对象中,防止多次从redis中读取,
        if (request != null && sessionId != null) {

            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }


        //如果从request获取不到,那么我们从自定义的RedisSessionDao中获取一份,然后放在request中,减少redis交互
        Session session = super.retrieveSession(key);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    /**
     * shiro是通过request获取到sessionid,然后通过sessionid判断用户有没有session,是否登录
     *
     * 自定义获取session的方法,用户可以防在参数或者header中
     * @param request
     * @param response
     * @return
     */
    @Override
    public Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取参数中的token
        String token = request.getParameter("token");
        // 获取请求头中的session
        token = token == null ? WebUtils.toHttp(request).getHeader("token") : token;
        return token;
    }


}
