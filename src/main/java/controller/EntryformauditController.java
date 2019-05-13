package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.activity;
import model.entryform_audit;
import model.entryitem;

import com.jfinal.core.Controller;

public class EntryformauditController extends Controller{
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
				value[i] = String.join("-",checked);
			}else{
				value[i] = getPara(e.getStr("name"));
			}
		}
		List<entryform_audit> eas = entryform_audit.dao.find("select * from entryform_audit where activity = '"+aid+"' and participant = '"+pname+"'");
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
	}
	public void getAuditOfOrganizer(){
		String name = getPara("name");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where organizer = '"+name+"' and status = 'tobeAudit' order by submittime desc");
		renderJson(res);
	}
	public void searchAudit(){
		String organizer = getPara("organizer");
		String type = getPara("type");
		String value = getPara("value");
		List<entryform_audit> res = entryform_audit.dao.find("select * from entryform_audit where organizer = '"+organizer+"' and "+type+" like '%"+value+"%' and status = 'tobeAudit' order by submittime desc");
		renderJson(res);
	}
}
