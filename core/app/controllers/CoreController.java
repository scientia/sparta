package controllers;

import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import org.drools.lang.DRLParser.attributes_key_return;

import models.DocumentVersion;
import models.FileContent;
import models.PartVersion;
import models.ProductVersion;

import play.*;
import play.data.binding.*;
import play.mvc.*;
import play.db.Model;
import play.db.jpa.JPA;
import play.data.validation.*;
import play.exceptions.*;
import play.i18n.*;

public abstract class CoreController extends Controller {

	protected static int DRPDOWN_MAX_NO_OBJS = 50;
	protected static String LIKE_CHAR="%";
	
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
		String format = params.get("format");
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

	public static void list2(int page, String search, String searchFields,
			String orderBy, String order) {
		
		String format  = (String)request.args.get("format");
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
		/*try {
			render(type, objects, count, totalCount, page, orderBy, order);
		} catch (TemplateNotFoundException e) {
			render("core/list.html", type, objects, count, totalCount, page,
					orderBy, order);
		}*/
		renderJSON(objects);
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
		
		if(att instanceof FileContent){
			FileContent attachment = (FileContent)att;
			if (attachment == null || !attachment.exists()) {
				notFound();
			}
			response.contentType = attachment.type();
			renderBinary(attachment.get(), attachment.length());
		}
		/**
		 * Since normal binary files are loaded to a default server
		 * configured dirctory they can be downloaded without setting 
		 * the spaceName
		 */
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
				render(request.controller.replace(".", "/") + "/edit.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("core/edit.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("core.saved", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		redirect(request.controller + ".show", object._key());
	}

	public static void blank() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Model object = (Model) constructor.newInstance();
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("core/blank.html", type, object);
		}
	}

	public static void create() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Model object = (Model) constructor.newInstance();
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("core.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/blank.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("core/blank.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("core.created", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		if (params.get("_saveAndAddAnother") != null) {
			redirect(request.controller + ".blank");
		}
		redirect(request.controller + ".show", object._key());
	}

	public static void delete(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		try {
			object._delete();
		} catch (Exception e) {
			flash.error(Messages.get("core.delete.error", type.modelName));
			redirect(request.controller + ".show", object._key());
		}
		flash.success(Messages.get("core.deleted", type.modelName));
		redirect(request.controller + ".list");
	}

	public static void xdelete(String id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		try {
			object._delete();
		} catch (Exception e) {		   
		    renderText("error while deleting items");
		}
		renderText("successfully deleted items");
	}
	
	// ~~~~~~~~~~~~~
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface For {
		Class<? extends Model> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Exclude {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Hidden {
	}

	// ~~~~~~~~~~~~~
	static int getPageSize() {
		return Integer.parseInt(Play.configuration.getProperty("core.pageSize",
				"30"));
	}

	public static class ObjectType implements Comparable<ObjectType> {

		public Class<? extends Controller> controllerClass;
		public Class<? extends Model> entityClass;
		public String name;
		public String modelName;
		public String controllerName;
		public String keyName;

		public ObjectType(Class<? extends Model> modelClass) {
			this.modelName = modelClass.getSimpleName();
			this.entityClass = modelClass;
			this.keyName = Model.Manager.factoryFor(entityClass).keyName();
		}

		@SuppressWarnings("unchecked")
		public ObjectType(String modelClass) throws ClassNotFoundException {
			this((Class<? extends Model>) Play.classloader
					.loadClass(modelClass));
		}

		public static ObjectType forClass(String modelClass)
				throws ClassNotFoundException {
			return new ObjectType(modelClass);
		}

		public static ObjectType get(Class<? extends Controller> controllerClass) {
			Class<? extends Model> entityClass = getEntityClassForController(controllerClass);
			if (entityClass == null
					|| !Model.class.isAssignableFrom(entityClass)) {
				return null;
			}
			ObjectType type = new ObjectType(entityClass);
			type.name = controllerClass.getSimpleName().replace("$", "");
			type.controllerName = controllerClass.getSimpleName().toLowerCase()
					.replace("$", "");
			type.controllerClass = controllerClass;
			return type;
		}

		@SuppressWarnings("unchecked")
		public static Class<? extends Model> getEntityClassForController(
				Class<? extends Controller> controllerClass) {
			if (controllerClass.isAnnotationPresent(For.class)) {
				return ((For) (controllerClass.getAnnotation(For.class)))
						.value();
			}
			for (Type it : controllerClass.getGenericInterfaces()) {
				if (it instanceof ParameterizedType) {
					ParameterizedType type = (ParameterizedType) it;
					if (((Class<?>) type.getRawType()).getSimpleName().equals(
							"CRUDWrapper")) {
						return (Class<? extends Model>) type
								.getActualTypeArguments()[0];
					}
				}
			}
			String name = controllerClass.getSimpleName().replace("$", "");
			name = "models." + name.substring(0, name.length() - 1);
			try {
				return (Class<? extends Model>) Play.classloader
						.loadClass(name);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}

		public Object getListAction() {
			return Router.reverse(controllerClass.getName().replace("$", "")
					+ ".list");
		}

		public Object getBlankAction() {
			return Router.reverse(controllerClass.getName().replace("$", "")
					+ ".blank");
		}

		public Long count(String search, String searchFields, String where) {
			return Model.Manager.factoryFor(entityClass).count(
					searchFields == null ? new ArrayList<String>() : Arrays
							.asList(searchFields.split("[ ]")), search, where);
		}

		@SuppressWarnings("unchecked")
		public List<Model> findPage(int page, String search,
				String searchFields, String orderBy, String order, String where) {
			return Model.Manager.factoryFor(entityClass).fetch(
					(page - 1) * getPageSize(),
					getPageSize(),
					orderBy,
					order,
					searchFields == null ? new ArrayList<String>() : Arrays
							.asList(searchFields.split("[ ]")), search, where);
		}

		public Model findById(Object id) {
			if (id == null)
				return null;
			return Model.Manager.factoryFor(entityClass).findById(id);
		}

		public List<ObjectField> getFields() {
			List<ObjectField> fields = new ArrayList<ObjectField>();
			for (Model.Property f : Model.Manager.factoryFor(entityClass)
					.listProperties()) {
				ObjectField of = new ObjectField(f);
				if (of.type != null) {
					fields.add(of);
				}
			}
			return fields;
		}

		public ObjectField getField(String name) {
			for (ObjectField field : getFields()) {
				if (field.name.equals(name)) {
					return field;
				}
			}
			return null;
		}

		public int compareTo(ObjectType other) {
			return modelName.compareTo(other.modelName);
		}

		@Override
		public String toString() {
			return modelName;
		}

		public static class ObjectField {

			private Model.Property property;
			public String type = "unknown";
			public String name;
			public boolean multiple;
			public boolean required;

			public ObjectField(Model.Property property) {
				Field field = property.field;
				this.property = property;
				if (CharSequence.class.isAssignableFrom(field.getType())) {
					type = "text";
					if (field.isAnnotationPresent(MaxSize.class)) {
						int maxSize = field.getAnnotation(MaxSize.class)
								.value();
						if (maxSize > 100) {
							type = "longtext";
						}
					}
					if (field.isAnnotationPresent(Password.class)) {
						type = "password";
					}
				}
				if (Number.class.isAssignableFrom(field.getType())
						|| field.getType().equals(double.class)
						|| field.getType().equals(int.class)
						|| field.getType().equals(long.class)) {
					type = "number";
				}
				if (Boolean.class.isAssignableFrom(field.getType())
						|| field.getType().equals(boolean.class)) {
					type = "boolean";
				}
				if (Date.class.isAssignableFrom(field.getType())) {
					type = "date";
				}
				if (property.isRelation) {
					type = "relation";
				}
				if (property.isMultiple) {
					multiple = true;
				}
				if (Model.BinaryField.class.isAssignableFrom(field.getType())
						|| /** DEPRECATED **/
						play.db.jpa.FileAttachment.class.isAssignableFrom(field
								.getType())) {
					type = "binary";
				}
				if (field.getType().isEnum()) {
					type = "enum";
				}
				if (property.isGenerated) {
					type = null;
				}
				if (field.isAnnotationPresent(Required.class)) {
					required = true;
				}
				if (field.isAnnotationPresent(Hidden.class)) {
					type = "hidden";
				}
				if (field.isAnnotationPresent(Exclude.class)) {
					type = null;
				}
				if (java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
					type = null;
				}
				name = field.getName();
			}

			public List<Object> getChoices() {
				return property.choices.list();
			}
		}
	}
	
	

	/** Part List Relations Tab End*/
	/**
	 * read the input delimited string with id 
	 *
	 */
	//TODO: this could be done with a type binder
	protected static List<String> stringToArrayList(String idString){
		return Arrays.asList(idString.trim().split("\\s*" + ","+"\\s*"));
	}
	
	protected static Model getObjectFromId(String id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		return object;
	}
	
	public static void bulkdelete(Long[] keys) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);		
		for(int i=0; i< keys.length; i++){
			Long id = keys[i];
			Model object = type.findById(id);
			try {
				object._delete();
			} catch (Exception e) {
				JPA.setRollbackOnly();
				renderText(Messages.get("core.delete.error", type.modelName));
				
			}								
		}
		redirect(request.controller + ".list");
	}
}
