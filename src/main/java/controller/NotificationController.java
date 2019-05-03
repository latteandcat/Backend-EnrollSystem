package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.activity;
import model.organizer_notification;

import com.jfinal.core.Controller;

public class NotificationController extends Controller{
	// 添加审核通过通知
	public void notiPassActivity(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		String sender = getPara("sender");
		activity a = activity.dao.findById(id);
		String receiver = a.getStr("organizer");
		String content = "亲爱的"+receiver+"，您发起的活动——"+ a.getStr("name") + " 已经通过管理员的审核！";
		organizer_notification onoti = getModel(organizer_notification.class);
		List<organizer_notification> onotis = organizer_notification.dao.find("select * from organizer_notification where id in(select max(id) from organizer_notification)");
		if(onotis.size()==0){
			onoti.set("id",1);
		}else{
			onoti.set("id",onotis.get(0).getInt("id")+1);
		}
		onoti.set("content",content);
		onoti.set("sender", sender);
		onoti.set("receiver", receiver);
		onoti.set("createtime",df.format(new Date()));
		onoti.set("hasread","no");
		onoti.save();
		renderJson("{\"status\":\"addNotiSuccess\"}");
	}
	// 添加审核未通过通知
	public void notiUnpassActivity(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		String reason = getPara("reason");
		String sender = getPara("sender");
		activity a = activity.dao.findById(id);
		String receiver = a.getStr("organizer");
		String content = "亲爱的"+receiver+"，您发起的活动——"+ a.getStr("name") + " 由于"+reason+"未通过审核，您可以修改活动信息后重新申请审核！";
		organizer_notification onoti = getModel(organizer_notification.class);
		List<organizer_notification> onotis = organizer_notification.dao.find("select * from organizer_notification where id in(select max(id) from organizer_notification)");
		if(onotis.size()==0){
			onoti.set("id",1);
		}else{
			onoti.set("id",onotis.get(0).getInt("id")+1);
		}
		onoti.set("content",content);
		onoti.set("sender", sender);
		onoti.set("receiver", receiver);
		onoti.set("createtime",df.format(new Date()));
		onoti.set("hasread","no");
		onoti.save();
		renderJson("{\"status\":\"addNotiSuccess\"}");
	}
	//获取某组织者的未读通知
	public void unreadnotiOfOrganizer(){
		String name = getPara("name");
		List<organizer_notification> results = organizer_notification.dao.find("select * from organizer_notification where receiver = '"+name+"' and hasread = 'no' ");
		renderJson(results);
	}
	//获取某组织者的已读通知
	public void hasreadnotiOfOrganizer(){
		String name = getPara("name");
		List<organizer_notification> results = organizer_notification.dao.find("select * from organizer_notification where receiver = '"+name+"' and hasread = 'yes' ");
		renderJson(results);
	}
	//删除已读通知
	public void deleteHasreadnotiOfOrganizer(){
		organizer_notification.dao.deleteById(getParaToInt("id"));
		renderJson("{\"status\":\"deleteSuccess\"}");
	}
	//未读变已读
	public void readNotification(){
		int id = getParaToInt("id");
		organizer_notification onoti = organizer_notification.dao.findById(id);
		onoti.set("hasread","yes");
		onoti.update();
		renderJson("{\"status\":\"readSuccess\"}");
	}
}
