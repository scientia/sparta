package models;

import java.util.ArrayList;
import java.util.List;

public class StructureTree{

	private ProductVersion rootProduct;
	
	private StructureNode rootElement;
	
	/** default is always uses*/
	private int direction = 1;
	
	public StructureTree() {
		super();
	}
	
	public ProductVersion getRootProduct() {
		return rootProduct;
	}
	
	public void setRootProduct(ProductVersion rootProduct) {
		this.rootProduct = rootProduct;
	}
	
	public StructureNode getRootElement() {
		return rootElement;
	}
	
	public void setRootElement(StructureNode rootElement) {
		this.rootElement = rootElement;
	}
	
	
	
	public List<StructureNode> toList() {
		List<StructureNode> list = new ArrayList<StructureNode>();
        walk(rootElement, list);
        return list;
	}

	
	public String toString(){
		return toList().toString();
	}
	
	 private void walk(StructureNode element, List<StructureNode> list) {
	        list.add(element);
	        for (StructureNode data : element.getChildren()) {
	            walk(data, list);
	        }
	}
}
