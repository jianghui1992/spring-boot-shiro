https://blog.csdn.net/u013615903/article/details/78781166
https://blog.csdn.net/palerock/article/details/73457415


## 问题1:如何获取到当前登录用户
如果登陆成功,后期可以通过SecurityUtils.getSubject().getPrincipal()获取到我们在SimpleAuthenticationInfo放入的第一个参数


## 问题1: 通过注解进行权限限制
bugs点:
 当通过这种方式判断角色或者权限时,
  filterChainDefinitionMap.put("/testRole", "roles[\"admin1\"]");
  需要配合使用
    //未授权界面
    shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorizedUrl");
   负责接收不到异常,后台也不报错
   
   
## shiro除了使用预定于的filter,还可以自定义filter

## httpbasic认证的示例
https://blog.csdn.net/kkdelta/article/details/28419625

## shiro会话管理

### sessionManager SessionDao

### session on redis

