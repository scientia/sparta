package controllers;

import models.ViewDefinitionContext;
import play.mvc.With;
import controllers.CoreController.For;
@With(Secure.class)
@For(ViewDefinitionContext.class)
public class ViewContexts extends CoreController {

}
