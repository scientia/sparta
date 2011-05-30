package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.MappedSuperclass;

import models.AuthoredTemporalModel;

@MappedSuperclass
public class ItemModel extends AuthoredTemporalModel {

	
	public List<File> getFiles(String entityClass){		
		List<ItemFile> itemfiles = ItemFile.find("byEntityIdAndEntityClass", this._key(), entityClass).fetch();
		List<File> files = new ArrayList<File>();
		for(ItemFile itemfile: itemfiles){
			files.add(itemfile.file);
		}	
		return files;
	}
}
