package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	@Before
	static void setConnectedUser(){
		if(Security.isConnected()){
			User user = User.find("byUsername", Security.connected()).first();
			renderArgs.put("user", user);
		}
	}
	
    public static void index() {
    	if(Security.isConnected()){
    		Dashboard.index();
    	}
         render();
    }
}