package controllers;

import models.File;
import models.FileContent;
import models.Role;
import models.Space;
import models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import play.data.Upload;
import play.db.Model;
import play.db.Model.BinaryField;
import play.exceptions.UnexpectedException;
import play.mvc.Before;
import play.mvc.With;
import play.mvc.Http.Request;

@With(Secure.class)
public class Dashboard extends CoreController {

	/*
	 * @Before static void setConnectedUser(){ if(Security.isConnected()){ User
	 * user = User.find("byUsername", Security.connected()).first();
	 * renderArgs.put("user", user.fullname); } }
	 */

	public static void index() {

		render();
	}

	public static void dump(Map args, int uploader_count) {

		for (int count = 1; count <= uploader_count; count++) {
			String namekey = "uploader_" + count + "_name";
			String namevalue = params.get(namekey);

			String tempnamekey = "uploader_" + count + "_tmpname";
			String tempnameval = params.get(tempnamekey);

			java.io.File dir = new java.io.File(
					"C:/prashant/scientia/symphony/uploads");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			java.io.File file2 = new java.io.File(dir, tempnameval);
			try {
				InputStream is = new FileInputStream(file2);
				try {
					BinaryField b = (BinaryField) FileContent.class.newInstance();

				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {

			}
		}
		Map args2 = params.all();
		Set<String> keys = args2.keySet();
		for (String key : keys) {
			String value = (String) args2.get(key);
		}
		renderJSON(args);
	}
}
