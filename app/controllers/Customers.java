package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Customer;
import models.Staff;
import play.mvc.With;

@With(Secure.class)
public class Customers extends CoreController {

	public static void listcustomers(String q) {
		
		String queryLike = LIKE_CHAR + q + LIKE_CHAR;
		List<Customer> customers;

		if (queryLike == null) {
			customers = Customer.findAll();
		} else {
			customers = Customer.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List custlist = new ArrayList();
		for (Customer staff : customers) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", staff.id.toString());
			smap.put("name", staff.name);
			custlist.add(smap);
		}
		renderJSON(custlist);

	}
}
