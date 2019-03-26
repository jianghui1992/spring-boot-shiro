package com.edgewalk.springbootshiro.security;

import com.google.common.collect.Sets;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: edgewalk
 * 2019-03-26 22:29
 */
public class CustomRealm extends AuthorizingRealm {
    Map<String, String> usermap = new HashMap<String, String>();

    {
        usermap.put("admin", "3ef7164d1f6167cb9f2658c07d3c2f0a");
    }

    /**
     * 授权,需要配合shiro的标签使用
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取认证通过的用户对象
        //就是我们调用doGetAuthenticationInfo方法后存入simpleAuthenticationInfo对象的第一个参数
        String username = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或者缓存获取角色信息
        Set<String> roles = getRoleByUsername(username);
        //从数据库或者缓存获取菜单权限信息
        Set<String> permissions = roles.stream().flatMap(role -> getPermissionSByRole(role).stream()).collect(Collectors.toSet());

        //返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);
        info.addRoles(roles);
        return info;
    }

    /**
     * 认证 通过页面传过来的用户名,查询数据库,把数据封装到AuthenticationInfo对象中,返回
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1 .从主体传过来的认证信息中,获得用户名
        String username = authenticationToken.getPrincipal().toString();
        //通过用户名到数据库中获取凭证
        String password = getPasswordByUsername(username);
        if (StringUtils.isEmpty(password)) {
            return null;
        }
        /**
         * principal: 存入的user对象,当认证通过了,获取到user对象的.获取到realm域,从realm域中获取user对象
         * credentials:存入的密码(数据库中的密码)
         * realmName:我们给realm域的名字
         */
        // 当用户调用subject.login(token)时,
        // SecurityManager会获取到SimpleAuthenticationInfo中的password然后和token中的password对比,如果通过就登录成功
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("test", password, "customerRealm");
        return simpleAuthenticationInfo;
    }

    /**
     * 模拟数据库操作,通过用户名获取密码
     *
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        return usermap.get(username);
    }


    private Set<String> getPermissionSByRole(String role) {
        return Sets.newHashSet("user:add", "user:delete");
    }

    /**
     * 模拟数据库操作,根据用户名获取用户角色信息
     *
     * @param username
     * @return
     */
    private Set<String> getRoleByUsername(String username) {
        return Sets.newHashSet("admin", "user");
    }


}
