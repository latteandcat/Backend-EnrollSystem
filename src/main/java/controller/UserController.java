package controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import sun.misc.BASE64Encoder;
import model.admin;
import model.participant;
import model.organizer;

import com.jfinal.core.Controller;

public class UserController extends Controller{
	/*
	 * 管理员登录
	 * */
	public void admin() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		System.out.println("登陆用户为："+name+"密码为："+psw);
		String sql = "select * from admin where name = '" + name + "'";
		admin a = admin.dao.findFirst(sql);
		if(a==null){
			System.out.println("login failed");
			renderJson("{\"status\":\"userNotFound\",\"name\":\"\",\"role\":\"\"}");
		}else if(a!=null){
			String pswTrue = a.getStr("password");
			if(checkpassword(psw, pswTrue) == false){
				System.out.println("password fault");
				renderJson("{\"status\":\"passwordFault\",\"name\":\"\",\"role\":\"\"}");
			}else{
				renderJson("{\"status\":\"loginSuccess\",\"name\":\""+name+"\",\"role\":\"admin\"}");
			}
		} 
    }
	/*
	 * participant登录
	 * */
	public void participant() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		System.out.println("登陆用户为："+name+"密码为："+psw);
		String sql = "select * from participant where name = '" + name + "'";
		participant p = participant.dao.findFirst(sql);
		if(p==null){
			System.out.println("login failed");
			renderJson("{\"status\":\"userNotFound\",\"name\":\"\",\"role\":\"\"}");
		}else if(p!=null){
			String pswTrue = p.getStr("password");
			if(checkpassword(psw, pswTrue) == false){
				System.out.println("password fault");
				renderJson("{\"status\":\"passwordFault\",\"name\":\"\",\"role\":\"\"}");
			}else{
				renderJson("{\"status\":\"loginSuccess\",\"name\":\""+name+"\",\"role\":\"participant\"}");
			}
		} 
    }
	/*
	 * participant注册
	 * */
	public void participantRegister() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		String phone = getPara("phone");
		String pswInSql = EncoderByMd5(psw);
		List<participant> ps = participant.dao.find("select * from participant where name = '"+ name +"'");
		if(ps.isEmpty()){
			participant p = getModel(participant.class);
			List<participant> plist = participant.dao.find("select * from participant where id in(select max(id) from participant)");
			if(plist.size()==0){
				p.set("id",1);
			}else{
				p.set("id",plist.get(0).getInt("id")+1);
			}
			p.set("name",name);
			p.set("password",pswInSql);
			p.set("phonenumber",phone);
			p.save();
			renderJson("{\"status\":\"RegisterSuccess\"}");
		}else{
			renderJson("{\"status\":\"AlreadyExist\"}");
		}
	}
	/*
	 * organizer登录
	 * */
	public void organizer() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		System.out.println("登陆用户为："+name+"密码为："+psw);
		String sql = "select * from organizer where name = '" + name + "'";
		organizer o = organizer.dao.findFirst(sql);
		if(o==null){
			System.out.println("login failed");
			renderJson("{\"status\":\"userNotFound\",\"name\":\"\",\"role\":\"\"}");
		}else if(o!=null){
			String pswTrue = o.getStr("password");
			if(checkpassword(psw, pswTrue) == false){
				System.out.println("password fault");
				renderJson("{\"status\":\"passwordFault\",\"name\":\"\",\"role\":\"\"}");
			}else{
				renderJson("{\"status\":\"loginSuccess\",\"name\":\""+name+"\",\"role\":\"organizer\"}");
			}
		} 
    }
	/*
	 * organizer注册
	 * */
	public void organizerRegister() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String psw = getPara("psw");
		String phone = getPara("phone");
		String pswInSql = EncoderByMd5(psw);
		List<organizer> os = organizer.dao.find("select * from organizer where name = '"+ name +"'");
		if(os.isEmpty()){
			organizer o = getModel(organizer.class);
			List<organizer> olist = organizer.dao.find("select * from organizer where id in(select max(id) from organizer)");
			if(olist.size()==0){
				o.set("id",1);
			}else{
				o.set("id",olist.get(0).getInt("id")+1);
			}
			o.set("name",name);
			o.set("password",pswInSql);
			o.set("phonenumber",phone);
			o.save();
			renderJson("{\"status\":\"RegisterSuccess\"}");
		}else{
			renderJson("{\"status\":\"AlreadyExist\"}");
		}
	}
	// 获取审核员list
	public void adminList(){
		List<admin> res= admin.dao.find("select * from admin where name != '安小狐SA'");
		renderJson(res);
	}
	// 添加审核员
	public void addAdmin() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String name = getPara("name");
		String phone = getPara("phonenumber");
		String password = getPara("password");
		List<admin> ads = admin.dao.find("select * from admin where name = '"+ name +"'");
		if(ads.isEmpty()){
			admin a = getModel(admin.class);
			List<admin> alist = admin.dao.find("select * from admin where id in(select max(id) from admin)");
			if(alist.size()==0){
				a.set("id",1);
			}else{
				a.set("id",alist.get(0).getInt("id")+1);
			}
			a.set("name",name);
			a.set("password",EncoderByMd5(password));
			a.set("phonenumber",phone);
			a.save();
			renderJson("{\"status\":\"addSuccess\"}");
		}else{
			renderJson("{\"status\":\"alreadyExist\"}");
		}
	}
	// 更新审核员信息
	public void updateAdmin(){
		int id = getParaToInt("id");
		String name = getPara("name");
		String phone = getPara("phonenumber");
		admin a = admin.dao.findById(id);
		a.set("name", name);
		a.set("phonenumber",phone);
		a.update();
		renderJson("{\"status\":\"updateSuccess\"}");
	}
	// 查询审核员信息
	public void searchAdmin(){
		String name = getPara("name");
		List<admin> ads = admin.dao.find("select * from admin where name like '%"+ name +"%' and name != '安小狐SA'");
		renderJson(ads);
	}
	// 删除审核员
	public void deleteAdmin(){
		int id = getParaToInt("id");
		admin.dao.deleteById(id);
		renderJson("{\"status\":\"deleteSuccess\"}");
	}
	/*
	 * 获取用户信息
	 * */
	public void getUserInfo() {
		String name = getPara("name");
		String role = getPara("role");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			admin a = admin.dao.findFirst(sql);
			String phone = a.getStr("phonenumber");
			renderJson("{\"phonenumber\":\""+phone+"\"}");
		} else if (role.equals("participant")) {
			participant p = participant.dao.findFirst(sql);
			String phone = p.getStr("phonenumber");
			renderJson("{\"phonenumber\":\""+phone+"\"}");
		} else if (role.equals("organizer")) {
			organizer o = organizer.dao.findFirst(sql);
			String phone = o.getStr("phonenumber");
			renderJson("{\"phonenumber\":\""+phone+"\"}");
		} else {
			renderJson("{\"phonenumber\":\"\"}");
		}	
	}
	/*
	 * 修改用户密码
	 * */
	public void updatepwd() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String role = getPara("role");
		String oldpwd = getPara("oldpwd");
		String newpwd = getPara("newpwd");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			admin a = admin.dao.findFirst(sql);
			if(checkpassword(oldpwd, a.getStr("password"))){
				a.set("password", EncoderByMd5(newpwd));
				a.update();
				renderJson("{\"status\":\"UpdateSuccess\"}");
			}else{
				renderJson("{\"status\":\"PwdFault\"}");
			}
		} else if (role.equals("participant")) {
			participant p = participant.dao.findFirst(sql);
			if(checkpassword(oldpwd, p.getStr("password"))){
				p.set("password", EncoderByMd5(newpwd));
				p.update();
				renderJson("{\"status\":\"UpdateSuccess\"}");
			}else{
				renderJson("{\"status\":\"PwdFault\"}");
			}
		} else if (role.equals("organizer")) {
			organizer o = organizer.dao.findFirst(sql);
			if(checkpassword(oldpwd, o.getStr("password"))){
				o.set("password", EncoderByMd5(newpwd));
				o.update();
				renderJson("{\"status\":\"UpdateSuccess\"}");
			}else{
				renderJson("{\"status\":\"PwdFault\"}");
			}
		} else {
			renderJson("{\"status\":\"ERROR\"}");
		}		
	}
	/*
	 * 登陆后用户密码忘记时修改
	 * */
	public void forgetpwd() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String name = getPara("name");
		String role = getPara("role");
		String newpwd = getPara("newpwd");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			admin a = admin.dao.findFirst(sql);
			a.set("password", EncoderByMd5(newpwd));
			a.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else if (role.equals("participant")) {
			participant p = participant.dao.findFirst(sql);
			p.set("password", EncoderByMd5(newpwd));
			p.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else if (role.equals("organizer")) {
			organizer o = organizer.dao.findFirst(sql);
			o.set("password", EncoderByMd5(newpwd));
			o.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else {
			renderJson("{\"status\":\"ERROR\"}");
		}		
	}
	/*
	 * 登陆前用户密码忘记时修改
	 * */
	public void forgetPwdBeforeLogin() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String name = getPara("username");
		String role = getPara("userrole");
		String phone = getPara("phone");
		String newpwd = getPara("newpwd");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			List<admin> userlist = admin.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				admin a = admin.dao.findFirst(sql);
				if(a.getStr("phonenumber").equals(phone)){
					a.set("password", EncoderByMd5(newpwd));
					a.update();
					renderJson("{\"status\":\"UpdateSuccess\"}");
				}else{
					renderJson("{\"status\":\"validateFalse\"}");
				}
			}
		} else if (role.equals("participant")) {
			List<participant> userlist = participant.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				participant p = participant.dao.findFirst(sql);
				if(p.getStr("phonenumber").equals(phone)){
					p.set("password", EncoderByMd5(newpwd));
					p.update();
					renderJson("{\"status\":\"UpdateSuccess\"}");
				}else{
					renderJson("{\"status\":\"validateFalse\"}");
				}
			}
		} else if (role.equals("organizer")) {
			List<organizer> userlist = organizer.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				organizer o = organizer.dao.findFirst(sql);
				if(o.getStr("phonenumber").equals(phone)){
					o.set("password", EncoderByMd5(newpwd));
					o.update();
					renderJson("{\"status\":\"UpdateSuccess\"}");
				}else{
					renderJson("{\"status\":\"validateFalse\"}");
				}
			}
		} else {
			renderJson("{\"status\":\"ERROR\"}");
		}		
	}
	// 审核员修改用户密码
	public void changeUserpwd() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String name = getPara("name");
		String role = getPara("role");
		String newpwd = getPara("pwd");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			List<admin> userlist = admin.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				admin a = admin.dao.findFirst(sql);
				a.set("password", EncoderByMd5(newpwd));
				a.update();
				renderJson("{\"status\":\"changeSuccess\"}");
			}
		} else if (role.equals("participant")) {
			List<participant> userlist = participant.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				participant p = participant.dao.findFirst(sql);
				p.set("password", EncoderByMd5(newpwd));
				p.update();
				renderJson("{\"status\":\"changeSuccess\"}");
			}
		} else if (role.equals("organizer")) {
			List<organizer> userlist = organizer.dao.find(sql);
			if(userlist.isEmpty()){
				renderJson("{\"status\":\"userNotFound\"}");
			}else{
				organizer o = organizer.dao.findFirst(sql);
				o.set("password", EncoderByMd5(newpwd));
				o.update();
				renderJson("{\"status\":\"changeSuccess\"}");
			}
		} else {
			renderJson("{\"status\":\"ERROR\"}");
		}
	}
	/*
	 * 修改用户信息
	 * */
	public void updateprofile() {
		String name = getPara("name");
		String role = getPara("role");
		String newname = getPara("newname");
		String newphone = getPara("newphone");
		String sql = "select * from " + role + " where name = '" + name + "'";
		if (role.equals("admin")) {
			admin a = admin.dao.findFirst(sql);
			a.set("name", newname);
			a.set("phonenumber", newphone);
			a.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else if (role.equals("participant")) {
			participant p = participant.dao.findFirst(sql);
			p.set("name", newname);
			p.set("phonenumber", newphone);
			p.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else if (role.equals("organizer")) {
			organizer o = organizer.dao.findFirst(sql);
			o.set("name", newname);
			o.set("phonenumber", newphone);
			o.update();
			renderJson("{\"status\":\"UpdateSuccess\"}");
		} else {
			renderJson("{\"status\":\"ERROR\"}");
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
