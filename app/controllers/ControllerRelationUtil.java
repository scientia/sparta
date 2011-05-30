package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Customer;
import models.Plant;
import models.Supplier;

/**
 * This could be better with the direct binding
 * @author snehal
 *
 */
public class ControllerRelationUtil {

	public static List<Plant> relIdsToPlants(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Plant> models = new ArrayList<Plant>();
		for(String id : ids){		
			Plant dv = Plant.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	//TODO: this could be done with a type binder
	protected static List<String> stringToArrayList(String idString){
		return Arrays.asList(idString.trim().split("\\s*" + ","+"\\s*"));
	}
	
	public static List<Supplier> relIdsToSuppliers(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Supplier> models = new ArrayList<Supplier>();
		for(String id : ids){		
			Supplier dv = Supplier.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	public static List<Customer> relIdsToCustomers(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Customer> models = new ArrayList<Customer>();
		for(String id : ids){		
			Customer dv = Customer.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
}
