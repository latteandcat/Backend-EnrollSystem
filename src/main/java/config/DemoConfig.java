package config;
import com.jfinal.config.*;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import controller.HelloController;
 
public class DemoConfig extends JFinalConfig {
 
    public static void main(String[] args) {
        UndertowServer.start(DemoConfig.class, 8081, true);
    }
 
    public void configConstant(Constants me) {
       me.setDevMode(true);
    }
    
    public void configRoute(Routes me) {
       me.add("/hello", HelloController.class);
    }
    
    public void configEngine(Engine me) {}
    public void configPlugin(Plugins me) {}
    public void configInterceptor(Interceptors me) {}
    public void configHandler(Handlers me) {}
}