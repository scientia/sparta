package tags;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import models.Lookup;
import models.LookupValue;
import models.UnitOfMeasure;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

@FastTags.Namespace("sparta")
public class SpartaTags extends FastTags {
	
	public static void _lookup(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) {	
		String realm = (String)args.get("realm");
		String selected = (String)args.get("value");		
		List<LookupValue> lvalues = Lookup.getValues(realm);		
		for(LookupValue lvalue :lvalues){			
			if(!lvalue.code.equals(selected)){				
			    out.println("<option name=\""+lvalue.code+"\" value=\""+ lvalue.code+"\" >"+lvalue.value+"</option>");
			}else{				
				out.println("<option name=\""+lvalue.code+"\"selected=\"true\" value=\""+ lvalue.code+"\" >"+lvalue.value+"</option>");
			}			
		}		
	}

	public static void _lookupValue(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) {		
		String realm = (String)args.get("realm");
		String code = (String)args.get("code");		
		String value = LookupValue.getValueFromCode(realm, code);	
		out.println(value);		
	}
	

	public static void _unitOfMeasure(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) {	
		String selected = (String)args.get("value");
		List<UnitOfMeasure> uoms = UnitOfMeasure.findAll();
		for(UnitOfMeasure uom :uoms){			
			if(!uom.code.equals(selected)){				
			    out.println("<option name=\""+uom.code+"\" value=\""+ uom.code+"\" >"+uom.name+"</option>");
			}else{				
				out.println("<option name=\""+uom.code+"\"selected=\"true\" value=\""+ uom.code+"\" >"+uom.name+"</option>");
			}			
		}		
	}
	

	public static void _uomValue(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) {								
	}
	
}
