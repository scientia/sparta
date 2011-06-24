package controllers;

import models.DesignDocumentVersion;
import models.SpecDocumentVersion;
import controllers.CoreController.For;
import play.mvc.*;

@With(Secure.class)
@For(SpecDocumentVersion.class)
public class SpecDocuments extends Documents{

}
