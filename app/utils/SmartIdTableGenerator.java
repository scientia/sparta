package utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;

/**
 * 
 * @author MertAk
 */
public class SmartIdTableGenerator extends org.hibernate.id.TableGenerator {

	private DecimalFormat format;

	public void configure(Type type, Properties params, Dialect dialect) {
		super.configure(type, params, dialect);

		String formatPattern = params.getProperty("format");
		if (formatPattern != null)
			format = new DecimalFormat(formatPattern);
	}

	public synchronized Serializable generate(SessionImplementor session,
			Object object) throws HibernateException {
		Serializable generated = super.generate(session, object);
		String v = null;
		if (generated instanceof String)
			generated = Long.parseLong((String) generated);
		if (generated instanceof Number) {
			if (format != null)
				v = format.format(generated);
			else
				v = String.valueOf(generated);
		} else
			v = (String) generated;

		return v;
	}

}