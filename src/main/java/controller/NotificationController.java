package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.activity;
import model.entryform_audit;
import model.organizer_notification;
import model.participant_notification;

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
		List<organizer_notification> results = organizer_notification.dao.find("select * from organizer_notification where receiver = '"+name+"' and hasread = 'no' order by createtime desc");
		renderJson(results);
	}
	//获取某组织者的已读通知
	public void hasreadnotiOfOrganizer(){
		String name = getPara("name");
		List<organizer_notification> results = organizer_notification.dao.find("select * from organizer_notification where receiver = '"+name+"' and hasread = 'yes' order by createtime desc");
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
	//添加报名审核未通过的消息
	public void notiPassSignup(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		String sender = getPara("sender");
		entryform_audit a = entryform_audit.dao.findById(id);
		String receiver = a.getStr("participant");
		String content = "亲爱的"+receiver+"，您报名的活动——"+ a.getStr("activity") + " 已经通过审核！请准时参加活动！";
		participant_notification pnoti = getModel(participant_notification.class);
		List<participant_notification> pnotis = participant_notification.dao.find("select * from participant_notification where id in(select max(id) from participant_notification)");
		if(pnotis.size()==0){
			pnoti.set("id",1);
		}else{
			pnoti.set("id",pnotis.get(0).getInt("id")+1);
		}
		pnoti.set("content",content);
		pnoti.set("sender", sender);
		pnoti.set("receiver", receiver);
		pnoti.set("createtime",df.format(new Date()));
		pnoti.set("hasread","no");
		pnoti.save();
		renderJson("{\"status\":\"addNotiSuccess\"}");		
	}
	//添加报名审核通过的消息
	public void notiUnpassSignup(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		String reason = getPara("reason");
		String sender = getPara("sender");
		entryform_audit a = entryform_audit.dao.findById(id);
		String receiver = a.getStr("participant");
		String content = "亲爱的"+receiver+"，您报名的活动——"+ a.getStr("activity") + " 由于"+reason+"未通过审核，您可以在活动报名页面重新报名！";
		participant_notification pnoti = getModel(participant_notification.class);
		List<participant_notification> pnotis = participant_notification.dao.find("select * from participant_notification where id in(select max(id) from participant_notification)");
		if(pnotis.size()==0){
			pnoti.set("id",1);
		}else{
			pnoti.set("id",pnotis.get(0).getInt("id")+1);
		}
		pnoti.set("content",content);
		pnoti.set("sender", sender);
		pnoti.set("receiver", receiver);
		pnoti.set("createtime",df.format(new Date()));
		pnoti.set("hasread","no");
		pnoti.save();
		renderJson("{\"status\":\"addNotiSuccess\"}");		
	}
	//获取某报名者的未读通知
	public void getNotiofParticipant(){
		String name = getPara("name");
		List<participant_notification> results = participant_notification.dao.find("select * from participant_notification where receiver = '"+name+"' and hasread = 'no' order by createtime desc");
		renderJson(results);
	}
	//获取某报名者的已读通知
	public void getHasreadNotiofParticipant(){
		String name = getPara("name");
		List<participant_notification> results = participant_notification.dao.find("select * from participant_notification where receiver = '"+name+"' and hasread = 'yes' order by createtime desc");
		renderJson(results);
	}
	//删除报名者已读通知
	public void deleteHasreadNotiofParticipant(){
		participant_notification.dao.deleteById(getParaToInt("id"));
		renderJson("{\"status\":\"deleteSuccess\"}");
	}
	//报名者未读变已读
	public void readNotificationofParticipant(){
		int id = getParaToInt("id");
		participant_notification pnoti = participant_notification.dao.findById(id);
		pnoti.set("hasread","yes");
		pnoti.update();
		renderJson("{\"status\":\"readSuccess\"}");
	}	
}
