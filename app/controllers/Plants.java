package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Plant;
import play.mvc.With;

@With(Secure.class)
public class Plants extends CoreController{

	public static void listplants() {
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR + queryIn + LIKE_CHAR;
		List<Plant> plants;

		if (queryLike == null) {
			plants = Plant.findAll();
		} else {
			plants = Plant.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List plantlist = new ArrayList();
		for (Plant plant : plants) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", plant.id.toString());
			smap.put("name", plant.name);
			plantlist.add(smap);
		}
		renderJSON(plantlist);
	}
}
