package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.activity;
import model.entryform_audit;
import model.entryitem;

import com.jfinal.core.Controller;

public class EntryformauditController extends Controller{
	// 报名活动
	public void signupActivity(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int aid = getParaToInt("aid");
		String pname = getPara("pname");
		activity a = activity.dao.findById(aid);
		String entryformStr = a.get("entryform");
		String[] entryitems= entryformStr.split(",");
		String name[] = new String[entryitems.length];
		String value[] = new String[entryitems.length];
		for (int i = 0; i < entryitems.length; i++) {
			entryitem e = entryitem.dao.findById(Integer.parseInt(entryitems[i]));
			name[i] = e.get("name");
			if(e.get("type").equals("checkbox")){
				String optionStr = e.getStr("options");
				String[] options = optionStr.split(",");
				String[] checked = new String[options.length];
				for (int j = 0; j < options.length; j++) {
					checked[j] = getPara(e.getStr("name")+"["+j+"]");
				}
				value[i] = String.join("-",checked).replace("-null", "").replace("null", "");
			}else{
				value[i] = getPara(e.getStr("name"));
			}
		}
		List<entryform_audit> eas = entryform_audit.dao.find("select * from entryform_audit where activity = '"+a.getStr("name")+"' and participant = '"+pname+"' and organizer = '"+a.getStr("organizer")+"'");
		int number = a.getInt("number");
		List<entryform_audit> eaofa = entryform_audit.dao.find("select * from entryform_audit where activity = '"+a.getStr("name")+"' and organizer = '"+a.getStr("organizer")+"'");
		if(eaofa.size() < number) {
			if(eas.isEmpty()){
				entryform_audit ea = getModel(entryform_audit.class);
				List<entryform_audit> eass = entryform_audit.dao.find("select * from entryform_audit where id in(select max(id) from entryform_audit)");
				if(eass.size()==0){
					ea.set("id",1);
				}else{
					ea.set("id",eass.get(0).getInt("id")+1);
				}
				ea.set("name", String.join(",", name));
				ea.set("value", String.join(",", value));
				ea.set("submittime", df.format(new Date()));
				ea.set("participant", pname);
				ea.set("activity", a.getStr("name"));
				ea.set("organizer", a.getStr("organizer"));
				ea.set("signinstatus", "no");
				if(a.getStr("isneedaudit").equals("true")){
					ea.set("status", "tobeAudit");
					ea.save();
					renderJson("{\"status\":\"needAudit\"}");
				}else{
					ea.set("status", "noneedAudit");
					ea.save();
					renderJson("{\"status\":\"signupPassed\"}");
				}
			}else{
				renderJson("{\"status\":\"alreadySignup\"}");
			}
		} else {
			renderJson("{\"status\":\"numberFull\"}");
		}
	}
	// 获取组织者的报名审核
	public void getAuditOfOrganizer(){
		String name = getPara("name");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where organizer = '"+name+"' and status = 'tobeAudit' order by submittime desc");
		renderJson(res);
	}
	// 搜索组织者的报名审核
	public void searchAudit(){
		String organizer = getPara("organizer");
		String type = getPara("type");
		String value = getPara("value");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where organizer = '"+organizer+"' and "+type+" like '%"+value+"%' and status = 'tobeAudit' order by submittime desc");
		renderJson(res);
	}
	//通过报名审核的方法
	public void passSignup(){
		int id = getParaToInt("id");
		entryform_audit a = entryform_audit.dao.findById(id);
		a.set("status", "passed");
		a.update();
		renderJson("{\"status\":\"passSuccess\"}");
	}
	// 获取某组织者的报名信息
	public void getAuditOfParticipant(){
		String name = getPara("name");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where participant = '"+name+"' order by submittime desc");
		renderJson(res);
	}
	// 搜索报名者的报名信息
	public void searchAuditofParticipant(){
		String participant = getPara("participant");
		String type = getPara("type");
		String value = getPara("value");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where participant = '"+participant+"' and "+type+" like '%"+value+"%' order by submittime desc");
		renderJson(res);
	}
	// 取消报名
	public void cancelSignup() throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = getParaToInt("id");
		entryform_audit ea = entryform_audit.dao.findById(id);
		String ac = ea.getStr("activity");
		String or = ea.getStr("organizer");
		activity a = activity.dao.findFirst("select * from activity where organizer = '"+or+"' and name = '"+ac+"'");
		String deadline = a.getStr("deadline");
		String now = df.format(new Date());
		Date date1 = df.parse(now);
		Date date2= df.parse(deadline);
		if (date2.getTime() > date1.getTime()) {
			// date1在date2前
			entryform_audit.dao.deleteById(id);
			renderJson("{\"status\":\"cancelSuccess\"}");
		}else{
			renderJson("{\"status\":\"cannotCancel\"}");
		}
	}
	// 获取活动的报名信息
	public void getsignupAuditofActivity(){
		String ac = getPara("activity");
		String or = getPara("organizer");
		List<entryform_audit> eas = entryform_audit.dao.find("select * from entryform_audit where activity = '"+ac+"' and organizer = '"+or+"' and (status = 'passed' or status = 'noneedAudit')");
		renderJson(eas);
	}
	public void searchSignupofActivity(){
		String pa = getPara("participant");
		String ac = getPara("activity");
		String or = getPara("organizer");
		List<entryform_audit> eas = entryform_audit.dao.find("select * from entryform_audit where participant like '%"+pa+"%' and activity = '"+ac+"' and organizer = '"+or+"' and (status = 'passed' or status = 'noneedAudit')");
		renderJson(eas);
	}
	public void signinforParticipant(){
		int id = getParaToInt("id");
		entryform_audit ea = entryform_audit.dao.findById(id);
		ea.set("signinstatus","yes");
		ea.update();
		renderJson("{\"status\":\"signinSuccess\"}");
	}
}
