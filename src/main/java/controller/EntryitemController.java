package controller;

import java.util.Arrays;
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
	// 根据名称查询报名项
	public void searchItemByName(){
		String name = getPara("name");
		String creator = getPara("creator");
		List<entryitem> entryitems = entryitem.dao.find("select * from entryitem where name = '"+name+"'and creator = '"+creator+"'");
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
}
