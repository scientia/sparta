package controllers;

import models.Lookup;
import models.LookupValue;
import controllers.CoreController.For;
import play.mvc.With;

@With(Secure.class)
@For(LookupValue.class)
public class LookupValues extends CoreController{

}
