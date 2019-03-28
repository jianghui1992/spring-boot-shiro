package com.edgewalk.springbootshiro.security.session;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis保存session.默认是保存在内存中,默认类 {@link MemorySessionDAO}
 * Created by: edgewalk
 * 2019-03-28 21:12
 */
@Slf4j
public class RedisSessionDao extends AbstractSessionDAO {


    private static final String shiro_session_prefix = "boot-session:";

    @Value("forever.token")
    private Set<String> foreverToken;

    @Autowired
    private RedisTemplate<String, Session> redisTemplate;


    private String getKey(String key) {
        return shiro_session_prefix + key;
    }

    @Override
    protected Serializable doCreate(Session session) {
        //生成一个sessionid,然后和session绑定
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisTemplate.opsForValue().set(getKey(sessionId.toString()), session, TimeUnit.SECONDS.toMinutes(10));
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.info("doReadSession");
        if (sessionId == null) return null;
        //自定义永久session
        if (sessionId.toString().equals("123456")) {
            Session session = SecurityUtils.getSecurityManager().start(new DefaultSessionContext());
//            SimpleSession session = new SimpleSession();
            ((SimpleSession) session).setId(sessionId);
            update(session);
            return session;
        }

        return redisTemplate.opsForValue().get(getKey(sessionId.toString()));
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (Objects.nonNull(session) && Objects.nonNull(session.getId()))
            redisTemplate.opsForValue().set(getKey(session.getId().toString()), session, TimeUnit.SECONDS.toMinutes(10));
    }

    @Override
    public void delete(Session session) {
        redisTemplate.delete(getKey(session.getId().toString()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisTemplate.keys(shiro_session_prefix);
        if (CollectionUtils.isEmpty(keys)) {
            return Lists.newArrayList();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
