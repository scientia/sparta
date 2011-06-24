import controllers.Projects;
import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        // Check if the database is empty
        if(User.count() == 0) {
            Fixtures.load("initial-data.yml");
            Fixtures.load("sample-data.yml");
        	Fixtures.load("uoms.yml");
        	
        	loadProductMaterials();
    		loadProductFinish();
    		loadProductLocation();
            loadProductWorkflowStates();
            loadContextAppDomain();
            loadContextReleaseState();
    		loadConfiguration();
            
    		loadProjectPhases();
    		loadProductStages();
    		
    		loadDesignDocumentTypes();
    		loadEngineeringDocumentTypes();
    		loadManufacturingDocumentTypes();
    		loadSupportDocumentTypes();
    		
    		loadECOChangeClasses();
    		loadECRWorkflowStates();
    		loadECOWorkflowStates();
    		
    		loadChangeDisposition();
    		loadItemAffectedType();
    		loadAffectedDisposition();
    		
    		loadTaskWorkflowStatus();
    		
            /*Project pj = Project.find("byName", "Default").first();
            Part p = new Part("123456", "02", false).save();
            for(int i=0 ; i<80; i++){
            	
            	DesignDocumentVersion dv = new DesignDocumentVersion("document "+i);
            	dv.project = pj;            	
            	dv.save();
            	PartVersion pv = new PartVersion("a part"+i, p);
            	pv.project = pj;
            	pv.iteration=i;
            	pv.save();
            }*/
            
        } 
    }


	private void loadConfiguration() {
		Lookup l = new Lookup("sparta.context.configuration", "string", "string", "", false).save();		
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l, "1", "As Saved").save();
		new LookupValue(l, "2", "As Built").save();
		new LookupValue(l, "3", "As Designed").save();
		new LookupValue(l, "4", "As Planned").save();
		new LookupValue(l, "5", "As Shipped").save();		
	}

	private void loadContextReleaseState() {
		Lookup l = new Lookup("sparta.context.releasestate", "string", "string", "", false).save();		
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l, "1", "Design Phase").save();
		new LookupValue(l, "2", "Production").save();
		new LookupValue(l, "3", "Recycling Phase").save();
	}

	private void loadContextAppDomain() {
		Lookup l = new Lookup("sparta.context.applicationdomain", "string", "string", "", false).save();
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l, "1", "Assembly Study").save();
		new LookupValue(l, "2", "Electrical Design").save();
		new LookupValue(l, "3", "Mechanical Design").save();
		new LookupValue(l, "4", "Preliminary Design").save();
		new LookupValue(l, "5", "Process Planning").save();
	}

	private void loadSupportDocumentTypes() {
		Lookup l = new Lookup("sparta.support.doctypes", "string", "string", "", false).save();	
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l,"1","Field Instruction/Bulletin/Kit").save();
		new LookupValue(l,"2","Spare parts list").save();
		new LookupValue(l,"3","Illustrated Parts Catalog").save();  
		new LookupValue(l,"4","Spares Kits").save();
		new LookupValue(l,"5","Product Description Manual").save();  
		new LookupValue(l,"6","Installation Instruction").save();
		new LookupValue(l,"7","Maintenance Manual").save();
	}

	private void loadManufacturingDocumentTypes() {

		Lookup l = new Lookup("sparta.manufacturing.doctypes", "string", "string", "", false).save();
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l,"1","Routing/Process Sheets").save();
		new LookupValue(l,"2","Inspection Process").save();
		new LookupValue(l,"3","Illustrations for Process").save();
		new LookupValue(l,"4","CAM programms").save();
		new LookupValue(l,"5","Tool/Fixture Drawing").save();  
		new LookupValue(l,"6","Test Equipments Drawing").save();
		
	}

	private void loadEngineeringDocumentTypes() {
		Lookup l = new Lookup("sparta.engineering.doctypes", "string",
				"string", "", false).save();
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l, "1", "DFMEA").save();
		new LookupValue(l, "2", "FEA").save();
	}

	private void loadDesignDocumentTypes() {
		Lookup l = new Lookup("sparta.design.doctypes", "string", "string", "",
				false).save();
		new LookupValue(l, "0", "Generic").save();
		new LookupValue(l, "1", "Product Spec").save();
		new LookupValue(l, "2", "Process Spec").save();
		new LookupValue(l, "3", "Customer Spec").save();
		new LookupValue(l, "4", "Material Spec").save();
		new LookupValue(l, "5", "Design Spec").save();
		new LookupValue(l, "6", "Spec Control Drawing").save();
		new LookupValue(l, "7", "Test Spec").save();
		new LookupValue(l, "8", "Source Control Drawing").save();
		new LookupValue(l, "9", "Part Drawing").save();
		new LookupValue(l, "10", "Assembly Drawing /Part List").save();
		new LookupValue(l, "11", "SilkScreen Drawing").save();
		new LookupValue(l, "12", "LogicDiagram").save();
		new LookupValue(l, "13", "PCB ArtWork").save();
		new LookupValue(l, "14", "Padmaster/solder master").save();
		new LookupValue(l, "15", "Cable/Harness Dwg").save();
		new LookupValue(l, "16", "Schematic").save();
		new LookupValue(l, "17", "Wiring from-to list").save();
		new LookupValue(l, "18", "Software Program").save();
		new LookupValue(l, "19", "Prom Truth Table").save();
		new LookupValue(l, "20", "PROM/EPROM Spec").save();
		new LookupValue(l, "21", "CAD").save();
	}

	private void loadProductStages() {
		Lookup l = new Lookup("sparta.product.releasestate", "string",
				"string", "", false).save();
		new LookupValue(l, "design", "Design").save();
		new LookupValue(l, "prototype", "Prototype").save();
		new LookupValue(l, "production", "Production").save();
		new LookupValue(l, "obsolete", "Obsolete").save();
		new LookupValue(l, "recycling", "Recycling").save();
	}

	private void loadProjectPhases() {
		Lookup l = new Lookup("sparta.project.phase", "string", "string", "",
				false).save();
		new LookupValue(l, "I", "Initiating").save();
		new LookupValue(l, "P", "Planning").save();
		new LookupValue(l, "E", "Executing").save();
		new LookupValue(l, "M", "Monitoring").save();
		new LookupValue(l, "C", "Closing").save();
	}

	private void loadProductWorkflowStates() {
		Lookup l = new Lookup("sparta.product.workflowstate", "string", "string", "", false).save();
		new LookupValue(l,"0","Initiated").save();
		new LookupValue(l,"1","Working").save();
	    new LookupValue(l,"2","Pending Approval").save();  
	    new LookupValue(l,"3","Approved").save();
	    new LookupValue(l,"4","Released").save();
	}
	
	private void loadProductLocation() {
		Lookup l = new Lookup("sparta.product.location", "string", "string", "", false).save();		
		new LookupValue(l,"0","Sample Location").save();
	}

	
	private void loadProductFinish() {
		Lookup l = new Lookup("sparta.product.finish", "string", "string", "", false).save();		
		new LookupValue(l,"0","Less Color").save();
	}

	private void loadProductMaterials() {
		Lookup l = new Lookup("sparta.product.material", "string", "string", "", false).save();		
		new LookupValue(l,"0","Sample Material").save();
	}
	

	private void loadECRWorkflowStates() {
		Lookup l = new Lookup("sparta.ecr.workflowstate", "string", "string", "", false).save();
		new LookupValue(l,"0","Initiated").save();
		new LookupValue(l,"1","Reviewing").save();
	    new LookupValue(l,"2","Pending Approval").save();  
	    new LookupValue(l,"3","Approved").save();
	    new LookupValue(l,"4","Working").save();
	    new LookupValue(l,"5","Complete").save();
	    new LookupValue(l,"6","Closed").save();
	}


	private void loadECOChangeClasses() {
		Lookup l = new Lookup("sparta.eco.changeclass", "string", "string", "", false).save();
		new LookupValue(l,"0","Interchangeable").save();
	    new LookupValue(l,"1","Non-Interchangeable").save();
		new LookupValue(l,"2","Document Only").save();		
	    new LookupValue(l,"3","Initial Release").save();
	    new LookupValue(l,"4","Mixed Revision Change").save();
	}

	private void loadECOWorkflowStates() {
		Lookup l = new Lookup("sparta.eco.workflowstate", "string", "string", "", false).save();
		new LookupValue(l,"0","Initiated").save();
		new LookupValue(l,"1","Reviewing").save();
	    new LookupValue(l,"2","Pending Approval").save();  
	    new LookupValue(l,"3","Approved").save();
	    new LookupValue(l,"4","Working").save();
	    new LookupValue(l,"5","Complete").save();
	    new LookupValue(l,"6","Closed").save();
	}

	private void loadChangeDisposition() {
		Lookup l = new Lookup("sparta.change.problemstatus", "string", "string", "", false).save();
		new LookupValue(l,"0","None").save();
		new LookupValue(l,"1","Accepted").save();
		new LookupValue(l,"2","Rejected").save();
	    new LookupValue(l,"3","Next Product").save();
	}

	private void loadItemAffectedType() {
		Lookup l = new Lookup("sparta.change.affecttype", "string", "string", "", false).save();
		new LookupValue(l,"0","Revise").save();
		new LookupValue(l,"1","Tab").save();
		new LookupValue(l,"2","Add").save();
	    new LookupValue(l,"3","Delete").save();
	}
	private void loadAffectedDisposition(){
		Lookup l = new Lookup("sparta.change.partdisposition", "string", "string", "", false).save();		
		new LookupValue(l,"0","Use As Is").save();
		new LookupValue(l,"1","Return to Vendor").save();
		new LookupValue(l,"2","Reworkable").save();
	    new LookupValue(l,"3","Manufacturing Rework").save();
	}
	
	private void loadTaskWorkflowStatus() {
		Lookup l = new Lookup("sparta.tasks.workflowstatus", "string", "string", "", false).save();		
		new LookupValue(l,"0","Assigned").save();
		new LookupValue(l,"1","Accepted").save();
		new LookupValue(l,"2","Rejected").save();
	    new LookupValue(l,"3","Cancelled").save();
	    new LookupValue(l,"4","Complete").save();
		
	}
}