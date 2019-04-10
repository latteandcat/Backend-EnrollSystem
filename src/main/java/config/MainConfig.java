package config;
import model.activity;
import model.admin;
import model.entryitem;
import model.organizer;
import model.participant;
import model.question;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import controller.ActivityController;
import controller.AdminController;
import controller.EntryitemController;
import controller.LoginController;
import controller.OrganizerController;
import controller.ParticipantController;
import controller.QuestionController;
import controller.TestController;
 
public class MainConfig extends JFinalConfig {
 
    public static void main(String[] args) {
        UndertowServer.start(MainConfig.class, 7040, true);
    }
 
    public void configConstant(Constants me) {
    	//此方法用来配置JFinal常量值
    	me.setDevMode(true);
    }
    
    public void configRoute(Routes me) {
    	//此方法用来配置访问路由
    	me.add("/api/test", TestController.class);
    	me.add("/api/activity", ActivityController.class);
    	me.add("/api/admin", AdminController.class);
    	me.add("/api/entryitem", EntryitemController.class);
    	me.add("/api/login", LoginController.class);
    	me.add("/api/organizer", OrganizerController.class);
    	me.add("/api/participant", ParticipantController.class);
    	me.add("/api/question", QuestionController.class);
    }
    
    public void configEngine(Engine me) {
    	// 此方法用来配置Template Engine
    }
    public void configPlugin(Plugins me) {
    	//此方法用来配置JFinal的Plugin
    	PropKit.use("database.txt");
    	DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcurl"),PropKit.get("user"),PropKit.get("password").trim());
    	me.add(druidPlugin);
    	ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
    	me.add(arp);
    	arp.addMapping("activity", activity.class);
    	arp.addMapping("admin", admin.class);
    	arp.addMapping("entryitem", entryitem.class);
    	arp.addMapping("organizer", organizer.class);
    	arp.addMapping("participant", participant.class);
    	arp.addMapping("question", question.class);
    	arp.setShowSql(true);
    	arp.setDialect(new MysqlDialect());
    }
    public void configInterceptor(Interceptors me) {
    	// 此方法用来配置JFinal的全局拦截器，全局拦截器将拦截所有 action 请求，除非使用@Clear在Controller中清除
    }
    public void configHandler(Handlers me) {
    	//此方法用来配置JFinal的Handler
    }
}