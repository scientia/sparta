package models;

import java.util.ArrayList;
import java.util.List;

public class StructureNode {

	/** first level vill not have any parents*/
	public ProductStructure parent;
	
	public List<StructureNode> children;
	
	public StructureNode() {
		super();
	}
	
	public StructureNode(ProductStructure parent){
		setParent(parent);
	}

	public void setParent(ProductStructure parent) {
		this.parent = parent;
	}
	
	public ProductStructure getParent() {
		return parent;
	}
	
	public List<StructureNode> getChildren() {
		if(this.children == null){
			return new ArrayList<StructureNode>();
		}
		return this.children;
	}
	
	public void setChildren(List<StructureNode> children) {
		this.children = children;
	}
	
	public int getNumberOfChilderen(){
		if(this.children == null){
			return 0;
		}
		return this.children.size();	
	}
	
	public void addChild(StructureNode child){
		if(children == null){
			children = new ArrayList<StructureNode>();
		}
		children.add(child);
	}
	
	public void insertChildAt(int index, StructureNode child)  throws IndexOutOfBoundsException {
		if(index == getNumberOfChilderen()){
			addChild(child);
		}else{
			this.children.get(index); //Throw an error if the index is incorrect
			this.children.add(index, child);
		}	
	}
	public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(getParent().toString()).append(",[");
        int i = 0;
        for (StructureNode e : getChildren()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(e.getParent().toString());
            i++;
        }
        sb.append("]").append("}");
        return sb.toString();
    }
	
}
