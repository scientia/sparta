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
            
            Part p = new Part("123456", "02", false).save();
            for(int i=0 ; i<80; i++){
            	
            	new DesignDocumentVersion("12345"+i,"document "+i).save();
            	PartVersion pv = new PartVersion("a part"+i, p);
            	pv.iteration=i;
            	pv.save();
            }
            
        }
        
        
    }
}