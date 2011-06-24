package controllers;

import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.For;
import controllers.CoreController.ObjectType;
import models.Bom;
import models.Customer;
import models.DesignDocumentVersion;
import models.Document;
import models.DocumentVersion;
import models.ECO;
import models.ECR;
import models.File;
import models.LookupValue;
import models.Part;
import models.PartStructure;
import models.PartVersion;
import models.Plant;
import models.Project;
import models.StructureTree;
import models.Supplier;
import models.UnitOfMeasure;
import models.User;
import models.number.PartNumber;
import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin.JPAModelLoader;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.With;

@With(Secure.class)
@For(PartVersion.class)
public class Parts extends Products {

	private static String DEFAULT_UOM = "uom";
	private static String DEFAULT_SERVICEPART = "false";
	
	@Before
	public static void addType() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		renderArgs.put("type", type);
	}

	public static void index() {
		if (getControllerClass() == CoreController.class) {
			forbidden();
		}
		render("core/index.html");
	}

	public static void list(int page, String search, String searchFields,
			String orderBy, String order) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		if (page < 1) {
			page = 1;
		}
		List<Model> objects = type.findPage(page, search, searchFields,
				orderBy, order, (String) request.args.get("where"));
		Long count = type.count(search, searchFields, (String) request.args
				.get("where"));
		Long totalCount = type.count(null, null, (String) request.args
				.get("where"));
		try {
			render(type, objects, count, totalCount, page, orderBy, order);
		} catch (TemplateNotFoundException e) {
			render("core/list.html", type, objects, count, totalCount, page,
					orderBy, order);
		}
	}

	public static void show(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("core/show.html", type, object);
		}
	}

	public static void edit(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);

		notFoundIfNull(object);
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("core/edit.html", type, object);
		}
	}

	public static void attachment(String id, String field) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		Object att = object.getClass().getField(field).get(object);
		if (att instanceof Model.BinaryField) {
			Model.BinaryField attachment = (Model.BinaryField) att;
			if (attachment == null || !attachment.exists()) {
				notFound();
			}
			response.contentType = attachment.type();
			renderBinary(attachment.get(), attachment.length());
		}
		// DEPRECATED
		if (att instanceof play.db.jpa.FileAttachment) {
			play.db.jpa.FileAttachment attachment = (play.db.jpa.FileAttachment) att;
			if (attachment == null || !attachment.exists()) {
				notFound();
			}
			renderBinary(attachment.get(), attachment.filename);
		}
		notFound();
	}

	public static void save(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
	
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("core.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/show.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("core/show.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("core.saved", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		redirect(request.controller + ".show", object._key());
	}

	public static void create() throws Exception {
		/** First create the identifier */
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		String isServicePart = params.get("object.identifier.servicePart");
		String uom = params.get("object.identifier.uom");
		if(isServicePart == null){
			isServicePart = DEFAULT_SERVICEPART;
		}
		if(uom == null){
			uom = DEFAULT_UOM;
		}
		
		PartVersion partVersion = new PartVersion();
		Binder.bind(partVersion, "object", params.all());
		
		validation.valid(partVersion);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("core.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/blank.html",
						type, partVersion);
			} catch (TemplateNotFoundException e) {
				render("core/blank.html", type, partVersion);
			}
		}
		
		String number = PartNumber.getPartNumber();
		Part part = new Part(number);
		part.servicePart = Boolean.parseBoolean(isServicePart);
		part.uom = UnitOfMeasure.find("byCode", uom).first();		
		part.save();		
		if(part == null){
			JPA.setRollbackOnly();
			error(Messages.get("sparta.partidentifier.failedcreate"));
		}
		
		partVersion.identifier = part;
		//String number = PartNumber.getPartNumber();
		//Part part = new Part(number).save();
		//partVersion.identifier = part;
		partVersion._save();
		
		flash.success(Messages.get("core.created", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		if (params.get("_saveAndAddAnother") != null) {
			redirect(request.controller + ".blank");
		}
		redirect(request.controller + ".show", partVersion._key());
	}


	
	

	public static void freeze(String id) {
		documents("1");
	}
	
	public static void userlist() {
		List<User> users = User.findAll();
		List ulist = new ArrayList();
		for (User u : users) {
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("id", u.id.toString());
			umap.put("name", u.username);
			ulist.add(umap);
		}
		renderText("successfully added documents");
	}

	/** Document Relations Tab Start*/
	public static void adddocuments(String id) {
		String documentIds = params.get("partdocs");
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		
		List<DocumentVersion> docs = relIdsToDocuments(documentIds);
		List<String> updates = new ArrayList<String>();
		for(DocumentVersion doc: docs){
			object.addDocument(doc);
			updates.add(doc.name);
		}		
		object.save();
		flash.success(Messages.get("sparta.relation.documents", type.modelName));
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	public static void documents(String id) {

		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		List<DocumentVersion> objects = object.getDocuments();
		// render(type, documents, documents.size(), documents.size(), 1,
		// orderBy, order);
		type = ObjectType.get(Documents.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
		// render(objects);
	}

	/**
	 * Add Documents to part.
	 * @param idlist
	 * @return
	 */
	private static List<DocumentVersion> relIdsToDocuments(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<DocumentVersion> models = new ArrayList<DocumentVersion>();
		for(String id : ids){
			DocumentVersion dv = DocumentVersion.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
		
	}
	/** Documents Relations Tab End */
	
	/** Part Relations Tab Start*/
	/**
	 * Add Parts to Parts with Structure relation
	 */
	public static void listparts(String q){
		String queryLike = LIKE_CHAR+q+LIKE_CHAR;
		List<PartVersion> parts;
		/*if query is null find the latest documents that user
		 *  has either recently created or updated 
		 */
		if(queryLike == null){
			String currentUser = Security.connected();		
			parts = PartVersion.find("select p from PartVersion p where d.createdBy = ? or d.updatedBy = ? order by lastupdated", currentUser, currentUser).fetch(DRPDOWN_MAX_NO_OBJS);

		}else{
			parts = PartVersion.find("byNameLike", queryLike).fetch(DRPDOWN_MAX_NO_OBJS);
		}
		List partlist = new ArrayList();
		for (PartVersion doc : parts) {
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("id", doc.id.toString());
			umap.put("name", doc.name);
			partlist.add(umap);
		}
		renderJSON(partlist);
	}
	/**
	 * Add child parts to the parent
	 * @param id
	 */
	public static void addparts(String id) {
		String partIds = params.get("used");
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		List<String> updates = new ArrayList<String>();
		
		List<PartVersion> parts = relIdsToParts(partIds);
		for(PartVersion part: parts){
			object.addPart(part);
			updates.add(part.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	public static void uses(String id) {
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		List<PartStructure> objects = object.getUsesParts();
		
		type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void usedon(String id){
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		List<PartVersion> objects = object.getUsedOnParts();
		
		type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	/**Part List Relations Tab Start*/
    
	private static List<PartVersion> relIdsToParts(String partIds) {
		List<String> ids = stringToArrayList(partIds);
		List<PartVersion> models = new ArrayList<PartVersion>();
		for(String id : ids){
			PartVersion pv = PartVersion.findById(Long.parseLong(id));
			models.add(pv);
		}
		return models;
	}
	
	/** Customer Relations Tab Start*/
	public static void addcustomers(String id) {
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		String customerIds = params.get("partcustomers");
		List<Customer> customers = relIdsToCustomers(customerIds);
		List<String> updates = new ArrayList<String>();
		for(Customer customer: customers){
			object.addCustomer(customer);
			updates.add(customer.name);
		}		
		object.save();
		flash.success(Messages.get("sparta.relation.documents", type.modelName));
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	private static List<Customer> relIdsToCustomers(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Customer> models = new ArrayList<Customer>();
		for(String id : ids){		
			Customer dv = Customer.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	public static void customers(String id) {
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		List<Customer> objects = object.getCustomers();		
		type = ObjectType.get(Customers.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void files(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		PartVersion object =  (PartVersion)type.findById(id);
		notFoundIfNull(object);
		List<File> objects = object.getFiles( type.entityClass.getName());		
		type = ObjectType.get(Files.class);
		try{
		render(type, objects, objects.size(), objects.size(), 0, "name",
				"DESC");
		}catch (TemplateNotFoundException e) {
			render("common/files.html", type, objects, objects.size(), objects.size(), 0, "name", "DESC");
		}
   }
	
	/** Plant Relations Tab Start*/
	public static void addplants(String id) {
		PartVersion object = (PartVersion) getObjectFromId(id);
		String plantIds = params.get("partplants");
		List<Plant> plants = relIdsToPlants(plantIds);
		List<String> updates = new ArrayList<String>();
		for(Plant plant: plants){
			object.addPlant(plant);
			updates.add(plant.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	private static List<Plant> relIdsToPlants(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Plant> models = new ArrayList<Plant>();
		for(String id : ids){		
			Plant dv = Plant.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	public static void plants(String id){
		PartVersion object = (PartVersion) getObjectFromId(id);
		List<Plant> objects = object.getPlants();		
		ObjectType type = ObjectType.get(Suppliers.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/** Suppliers Relations Tab Start*/
	public static void addsuppliers(String id , String sids) {
		PartVersion object = (PartVersion) getObjectFromId(id);
		String supplierIds = params.get("partsuppliers");
		List<Supplier> suppliers = relIdsToSuppliers(supplierIds);
		List<String> updates = new ArrayList<String>();
		for(Supplier supplier: suppliers){
			object.addSupplier(supplier);
			updates.add(supplier.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	private static List<Supplier> relIdsToSuppliers(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Supplier> models = new ArrayList<Supplier>();
		for(String id : ids){		
			Supplier dv = Supplier.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	public static void suppliers(String id) {
		PartVersion object = (PartVersion) getObjectFromId(id);
		List<Supplier> objects = object.getSuppliers();		
		ObjectType type = ObjectType.get(Suppliers.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/**
	 * ECOs tab 
	 * It will always create an affected part relation
	 */
	public static void addecos(String id, String partecos){
		PartVersion object = (PartVersion) getObjectFromId(id);		
		List<ECO> ecos = relIdsToECOs(partecos);
		List<String> updates = new ArrayList<String>();
		for(ECO eco: ecos){
			object.addECO(eco);
			updates.add(eco.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	
	private static List<ECO> relIdsToECOs(String idlist) {
		List<String> ids = stringToArrayList(idlist);
		List<ECO> models = new ArrayList<ECO>();
		for(String id : ids){		
			ECO dv = Supplier.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	public static void ecos(String id){
		PartVersion object = (PartVersion) getObjectFromId(id);
		List<ECO> objects = object.getECOs();		
		ObjectType type = ObjectType.get(ECOs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void ecrs(String id){
		PartVersion object = (PartVersion) getObjectFromId(id);
		List<ECR> objects = object.getECRs();		
		ObjectType type = ObjectType.get(ECRs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void versions(Long id){
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
	    Part identifier = object.identifier;
	    
	    List<PartVersion> objects = PartVersion.find("byIdentifier", identifier).fetch();
	    render(type, objects);		
	}
	
	public static void bom(Long id){		
		ObjectType type = ObjectType.get(Parts.class);
		notFoundIfNull(type);
		PartVersion object = (PartVersion) type.findById(id);
		notFoundIfNull(object);
		StructureTree tree = new Bom(Bom.STRUCTURE_USES).get(object);	
		
		
		render(tree);
	}
	
	public static void bomlevel() throws UnsupportedEncodingException{
		String dir = request.params.get("dir");
		StringBuffer sb = new StringBuffer();
		if(dir == null || "".equals(dir)){
			dir="data/..";
		}
		String abcd = "<ul class=\"jqueryFileTree\" style=\"display: none;\"><li class=\"file ext_xml\"><a href=\"#\" rel=\"./jqgrid_demo/js/src/css/ellipsis-xbl.xml\">ellipsis-xbl.xml</a></li><li class=\"file ext_css\"><a href=\"#\" rel=\"./jqgrid_demo/js/src/css/jquery.searchFilter.css\">jquery.searchFilter.css</a></li><li class=\"file ext_css\"><a href=\"#\" rel=\"./jqgrid_demo/js/src/css/ui.jqgrid.css\">ui.jqgrid.css</a></li><li class=\"file ext_css\"><a href=\"#\" rel=\"./jqgrid_demo/js/src/css/ui.multiselect.css\">ui.multiselect.css</a></li></ul>";
		if (dir.charAt(dir.length()-1) == '\\') {
	    	dir = dir.substring(0, dir.length()-1) + "/";
		} else if (dir.charAt(dir.length()-1) != '/') {
		    dir += "/";
		}
		
		dir = java.net.URLDecoder.decode(dir, "UTF-8");	
		
	    if (new java.io.File(dir).exists()) {
			String[] files = new java.io.File(dir).list(new FilenameFilter() {
			    public boolean accept(java.io.File dir, String name) {
					return name.charAt(0) != '.';
			    }
			});
			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
			sb.append("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
			// All dirs
			for (String file : files) {
			    if (new java.io.File(dir, file).isDirectory()) {
			    	sb.append("<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + dir + file + "/\">"
						+ file + "</a></li>");
			    }
			}
			// All files
			for (String file : files) {
			    if (!new java.io.File(dir, file).isDirectory()) {
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
