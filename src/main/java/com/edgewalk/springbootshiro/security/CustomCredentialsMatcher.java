package com.edgewalk.springbootshiro.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 *
 * 默认没有开启加密器,可以不开启,系统有默认的实现类{@link HashedCredentialsMatcher}
 * 自定义密码加密方法,需要注入到自定义Realm中,会在doGetAuthenticationInfo返回的simpleAuthenticationInfo中自己校验
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
	/**
	 * 进行密码对比的.页面密码和数据库的密码 token:从页面传过来的用户名和密码(没有加密的),loginAction的传递过来的
	 * info:从数据库中获取到的密码(加密的),realm域从数据库查出来的
	 */
   public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// 强转成实现类
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		// 获取页面的用户名
		String username = upToken.getUsername();
		// 获取页面的密码
		String password = new String(upToken.getPassword()); //传过来是一个字符数组,需要转一下
		// 对密码加密
	   /*
		* 散列算法一般用于生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，
		* 常见的散列算法如MD5、SHA等。一般进行散列时最好提供一个salt（盐），比如加密密码“admin”，
		* 产生的散列值是“21232f297a57a5a743894a0e4a801fc3”，
		* 可以到一些md5解密网站很容易的通过散列值得到密码“admin”，
		* 即如果直接对密码进行散列相对来说破解更容易，此时我们可以加一些只有系统知道的干扰数据，
		* 如用户名和ID（即盐）；这样散列的对象是“密码+用户名+ID”，这样生成的散列值相对来说更难破解。
		*/
	   //高强度加密算法,不可逆,生成32位字符
	   //TODO 用户名改变的时候,需要修改密码
		String fromPwd = new Md5Hash(password,username,2).toString();
		// 获取到数据库中的密码
		String dbPwd = (String) info.getCredentials();
		// 调用父类的方法,返回一个布尔类型的值
		return super.equals(fromPwd, dbPwd);
	}

	public static void main(String[] args) {
		System.out.println(new Md5Hash("admin","admin",2).toString());
	}
}