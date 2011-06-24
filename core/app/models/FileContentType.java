package models;

/**
 * A File identifies a file stored on a computer system or in a stack of non-digital documents.
 *  A File is either a Hardcopy or a Digital_file. 
 *  Examples:
 *  'geometry': The file contains a shape model;
 *  'NC data': The file contains numerical control data;
 *  'FE data': The file contains finite element data;
 *  'sample data': The file represents measured data;
 *  'process plan': The document file represents process planning data;
 *  'check plan': The document file represents quality control planning data;
 *  'drawing': The document file represents a technical drawing.
 */
public enum FileContentType {
	
	GEOMETRY("geometry"),
    NCDATA("NC data"),
    FEDATA("FE data"),
    SAMPLEDATA("sample data"),
    PROCESSPLAN("process plan"),
    CHECKPLAN("check plan"),
	DRAWING("drawing");

    final String name;

    FileContentType(String name){
        this.name = name;
    }

    public String toString() {
        return name;
    }
}