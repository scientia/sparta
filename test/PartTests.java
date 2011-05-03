import java.util.List;

import javax.persistence.Query;

import models.Part;
import models.PartNumber;
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
		PartNumber p = new PartNumber("abcd");
		p.save();
		assertEquals("3", p.number);
		
	}
	
	@Test
	public void getNextRevision(){
		Part part = new Part("1234563", "02", false).save();
		PartVersion pv = new PartVersion("first part", part).save();
		PartVersion pv2 = new PartVersion("first part", part);
		pv2.iteration = 3;
		pv2.save();
		
		Query q = JPA.em().createQuery("select max(iteration) from PartVersion");
		Integer a = (Integer)q.getSingleResult();
		assertEquals(3, a.intValue());
		
	}
	
	private int safeLongToInt(long n){
		if(n > Integer.MAX_VALUE || n < Integer.MIN_VALUE){
			throw new IllegalArgumentException(n + "cannot be converted from long to int");
		}
		
		return (int)n;
	}
}
