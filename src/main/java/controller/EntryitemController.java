package controller;

import java.util.ArrayList;
import java.util.List;

import model.entryitem;

import com.jfinal.core.Controller;

public class EntryitemController extends Controller{
	//自定义报名项的查询（属于某用户的）
	public void myEntryItems() {
		String name = getPara("name");
		List<entryitem> myEntryItems = entryitem.dao.find("select * from entryitem where creator = '"+name+"'");
		renderJson(myEntryItems);
	}
	//系统报名项的获取
	public void systemEntryItems() {
		List<entryitem> systemEntryItems = entryitem.dao.find("select * from entryitem where creator = 'system'");
		renderJson(systemEntryItems);
	}
	//添加自定义报名项
	public void addEntryItem() {
		String name = getPara("name");
		String type = getPara("type");
		String isrequired = getPara("isrequired");
		String reminder = getPara("reminder");
		String creator = getPara("creator");
		int length = getParaToInt("length");
		String options[] = new String[length];
		for (int i = 0; i < options.length; i++) {
			options[i] = getPara("options["+i+"]");
		}
		//数组转换为字符串
		String optionsStr = String.join(",",options);
		/**字符串转换为数组
		String[] optionsB= optionsStr.split(",");
		for (int i = 0; i < optionsB.length; i++) {
			System.out.println(optionsB[i]);
		}
		**/
		List<entryitem> entryitems = entryitem.dao.find("select * from entryitem where name = '"+name+"'and type = '"+type+"' and creator = '"+creator+"'");
		if(entryitems.isEmpty()){
			entryitem ei = getModel(entryitem.class);
			List<entryitem> eis = entryitem.dao.find("select * from entryitem where id in(select max(id) from entryitem)");
			if(eis.size()==0){
				ei.set("id", 1);
			}else{
				ei.set("id", eis.get(0).getInt("id")+1);
			}
			ei.set("name",name);
			ei.set("type",type);
			ei.set("isrequired",isrequired);
			ei.set("reminder",reminder);
			ei.set("creator",creator);
			ei.set("options",optionsStr);
			ei.save();
			renderJson("{\"status\":\"addSuccess\"}");
		}else{
			renderJson("{\"status\":\"alreadyExist\"}");
		}
	}
	// 根据类型和名称查询报名项
	public void searchItem(){
		String name = getPara("name");
		String creator = getPara("creator");
		String type = getPara("type");
		List<entryitem> entryitems = new ArrayList<entryitem>();
		if(type.equals("所有类型")){
			entryitems = entryitem.dao.find("select * from entryitem where name = '"+name+"'and creator = '"+creator+"'");
		}else{
			entryitems = entryitem.dao.find("select * from entryitem where name = '"+name+"'and creator = '"+creator+"' and type = '"+type+"'");
		}
		renderJson(entryitems);
	}
	// 更新报名项
	public void updateEntryItem(){
		int id = getParaToInt("id");
		String name = getPara("name");
		String isrequired = getPara("isrequired");
		String reminder = getPara("reminder");
		int length = getParaToInt("length");
		String options[] = new String[length];
		for (int i = 0; i < options.length; i++) {
			options[i] = getPara("options["+i+"]");
		}
		String optionsStr = String.join(",",options);
		entryitem ei = entryitem.dao.findById(id);
		ei.set("name",name);
		ei.set("isrequired",isrequired);
		ei.set("reminder",reminder);
		ei.set("options",optionsStr);
		ei.update();
		renderJson("{\"status\":\"updateSuccess\"}");
	}
	// 删除报名项
	public void deleteEntryItem(){
		entryitem.dao.deleteById(getParaToInt("id"));
		renderJson("{\"status\":\"deleteSuccess\"}");
	}
	//获取活动的报名项list
	public void getEntryItemsOfActivity() {
		String entryformStr = getPara("entryformStr");
		String[] entryitems= entryformStr.split(",");
		List<entryitem> entryform = new ArrayList<entryitem>();
		for (int i = 0; i < entryitems.length; i++) {
			entryitem e = entryitem.dao.findById(Integer.parseInt(entryitems[i]));
			System.out.println(e.getStr("id"));
			entryform.add(e);
		}
		renderJson(entryform);
	}
}
