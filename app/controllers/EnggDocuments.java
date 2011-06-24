package controllers;

import models.EngineeringDocumentVersion;
import controllers.CoreController.For;
import play.mvc.*;

@With(Secure.class)
@For(EngineeringDocumentVersion.class)
public class EnggDocuments extends Documents{

}
