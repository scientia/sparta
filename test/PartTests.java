import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Query;

import models.Part;
import models.number.PartNumber;
import models.PartVersion;

import org.junit.*;

import play.db.jpa.JPA;
import play.test.Fixtures;
import play.test.UnitTest;


public class PartTests extends UnitTest{

	@Before
    public void setup() {
        Fixtures.deleteAll();
        
    }
	
	@Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
	
	@Test
	public void partNumbrTest(){
		//PartNumber p = new PartNumber("abcd");
		//p.save();
		//assertEquals("3", p.number);
		
	}
	
	@Test
	public void getNextIteration(){
		Part part = new Part("1234563", "02", false).save();
		PartVersion pv = new PartVersion("first part", part).save();
		PartVersion pv2 = pv.clone();
		pv2.save();
		PartVersion pv3 = pv2.clone();
		pv3.save();
		assertEquals(4, pv3.getNextIteration().intValue());
		
	}
	@Test
	public void getNextRevision(){
		Part part = new Part("1234563", "02", false).save();
		PartVersion pv = new PartVersion("first part", part).save();
		PartVersion pv2 = pv.revise();
		pv2.save();
		PartVersion pv3 = pv2.revise();
		pv3.save();
		assertEquals(4, pv3.getNextRevision().intValue());
		
	}
	@Test
	public void partNumberTest(){
		String number = models.number.PartNumber.getPartNumber();
		assertEquals(0, PartNumber.count());
		assertEquals("PRT0000004",number);
		
	}
	
	@Test
	public void partCloneTest(){
		
		Part part = new Part("1234563", "02", false).save();
		PartVersion pv = new PartVersion("first part", part);
		pv.makeOrBuy=true;
		pv.location="abcd";
		pv.save();
		
		
			PartVersion pv2 = pv.clone();
			assertEquals("first part", pv2.name);
			assertEquals("P000012", pv.identifier.number);
			
		
		
	}
	private int safeLongToInt(long n){
		if(n > Integer.MAX_VALUE || n < Integer.MIN_VALUE){
			throw new IllegalArgumentException(n + "cannot be converted from long to int");
		}
		
		return (int)n;
	}
}
