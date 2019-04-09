package controller;

import com.jfinal.core.Controller;

public class ActivityController extends Controller {
	public void test() {
    	getResponse().addHeader("Access-Control-Allow-Origin", "*");
        renderJson("{\"age\":25,\"name\":\"huiliuyi\"}");
    }
}
