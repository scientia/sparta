package controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import models.Project;
import models.Space;
import models.Staff;
import controllers.CoreController.ObjectType;
import play.Play;
import play.mvc.With;

@With(Secure.class)
public class Spaces extends CoreController {

	
	
	public static void projects(String id) {
		ObjectType type = ObjectType.get(Spaces.class);
		notFoundIfNull(type);
		Space object = (Space) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getProjects();

		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void filebrowser(String id){
		ObjectType type = ObjectType.get(Spaces.class);
		notFoundIfNull(type);
		Space object = (Space) type.findById(id);
		notFoundIfNull(object);
		
		render(object);
	}
	
	public static void browsefiles(String dir) throws UnsupportedEncodingException{
		//String dir = request.params.get("dir");
		StringBuffer sb = new StringBuffer();
		if(dir == null || "".equals(dir)){
			dir="data/wip";
		}
		
		if (dir.charAt(dir.length()-1) == '\\') {
	    	dir = dir.substring(0, dir.length()-1) + "/";
		} else if (dir.charAt(dir.length()-1) != '/') {
		    dir += "/";
		}
		
		dir = java.net.URLDecoder.decode(dir, "UTF-8");	
		File store = null;
		if(new File(dir).isAbsolute()) {
			store = new File(dir);
        } else {
            store= Play.getFile(dir);
        }
		
	    if (store.exists()) {
			String[] files = store.list(new FilenameFilter() {
			    public boolean accept(java.io.File dir, String name) {
					return name.charAt(0) != '.';
			    }
			});
			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
			sb.append("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
			// All dirs
			for (String file : files) {
			    if (new java.io.File(store.getAbsolutePath(), file).isDirectory()) {
			    	sb.append("<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + dir + file + "/\">"
						+ file + "</a></li>");
			    }
			}
			// All files
			for (String file : files) {
			    if (!new java.io.File(store.getAbsolutePath(), file).isDirectory()) {
					int dotIndex = file.lastIndexOf('.');
					String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
					sb.append("<li class=\"file ext_" + ext + "\"><a href=\"#\" rel=\"" + dir + file + "\">"
						+ file + "</a></li>");
			    	}
			}
			sb.append("</ul>");
	    }
	    
	    renderText(sb.toString());
	}
}
