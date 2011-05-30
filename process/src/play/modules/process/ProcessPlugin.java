/*
    This file is part of secure-permissions-play-module.
    
    Copyright Lunatech Research 2010

    secure-permissions-play-module is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    secure-permissions-play-module is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Lesser Public License
    along with secure-permissions-play-module.  If not, see <http://www.gnu.org/licenses/>.
*/
package play.modules.process;

import play.Logger;
import play.PlayPlugin;

public class ProcessPlugin extends PlayPlugin {
	@Override
	public void onConfigurationRead() {
		Logger.info("Reloading processes");
		Processes.maybeLoadKnowledgeBase();
	}
	
	@Override
	public void detectChange() {
		Processes.maybeLoadKnowledgeBase();
	}
}
