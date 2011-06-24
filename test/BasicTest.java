import org.junit.*;

import java.util.*;
import play.test.*;
import models.Lookup;
import models.LookupValue;
import models.Part;
import models.PartStructure;
import models.PartVersion;
import models.Role;
import models.User;

public class BasicTest extends UnitTest {

	@Before
    public void setup() {
        Fixtures.deleteAll();
    }

	
   // @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    
   //// @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
    	User u = new User();
        u.username="bob";
        u.email="bob@gmail.com";
        u.password= "secret";
        u.save();
        
        // Retrieve the user with e-mail address bob@gmail.com
        User bob = User.find("byEmail", "bob@gmail.com").first();
        
        // Test 
        assertNotNull(bob);
        assertEquals("Bob Mason", bob.toString());
    }
   
    //@Test
    public void createAndAddProfile(){
    	
    	        // Create a new user and save it
    	    	new Role("admin").save();
    	    	
    	        User u = new User();
    	        u.username="bob";
    	        u.email="bob@gmail.com";
    	        u.password= "secret";
    	        u.save();
    	        // Retrieve the user with e-mail address bob@gmail.com
    	        User bob = User.find("byEmail", "bob@gmail.com").first();
    	        
    	        
    	        // Test 
    	        assertNotNull(bob);
    	        assertEquals("Bob", bob.toString());
    	        assertNotNull(Role.findByName("admin"));
    	       bob.addRole("admin");
    	       bob.save();
    	       
    	       assertEquals(1, User.findUsersWithProfile("admin").size() ); 
    }
   
   // @Test
    public void userHasProfile(){
    	new Role("admin").save();
    	
    	assertNotNull(Role.findByName("admin"));
    	User u1 = new User("bob","bob@gmail.com", "secret");
    	u1.addRole("admin");
    	u1.save();
    	User bob = User.find("byEmail", "bob@gmail.com").first();
    	List<Role> pfs = bob.getRoles();
    	boolean isIt = pfs.contains(new Role("admin")) ;
    	assertTrue(isIt);
    	
    }
    
   // @Test
    public void testPartCreate(){
    	Part p = new Part("12345").save();
    	
    	new PartVersion("abcd", p).save();
    	PartVersion p1 = PartVersion.find("byName", "abcd").first();
    	assertNotNull(p1);
    	assertNotNull(p1.identifier);
    	assertEquals("12345", p1.identifier.number );
    	
    	Part p2 = Part.find("byNumber", "12345").first();
    	assertEquals("12345", p2.number);
    }
    
    //@Test
    public void testLookups(){
    	Lookup l = new Lookup("product.finish", "string", "value", "code", true).save();
    	LookupValue lv = new LookupValue(l, "half", "Half").save();
    	
    	List<LookupValue> lvs = LookupValue.find("byLookup", l).fetch();    	
    	assertEquals(1, lvs.size());
    }
    
    public void testPartIdentifier(){
    
    	
    	Part part = new Part("123456", "02", false).save();
		PartVersion partVersion = new PartVersion("First Part", part);
		partVersion._save();
		
		PartVersion pv = PartVersion.find("byIdentifier", part).first();
		assertNotNull(pv);
		assertNotNull(pv.identifier);
		assertEquals("123456", pv.identifier.number);
		
		Part p1 = Part.find("byNumber", "123456").first();
		assertEquals("123456", p1.number);
		//p1.revisionsOfPart.add(pv);
		p1.save();
		/*Set<PartVersion> versions = p1.revisionsOfPart;
		assertEquals(1,versions.size());
		assertEquals("First Part", versions.iterator().next().name);*/
		
    }
    
   // @Test
    public void testStructure(){
    	PartVersion p1 = new PartVersion();
    	p1.name = "first part";
    	p1.save();
    	
    	PartVersion p2 = new PartVersion();
    	p2.name = "second part";
    	p2.save();
    	
    	/*PartStructure st = p1.addParts(p2);
    	assertNotNull(st);
    	assertEquals("second part", st.uses.name);
    	assertEquals("first part", st.usedOn.name);
    	assertEquals(0, st.quantity, 0);
    	st.quantity = 4.5;
    	st.save();
    	assertEquals(4.5, st.quantity, 0);*/
    	
    	//List<PartStructure> structs = PartStructure.find("byUsedOn", p1).fetch(); 
    	//assertNotNull(structs);
    	//assertEquals(1, structs.size());
    	//List<PartVersion> parts = p1.getUsesParts();
    	//assertEquals(1, parts.size());
    	List<PartVersion> usedons = p2.getUsedOnParts();
    	assertEquals(1, usedons.size());
    	//assertEquals("second part",parts.get(0).name);
    	//assertEquals("first part",usedons.get(0).name);
    }
    
    @Test
    public void testLookup(){
    	Lookup lp = new Lookup("abcd", "String", "String", "", false).save();
    	
    	LookupValue lv = new LookupValue(lp, "ea", "EACH").save();
    	lv = new LookupValue(lp, "kg", "KG").save();
    	lv = new LookupValue(lp, "mm", "MM").save();
    	
    	
    	String value = LookupValue.getValueFromCode("abcd", "mm");
    	
    	assertEquals("MM", value);
    }
}
