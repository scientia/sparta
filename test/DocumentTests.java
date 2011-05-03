
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import models.DocumentVersion;
import models.Part;
import models.PartVersion;

import org.junit.Test;

import play.db.jpa.JPA;
import play.test.UnitTest;


public class DocumentTests extends UnitTest{

	
	@Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
	
	@Test
	public void stringTrimming(){		
		String abcd = "1,2,3,4,5,6, ";
		
		List<String> ids = Arrays.asList(abcd.trim().split("\\s*" + ","+"\\s*"));
		assertEquals(6, ids.size());
		assertEquals(ids.get(5), "6");
		
	}
	
	public void lisdocs(){
		Long id1 = new Long(1);
		Long id2 = new Long(2);
		List<DocumentVersion> docs = DocumentVersion.find("select d from DocumentVersion d where p.id = :id? or p.id =?","1","2").fetch();
		
		assertEquals(2, docs.size());
	}
	
	
	public  void relIdsToDocuments(){
		Part p = new Part("12345", "01", false).save();
		PartVersion pv= new  PartVersion("abcd", p).save();
		String idlist = "1,2,3,5";
		List<String> ids = Arrays.asList(idlist.trim().split("\\s*" + ","+"\\s*"));
		assertEquals(4, ids.size());
		List<DocumentVersion> models = new ArrayList<DocumentVersion>();
		assertEquals("1", ids.get(0));
		for(String id : ids){
			
			DocumentVersion dv = DocumentVersion.findById(Long.parseLong(id));
			pv.addDocument(dv);
			models.add(dv);
		}
	    pv.save();
	   // assertEquals(4, pv.documents.size());	    
	}
}
