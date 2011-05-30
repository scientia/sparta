package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import play.Play;
import play.db.Model.BinaryField;
import play.exceptions.UnexpectedException;
import play.libs.Codec;
import play.libs.IO;

/**
 * This is copy of play.db.jpa.Blob with few space and plupload related modification.
 * @author snehal
 *
 */
public class FileContent implements BinaryField, UserType {

	private static String MAPPEDDIR_COLUMN_NAME= "MAPPEDDIR";
    private String UUID;
    private String type;
    private File file;
    private String spacePath;

    public FileContent() {}

    private FileContent(String UUID, String type) {
        this.UUID = UUID;
        this.type = type;
    }
    
    private FileContent(String UUID, String type, String mappedDir) {
        this.UUID = UUID;
        this.type = type;
        this.spacePath = mappedDir;
    }
    public InputStream get() {
        if(exists()) {
            try {
                return new FileInputStream(getFile());
            } catch(Exception e) {
                throw new UnexpectedException(e);
            }
        }
        return null;
    }
    /**
     * The input stream is file contents uploaded.
     * tempname is the name plupload has renamed the file to.
     * type is content type extracted from the request.
     * 
     * @param is
     * @param tempName
     * @param type
     */
    public void set(InputStream is, String tempName, String type ) {
        this.UUID = tempName;
        this.type = type;
        IO.write(is, getFile());
    }
    
    public void set(InputStream is, String tempName, String type, String spacePath ) {
        this.UUID = tempName;
        this.type = type;
        this.spacePath = spacePath;
        IO.write(is, getFile());
    }
    
    public void set(InputStream is, String type) {
        this.UUID = Codec.UUID();
        this.type = type;
        IO.write(is, getFile());
    }

    public long length() {
        return getFile().length();
    }

    public String type() {
        return type;
    }

    public boolean exists() {
        return UUID != null && getFile().exists();
    }

    public File getFile() {
        if(file == null) {
            file = new File(getStore(), UUID);
        }
        return file;
    }

    //

    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }

    public Class returnedClass() {
        return FileContent.class;
    }

    public boolean equals(Object o, Object o1) throws HibernateException {
        return o == null ? false : o.equals(o1);
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    // TODO: After we switch to Hibernate 3.6, Hibernate.STRING must be changed to
    // Hibernate.StringType.INSTANCE (how stupid is that to deprecate stuff before offering
    // an alternative?
    @SuppressWarnings("deprecation")
    public Object nullSafeGet(ResultSet rs, String[] names, Object o) throws HibernateException, SQLException {
        String val = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        if(val == null || val.length() == 0 || !val.contains("|")) {
            return new FileContent();
        }
        String mapDir = getMapDir(rs);     
       //String colName= rs.getString(15);      
       //Object object =   rs.getObject(15);
        return new FileContent(val.split("[|]")[0], val.split("[|]")[1], mapDir);
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException 
     */
    //TODO: need to revisit this to find a better way to get the space object
    // and the mapped directory
    private String getMapDir(ResultSet rs) throws SQLException {
		
    	ResultSetMetaData meta = rs.getMetaData();
    	
    	/** 
    	 * Pass null map directory.
    	 * This is handled in the getStore() method.
    	 * if it is null default store will be used.
    	 */
        String mapDir = null;
        
        for(int i =1; i <= meta.getColumnCount(); i++){
    	   String name = meta.getColumnName(i);
    	   if(name.equals(MAPPEDDIR_COLUMN_NAME)){
    		 return (String)rs.getObject(i);    		  
    	   }    	      	  
        }
	    return mapDir;
	}

	public void nullSafeSet(PreparedStatement ps, Object o, int i) throws HibernateException, SQLException {
        if(o != null) {
            ps.setString(i, ((FileContent)o).UUID + "|" + ((FileContent)o).type);
        } else {
            ps.setNull(i, Types.VARCHAR);
        }
    }

    public Object deepCopy(Object o) throws HibernateException {
        if(o == null) {
            return null;
        }
        return new FileContent(this.UUID, this.type);
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble(Object o) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object assemble(Serializable srlzbl, Object o) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //

    public static String getUUID(String dbValue) {
       return dbValue.split("[|]")[0];
    }

    public File getStore() {
    	String name = Play.configuration.getProperty("attachments.path", "attachments");
    	if(spacePath != null){
    		name = spacePath;
    	}       
        File store = null;
        if(new File(name).isAbsolute()) {
            store = new File(name);
        } else {
            store = Play.getFile(name);
        }
        if(!store.exists()) {
            store.mkdirs();
        }
        return store;
    }
}
