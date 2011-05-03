package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Supplier;

import play.mvc.With;

@With(Secure.class)
public class Suppliers extends CoreController {

	public static void listsuppliers(String q) {
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR + queryIn + LIKE_CHAR;
		List<Supplier> suppliers;

		if (queryLike == null) {
			suppliers = Supplier.findAll();
		} else {
			suppliers = Supplier.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List supplierlist = new ArrayList();
		for (Supplier supplier : suppliers) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", supplier.id.toString());
			smap.put("name", supplier.name);
			supplierlist.add(smap);
		}
		renderJSON(supplierlist);
	}
	
}
