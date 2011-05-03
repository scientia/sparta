package controllers;

import models.MfgDocumentVersion;
import controllers.CoreController.For;
import play.mvc.*;

@With(Secure.class)
@For(MfgDocumentVersion.class)
public class MfgDocuments extends Documents{

}
