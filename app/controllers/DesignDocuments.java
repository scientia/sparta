package controllers;

import java.util.List;
import java.util.Set;

import models.DesignDocumentVersion;
import models.DocumentVersion;
import models.PartVersion;
import controllers.CoreController.For;
import controllers.CoreController.ObjectType;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@With(Secure.class)
@For(DesignDocumentVersion.class)
public class DesignDocuments extends Documents{

	
}
