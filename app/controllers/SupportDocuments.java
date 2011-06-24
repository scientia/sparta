package controllers;

import models.DesignDocumentVersion;
import models.SupportDocumentVersion;
import controllers.CoreController.For;
import play.mvc.*;

@With(Secure.class)
@For(SupportDocumentVersion.class)
public class SupportDocuments extends Documents{

}
