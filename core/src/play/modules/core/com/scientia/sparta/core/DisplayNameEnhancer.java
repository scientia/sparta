package com.scientia.sparta.core;


import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

public class DisplayNameEnhancer extends Enhancer {

	@Override 

	   public void enhanceThisClass(ApplicationClass appClass) throws Exception { 
	      CtClass ctClass = makeClass(appClass); 
	  
	      // Only enhance model classes. 
	      if (!ctClass.subtypeOf(classPool.get("play.db.jpa.JPABase"))) { 
	         return; 
	      } 
	  
	      // Only enhance if there is no toString() method. 
	      try { 
	           ctClass.getDeclaredMethod("toString"); 
	           return; 
	      } 
	      catch (NotFoundException e) { 
	         // toString method not found, so continue.          
	        } 
	  
	      // Find an enhance annotated fields. 
	      for (CtField ctField : ctClass.getDeclaredFields()) { 
	         if (hasAnnotation(ctField, DisplayName.class.getName())) { 
	            final String code = "public String toString() { " + 
	                  "return this." + ctField.getName() + ".toString(); }"; 
	            final CtMethod toString = CtMethod.make(code, ctClass); 
	            ctClass.addMethod(toString); 
	            break; 
	         } 
	      } 
	  
	      // Done - update class. 
	      appClass.enhancedByteCode = ctClass.toBytecode(); 
	      ctClass.defrost(); 
	   } 
}
