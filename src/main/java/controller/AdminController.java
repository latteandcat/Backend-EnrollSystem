package controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;
import model.admin;

import com.jfinal.core.Controller;

public class AdminController extends Controller {
	public void login() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		System.out.println(name+psw);
		System.out.println(EncoderByMd5(psw));
		String sql = "select * from admin where name = '" + name + "'";
		admin a = admin.dao.findFirst(sql);
		if(a==null){
			System.out.println("login failed");
			renderJson("{\"status\":\"userNotFound\",\"name\":\"\",\"role\":\"\"}");
		}else if(a!=null){
			String pswTrue = a.getStr("password");
			System.out.println(pswTrue);
			if(psw.equals(pswTrue) == false){
				System.out.println("password fault");
				renderJson("{\"status\":\"passwordFault\",\"name\":\"\",\"role\":\"\"}");
			}else{
				getSession().setAttribute("admin", a);
				System.out.println("已保存到session,登录成功！");
				renderJson("{\"status\":\"loginSuccess\",\"name\":\"zsw\",\"role\":\"admin\"}");
			}
		} 
    }
	
	/**利用MD5进行加密
	 * @param str 待加密的字符串
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
			//确定计算方法
			System.out.println(str);
			MessageDigest md5=MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			//加密后的字符串
			String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
			System.out.println(newstr);
			return newstr;
	}
	/**判断用户密码是否正确
　　　　* @param newpasswd  用户输入的密码
　　　　 * @param oldpasswd  数据库中存储的密码－－用户密码的摘要
　　　　* @return
　　　　* @throws NoSuchAlgorithmException
　　　　* @throws UnsupportedEncodingException
　　　　*/
	public boolean checkpassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
			if(EncoderByMd5(newpasswd).equals(oldpasswd))
				return true;
			else
				return false;
	}
}
