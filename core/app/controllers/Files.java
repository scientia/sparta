package controllers;

import java.util.List;
import java.util.Map;

import controllers.CoreController.ObjectType;

import models.File;
import models.FileContent;
import models.ItemFile;
import models.ItemModel;
import models.Part;
import models.Space;
import play.data.Upload;
import play.db.Model;
import play.exceptions.UnexpectedException;
import play.mvc.With;
import play.mvc.Http.Request;

@With(Secure.class)
public class Files extends CoreController {


	/**
	 * Chunking is currently not supported.
	 * 
	 * @param file
	 * @param name
	 * @throws ClassNotFoundException 
	 */
	public static void upload(Long id ,String entityClass, Long entityid, String name, int chunks, int chunk) throws ClassNotFoundException {
		
		/*Map args1 = params.all();
		String name1 = params.get("name");
		String item = params.get("file");
		String chunks = params.get("chunks");
		String chunk = params.get("chunk");*/
		String spacePath = null;
		Space sp = Space.findById(id);
        if(sp != null){
         spacePath = sp.mappedDir;
	    }
		try {
			Map args = Request.current().args;
			List<Upload> uploads = (List<Upload>) Request.current().args
					.get("__UPLOADS");
			java.io.File file = null;
			String filename = "";
			FileContent b =  FileContent.class.newInstance();
			for (Upload upload : uploads) {
				if (upload.getFieldName().equals("file")
						&& upload.getSize() > 0) {					
					file = upload.asFile();
					filename = upload.getFileName();
					b.set( upload.asStream(), name, upload.getContentType(), spacePath);
					break;
				}
			}
            if(filename != null && b != null){            	              
			  File f = new File(filename,name, b, sp).save();
			  new ItemFile(entityid, entityClass, f).save();
			  
            }else{
            	throw new UnexpectedException("Either filename or file content are null.");
            }
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
		renderJSON("{success:true}");

	}

}
