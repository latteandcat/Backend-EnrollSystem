package controller;
import com.jfinal.core.Controller;
 
public class TestController extends Controller {
    public void index() {
    	getResponse().addHeader("Access-Control-Allow-Origin", "*");
        renderJson("{\"age\":25,\"name\":\"huiliuyi\"}");
    }
}