package com.edgewalk.springbootshiro;

import com.edgewalk.springbootshiro.security.CustomCredentialsMatcher;
import com.edgewalk.springbootshiro.security.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

public class ShiroTest {

    public static void main(String[] args) {
//        //从文件获取realm
//        IniRealm realm = new IniRealm("classpath:shiro.ini");
//        //默认内置的内存realm
//        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
//        simpleAccountRealm.addAccount("admin","admin");


        // 1创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        CustomRealm realm = new CustomRealm();
        //自定义密码加密器,也就是数据库存的是加密后的密码,安全
        realm.setCredentialsMatcher(new CustomCredentialsMatcher());
        securityManager.setRealm(realm);
        // subject对象,都是由SecurityManager管理的,获取的时候,默认是绑定当前线程的!!!
        SecurityUtils.setSecurityManager(securityManager);
        // 3获取subject对象
        Subject subject = SecurityUtils.getSubject();
        // 4做认证
        subject.login(new UsernamePasswordToken("admin", "admin"));
        //如果步骤4认证失败,会报异常
        //如果步骤4认证成功,那么就会打印认证成功
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        //subject.checkRole("admin1");
        //subject.checkPermissions("user:add", "user:delete");
    }


}