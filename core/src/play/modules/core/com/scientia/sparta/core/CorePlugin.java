package com.scientia.sparta.core;


import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;

public class CorePlugin extends PlayPlugin {

	@Override 

	   public void enhance(ApplicationClass appClass) throws Exception { 

	      new DisplayNameEnhancer().enhanceThisClass(appClass); 

	   } 

    /**
     * Routes
     * 
     * @see play.PlayPlugin#onRoutesLoaded()
     */
    @Override
    public void onRoutesLoaded() {
        Router.addRoute("GET", "/linkedin/login", "LinkedInController.login");
        Router.addRoute("GET", "/linkedin/logout", "LinkedInController.logout");
        Router.addRoute("GET", "/linkedin/oauth/callback", "LinkedInController.callback");
    }
}
