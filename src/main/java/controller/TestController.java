package controller;
import com.jfinal.core.Controller;


public class TestController extends Controller {
    
	public void index() {
        renderJson("{\"name\":\"zsw\"}");
    }
}