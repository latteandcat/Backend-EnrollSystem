package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.activity;
import model.entryitem;

import com.jfinal.core.Controller;

public class ActivityController extends Controller {
	//获取所有待审核活动的list
	public void getAllUnauditActivities(){
		List<activity> unauditActivities = activity.dao.find("select * from activity where  isapproved = 'tobeaudit'");
		renderJson(unauditActivities);
	}
	//获取未审核活动和审核失败的list
	public void getUnauditActivities(){
		String name = getPara("name");
		List<activity> unauditActivities = activity.dao.find("select * from activity where organizer = '"+name+"' and isapproved != 'passed'");
		renderJson(unauditActivities);
	}
	//获取未完成活动的list
	public void getUnfinishedActivities(){
		String name = getPara("name");
		List<activity> unfinishedActivities = activity.dao.find("select * from activity where organizer = '"+name+"' and isapproved = 'passed' and isarchived = 'unarchived'");
		renderJson(unfinishedActivities);
	}
	//获取已完成活动的list
	public void getFinishedActivities(){
		String name = getPara("name");
		List<activity> finishedActivities = activity.dao.find("select * from activity where organizer = '"+name+"' and isapproved = 'passed' and isarchived = 'archived'");
		renderJson(finishedActivities);
	}
	//根据活动名称查询未审核活动
	public void searchUnauditActivities(){
		String name = getPara("name");
		String organizer = getPara("organizer");
		List<activity> searchResults = new ArrayList<activity>();
		if(organizer.equals("")){
			searchResults = activity.dao.find("select * from activity where name = '"+name+"'");
		}else{
			searchResults = activity.dao.find("select * from activity where name = '"+name+"' and organizer = '"+organizer+"' ");
		}
		renderJson(searchResults);
	}
	//根据活动名称查询未完成活动
	public void searchUnfinishedActivities(){
		String name = getPara("name");
		String organizer = getPara("organizer");
		List<activity> searchResults = activity.dao.find("select * from activity where name = '"+name+"' and organizer = '"+organizer+"' ");
		renderJson(searchResults);
	}
	//根据活动名称查询已完成活动
	public void searchFinishedActivities(){
		String name = getPara("name");
		String organizer = getPara("organizer");
		List<activity> searchResults = activity.dao.find("select * from activity where name = '"+name+"' and organizer = '"+organizer+"' ");
		renderJson(searchResults);
	}
	//发起活动
	public void addActivity() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String name = getPara("name");
		String organizer = getPara("organizer");
		String organization = getPara("organization");
		String starttime = getPara("starttime");
		String endtime= getPara("endtime");
		String deadline= getPara("deadline");
		String site= getPara("site");
		String detail= getPara("detail");
		String isneedaudit = getPara("isneedaudit");
		int length = getParaToInt("length");
		String entryitems[] = new String[length];
		for (int i = 0; i < entryitems.length; i++) {
			entryitems[i] = getPara("entryform["+i+"][id]");
		}
		String entryform = String.join(",",entryitems);
		List<activity> activities = activity.dao.find("select * from activity where name = '"+name+"' and organizer = '"+organizer+"'");
		if(activities.isEmpty()){
			activity a = getModel(activity.class);
			List<activity> as = activity.dao.find("select * from activity where id in(select max(id) from activity)");
			if(as.size()==0){
				a.set("id",1);
			}else{
				a.set("id",as.get(0).getInt("id")+1);
			}
			a.set("name",name);
			a.set("organizer",organizer);
			a.set("organization",organization);
			a.set("starttime", parseDate(starttime));
			a.set("endtime", parseDate(endtime));
			a.set("deadline", parseDate(deadline));
			a.set("site", site);
			a.set("detail", detail);
			a.set("isneedaudit", isneedaudit);
			a.set("entryform", entryform);
			a.set("isapproved", "tobeaudit");
			a.set("isarchived", "unarchived");
			a.set("submittime", df.format(new Date()));
			a.save();
			renderJson("{\"status\":\"addSuccess\"}");
		}else{
			renderJson("{\"status\":\"alreadyExist\"}");
		}
	}
	//更新活动信息
	public void updateActivity() throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		String name = getPara("name");
		String organization = getPara("organization");
		String starttime = getPara("starttime");
		String endtime= getPara("endtime");
		String deadline= getPara("deadline");
		String site= getPara("site");
		String detail= getPara("detail");
		String isneedaudit = getPara("isneedaudit");
		int length = getParaToInt("length");
		String entryitems[] = new String[length];
		for (int i = 0; i < entryitems.length; i++) {
			entryitems[i] = getPara("entryform["+i+"][id]");
		}
		String entryform = String.join(",",entryitems);
		activity a = activity.dao.findById(id);
		a.set("name",name);
		a.set("organization",organization);
		a.set("starttime", parseDate(starttime));
		a.set("endtime", parseDate(endtime));
		a.set("deadline", parseDate(deadline));
		a.set("site", site);
		a.set("detail", detail);
		a.set("isneedaudit", isneedaudit);
		a.set("entryform", entryform);
		a.set("submittime", df.format(new Date()));
		a.update();
		renderJson("{\"status\":\"updateSuccess\"}");
	}
	// 删除报名项
	public void deleteActivity(){
		activity.dao.deleteById(getParaToInt("id"));
		renderJson("{\"status\":\"deleteSuccess\"}");
	}
	//转换前台发送的日期的方法
	public String parseDate(String d) throws ParseException{
		d = d.replace("Z"," UTC");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat1.parse(d);
		d = simpleDateFormat2.format(date);
		return d;
	}
	//通过活动审核的方法
	public void passActivity(){
		int id = getParaToInt("id");
		activity a = activity.dao.findById(id);
		a.set("isapproved", "passed");
		a.update();
		renderJson("{\"status\":\"passSuccess\"}");
	}
	//不通过活动审核的方法
	public void unpassActivity(){
		int id = getParaToInt("id");
		String reason = getPara("reason");
		activity a = activity.dao.findById(id);
		a.set("isapproved", reason);
		a.update();
		renderJson("{\"status\":\"unpassSuccess\"}");
	}
	//申请重新审核的方法
	public void reauditActivity(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		activity a = activity.dao.findById(id);
		a.set("isapproved", "tobeaudit");
		a.set("submittime", df.format(new Date()));
		a.update();
		renderJson("{\"status\":\"reauditSuccess\"}");
	}
}
