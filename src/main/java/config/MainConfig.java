package config;
import model.activity;
import model.admin;
import model.entryform_audit;
import model.entryitem;
import model.organizer;
import model.organizer_notification;
import model.participant;
import model.participant_notification;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import controller.ActivityController;
import controller.EntryformauditController;
import controller.EntryitemController;
import controller.NotificationController;
import controller.UserController;
 
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
    	me.add("/activity", ActivityController.class);
    	me.add("/entryitem", EntryitemController.class);
    	me.add("/user", UserController.class);
    	me.add("/notification", NotificationController.class);
    	me.add("/entryformaudit", EntryformauditController.class);
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
    	arp.addMapping("entryform_audit", entryform_audit.class);
    	arp.addMapping("organizer_notification", organizer_notification.class);
    	arp.addMapping("participant_notification", participant_notification.class);
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