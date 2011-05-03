package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import models.User;
import play.Logger;

public class Security extends Secure.Security {
  static boolean authentify(String userID, String password) {
    // create admin user if no users currently exist
    // don't forget to change the password or remove this code later
    
    User user = User.find("byUsername", userID).first();
    if (user == null) {
      flash.error("Invalid userid or password.");
      return false;
    }
    String passwordHash = password;//md5(password);
    boolean match = user != null && user.passwordHash.equals(passwordHash);
    if (match) {
      user.loginCount++;
      user.merge();
      user.save();
    }
    return match;
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

