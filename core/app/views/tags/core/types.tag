%{
    models = [];
    for(controllerClass in play.Play.classloader.getAssignableClasses(_('controllers.CoreController'))) {
        resourceModel = _('controllers.CoreController$ObjectType').get(controllerClass)
        if(resourceModel != null) {
            models.add(resourceModel)
        }
    }
    for(controllerClass in play.Play.classloader.getAssignableClasses(_('play.scalasupport.crud.CRUDWrapper'))) {
        resourceModel = _('controllers.CoreController$ObjectType').get(controllerClass)
        if(resourceModel != null) {
            models.add(resourceModel)
        }
    }
    java.util.Collections.sort(models)
}%

%{ models.eachWithIndex() { item, i -> }%
	%{
		attrs = [:]
		attrs.put('type', item)
	}%
    #{doBody vars:attrs /}
%{ } }%