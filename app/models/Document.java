package models;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"number","tab"})
})
public class Document extends Identifier{

	private static String[] cloneProps = {"number", "servicePart", "uom"};
	
	public Document(String number, String tab){
		super(number, tab);	
	}

	public Document(String number) {
		super(number);
		
	}
	
	public Document(){
		
	}

	@Override
	public String getNextTab() {
		Long count = Document.count("number=?", this.number);
		DecimalFormat df = new DecimalFormat(DEFAULT_TAB_FORMAT);
		return df.format(count);
	}

	public Document getTabbedDocument() {
		Document tabbedId = new Document();
		this._copy(tabbedId);
		tabbedId.tab = getNextTab();
		tabbedId.save();
		return tabbedId;
	}
	
	@Override
    public String[] getCopyProperties() {
    	return cloneProps;
    }
}