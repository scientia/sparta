package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import models.User;
import play.Logger;
import play.libs.Codec;
import play.modules.securePermissions.Check;

public class Security extends Secure2.Security {
  static boolean authentify(String userID, String password) {
    // create admin user if no users currently exist
    // don't forget to change the password or remove this code later
    
    User user = User.find("byUsername", userID).first();
    if (user == null) {
      flash.error("Invalid userid or password.");
      return false;
    }
    String passwordHash = Codec.hexMD5(password);
    boolean match = user != null && user.passwordHash.equals(passwordHash);
    if (match) {
      user.loginCount++;
      user.merge();
      user.save();
      if(user.viewContext != null && user.viewContext.id != null){
          session.put("viewcontext", user.viewContext.id);
      }
    }
    return match;
  }

  private static void checkIfMe(Check check) throws Throwable {
      for(String username : check.value()) {
          boolean hasProfile = (Boolean)Security.invoke("checkIfMe", username);
          if(!hasProfile) {
              Security.invoke("onCheckFailed", username);
          }
      }
  }
  
  static boolean checkIfMe(String userString) {
    return session.get("username").equals(userString);
  }
	  
  public static String md5(String password) {
    byte[] bytesOfMessage = password.getBytes();
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      Logger.fatal(e, "System configuration error");
      return null;
    }
    byte[] thedigest = md.digest(bytesOfMessage);
    String passwordHash = new String(thedigest);
    return passwordHash;
  }

  static boolean check(String profile) {
    User user = User.find("byUserID", connected()).first();
    return true;
   // return user.profiles != null && user.role.name().equalsIgnoreCase(profile);
  }
  
  static User getConnectedUser(){
	  String username = connected();
	  return User.find("byUsername",username).first();
  }
}

