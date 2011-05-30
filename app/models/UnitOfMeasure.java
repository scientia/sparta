package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"name", "code"})
})
@Entity
public class UnitOfMeasure extends TemporalModel {

	@Required
	@Column(nullable=false)
	@MaxSize(10)
	@MinSize(1)
	@Match("[a-zA-Z][a-zA-Z_0-9()\\.]*")
	public String code;
	
	@Required
	@MaxSize(30)
	@MinSize(1)
	public String name;
	public BigDecimal multiplier;
	
	@Match("(Universal|Metric|British)")
	public String measure;
	
	public String scale;
	
	public UnitOfMeasure(){
		
	}
}
