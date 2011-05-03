package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@Entity
public class ViewDefinitionContext extends TemporalModel{
	
	@Required
	@MaxSize(100)
	public String name;
	/**
	 * the text that identifies the application context that bounds the universe of discourse. 
	 * 'assembly study', 'digital mock-up', 'electrical design', 'mechanical design', 'preliminary design',
	 * 'process planning' are examples of application domains
	 */
	@Required
	@MaxSize(100)
	public String applicationDomain;
	
	/**
	 * the text that identifies a stage in the life cycle of a product. 
	 * 'design phase', 'production', 'recycling phase' are examples of life cycle stages.
	 */
	public String releaseState;
	
	/**
	 * 'As Saved','As Built', 'As Designed', 'As Planned', 'As Shipped'
	 */
	public String configuration;
	
	@Lob
    @Required
    @MaxSize(500)
    public String description;
	
	
	public String toString(){
		return this.name;
	}

}
