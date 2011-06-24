package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bom{

	public static int STRUCTURE_USEDON = 0;
	public static int STRUCTURE_USES = 1;
	
	
	private int direction = 1;
	public Bom(int direction) {
		this.direction = direction;
	}
	
	public  StructureTree get(PartVersion rootPart){
		StructureTree tree = new StructureTree();		
		tree.setRootProduct(rootPart);
		StructureNode rootElement = new StructureNode();
        tree.setRootElement(rootElement);        
		getRecursive(rootPart,rootElement, tree);				
		return tree;
	}

	private void getRecursive(PartVersion rootPart,StructureNode rootElement, StructureTree tree) {
		List<PartStructure> children ;
		if(direction == 0){
		  children  = rootPart.getUsedOnPartsStructures();
		}else{
			children = rootPart.getUsesParts();
		}
		
        List<StructureNode> childElements = new ArrayList<StructureNode>();
        for (Iterator<PartStructure> it = children.iterator(); it.hasNext();) {
        	PartStructure childStructure = it.next();
        	PartVersion childPart;
        	if(direction == 0){
        		childPart = childStructure.usedOn;
        	}else{
        		childPart  = childStructure.uses;
        	}
            StructureNode childElement = new StructureNode(childStructure);
            childElements.add(childElement);
            getRecursive(childPart,childElement, tree);
        }
        rootElement.setChildren(childElements);
	}

	
	
}
