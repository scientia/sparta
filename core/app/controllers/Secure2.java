/*
    This file is part of secure-permissions-play-module.

    Initially adapted from the Play! Framework Secure module with the following copyright
    and license:
    
    Copyright 2010

        Guillaume Bort (http://guillaume.bort.fr)
        The Play! developers team (https://launchpad.net/~play-developers)
        zenexity(http://www.zenexity.fr)

    Licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 
    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 
	
    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.
    
    Adaptation copyright Lunatech Research 2010 under the following license:

    secure-permissions-play-module is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    secure-permissions-play-module is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Lesser Public License
    along with secure-permissions-play-module.  If not, see <http://www.gnu.org/licenses/>.
*/
package controllers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Play;
import play.data.binding.Binder;
import play.data.validation.Required;
import play.exceptions.UnexpectedException;
import play.libs.Crypto;
import play.modules.securePermissions.Check;
import play.modules.securePermissions.CheckPermission;
import play.modules.securePermissions.PermissionCheck;
import play.modules.securePermissions.Permissions;
import play.modules.securePermissions.Role;
import play.mvc.ActionInvoker;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Scope;
import play.utils.Java;

/**
 * Modified security controller that adds role-based permissions.
 */
public class Secure2 extends Controller {

    @Before(unless={"login", "authenticate", "logout"})
    static void checkAccess() throws Throwable {
        // Authent
        if(!session.contains("username")) {
            flash.put("url", request.method == "GET" ? request.url : "/"); // seems a good default
            login();
        }
        // Profile checks
        Check check = getActionAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        check = getControllerInheritedAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        // Now permission checks
        Method m = (Method) ActionInvoker.getActionMethod(Http.Request.current().action)[1];
        checkPermissions(m);
    }

    private static void check(Check check) throws Throwable {
        for(String profile : check.value()) {
            boolean hasProfile = (Boolean)Security.invoke("check", profile);
            if(!hasProfile) {
                Security.invoke("onCheckFailed", profile);
            }
        }
    }

    private static void checkPermissions(Method method) throws Throwable{
    	String[] paramsNames = Java.parameterNames(method);
    	if (paramsNames == null && method.getParameterTypes().length > 0) {
    		throw new UnexpectedException("Parameter names not found for method " + method);
    	}
    	Annotation[][] annotations = method.getParameterAnnotations();
    	for (int i = 0; i < method.getParameterTypes().length; i++) {
    		// do we have a security annotation?
    		if(annotations[i].length == 0)
    			continue;
    		// find the annotation
    		CheckPermission check = null;
    		for (int j = 0; j < annotations[i].length; j++) {
				if(annotations[i][j] instanceof CheckPermission){
					check = (CheckPermission) annotations[i][j];
					break;
				}
			}
    		if(check == null)
    			continue;
    		// so we have a check, let's bind it
    		Class<?> type = method.getParameterTypes()[i];
    		Map<String, String[]> params = new HashMap<String, String[]>();
    		if (type.equals(String.class) || Number.class.isAssignableFrom(type) || type.isPrimitive()) {
    			params.put(paramsNames[i], Scope.Params.current().getAll(paramsNames[i]));
    		} else {
    			params.putAll(Scope.Params.current().all());
    		}
            Object target = Binder.bind(paramsNames[i], method.getParameterTypes()[i], method.getGenericParameterTypes()[i], method.getParameterAnnotations()[i], params, null, method, i + 1);
    		// now check
    		if(!checkPermission(target, check.value()))
            	Security.invoke("onCheckFailed", check.value());
    	}
    }

    /**
     * Check permissions whether the current user has the given permission.
     * @throws Throwable 
     */
    public static boolean checkPermission(Object target, String action) {
    	
    	final Set<String> roles = (Set<String>) Security.invoke("roles");
		return Permissions.check(new PermissionCheck(target, action), Security.connected(), roles);
    }
    
    // ~~~ Login

    public static void login() throws Throwable {
        Http.Cookie remember = request.cookies.get("rememberme");
        if(remember != null && remember.value.indexOf("-") > 0) {
            String sign = remember.value.substring(0, remember.value.indexOf("-"));
            String username = remember.value.substring(remember.value.indexOf("-") + 1);
            if(Crypto.sign(username).equals(sign)) {
                session.put("username", username);
                renderArgs.put("user", username);
                redirectToOriginalURL();
            }
        }
        flash.keep("url");
        render();
    }

    public static void authenticate(@Required String username, String password, boolean remember) throws Throwable {
        // Check tokens
        Boolean allowed = false;
        try {
            // This is the deprecated method name
            allowed = (Boolean)Security.invoke("authentify", username, password);
        } catch (UnsupportedOperationException e ) {
            // This is the official method name
            allowed = (Boolean)Security.invoke("authenticate", username, password);
        }
        if(validation.hasErrors() || !allowed) {
            flash.keep("url");
            flash.error("secure.error");
            params.flash();
            login();
        }
        // Mark user as connected
        session.put("username", username);
        // Remember if needed
        if(remember) {
            response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");
        }
        // Redirect to the original URL (or /)
        redirectToOriginalURL();
    }

    public static void logout() throws Throwable {
    	
        session.clear();
        response.setCookie("rememberme", "", "0mn");
        Security.invoke("onDisconnected");
        flash.success("secure.logout");
        Application.index();
    }
    
    // ~~~ Utils
    
    static void redirectToOriginalURL() throws Throwable {
        Security.invoke("onAuthenticated");
        String url = flash.get("url");
        if(url == null) {
            url = "/";
        }
        redirect(url);
    }

    public static class Security extends Controller {

        /**
         * @Deprecated
         * 
         * @param username
         * @param password
         * @return
         */
        static boolean authentify(String username, String password) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method is called during the authentication process. This is where you check if
         * the user is allowed to log in into the system. This is the actual authentication process
         * against a third party system (most of the time a DB).
         *
         * @param username
         * @param password
         * @return true if the authentication process succeeded
         */
        static boolean authenticate(String username, String password) {
            return true;
        }

        /**
         * This method checks that a profile is allowed to view this page/method. This method is called prior
         * to the method's controller annotated with the @Check method. 
         *
         * @param profile
         * @return true if you are allowed to execute this controller method.
         */
        static boolean check(String profile) {
            return ((Set<String>) invoke("roles")).contains(profile);
        }

        /**
         * This method returns the current connected username
         * @return
         */
        static String connected() {
            return session.get("username");
        }

        /**
         * Indicate if a user is currently connected
         * @return  true if the user is connected
         */
        static boolean isConnected() {
            return session.contains("username");
        }

        /**
         * This method is called after a successful authentication.
         * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
         */
        static void onAuthenticated() {
        }

         /**
         * This method is called after a successful sign off.
         * You need to override this method if you with to perform specific actions (eg. Record the time the user signed off)
         */
        static void onDisconnected() {
        }

        /**
         * This method is called if a check does not succeed. By default it shows the not allowed page (the controller forbidden method).
         * @param profile
         */
        static void onCheckFailed(String profile) {
            forbidden();
        }

        protected static Object invoke(String m, Object... args) {
            Class security = null;
            List<Class> classes = Play.classloader.getAssignableClasses(Security.class);
            if(classes.size() == 0) {
                security = Security.class;
            } else {
                security = classes.get(0);
            }
            try {
                return Java.invokeStaticOrParent(security, m, args);
            } catch(InvocationTargetException e) {
            	if(e.getTargetException() instanceof RuntimeException)
            		throw (RuntimeException)e.getTargetException();
                throw new RuntimeException(e.getTargetException());
            } catch (Exception e) {
            	throw new RuntimeException(e);
			}
        }

        /**
         * This method returns the logged-in user's roles.
         */
        static Set<String> roles() {
    		return Collections.emptySet();
        }

    }

}