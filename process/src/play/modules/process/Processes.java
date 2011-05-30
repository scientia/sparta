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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;

import play.Logger;
import play.Play;
import play.Play.Mode;
import play.vfs.VirtualFile;

/**
 * Permissions checker that uses a JBoss Rules stateless session to perform the check.
 */
public class Processes {

	private static final String RULES_FILE_NAME = "process.drl";
	private static KnowledgeBase knowledgeBase;
	private static String rulesFileName;
	private static Long rulesLastModified;
	private static String rulesMD5;

	private Processes() {
	}

	static void maybeLoadKnowledgeBase(){
		String newRulesFileName = Play.configuration.getProperty("process.definations");
		boolean defaultUsed = false;
		if(newRulesFileName == null){
			newRulesFileName = RULES_FILE_NAME;
			defaultUsed = true;
		}
		VirtualFile rulesFile = Play.getVirtualFile("conf/"+newRulesFileName);

		if(rulesFile == null || !rulesFile.exists()){
			if(defaultUsed){
				Logger.warn("No process file specified and none found. ");
				knowledgeBase = null;
				return;
			}
			// throw only if one was specified and is missing
			throw new RuntimeException("Rules file conf/"+newRulesFileName+" does not exist");
		}
		if(Play.mode == Mode.PROD){
			rulesFileName = newRulesFileName;
			loadKnowledgeBase(rulesFile.content());
		}else{
			// see if the file changed
			Long newLastModified = rulesFile.lastModified();
			byte[] content = rulesFile.content();
			String newMD5 = DigestUtils.md5Hex(content);
			if(rulesFileName == null || !rulesFileName.equals(newRulesFileName)
					|| rulesLastModified == null || !newLastModified.equals(rulesLastModified)
					|| rulesMD5 == null || !newMD5.equals(rulesMD5)){
				Logger.info("old file: %s, new file: %s, old modified: %s, new modified: %s, old md5: %s, new md5: %s",
						rulesFileName, newRulesFileName, rulesLastModified, newLastModified, rulesMD5, newMD5);
				rulesFileName = newRulesFileName;
				rulesLastModified = newLastModified;
				rulesMD5 = newMD5;
				loadKnowledgeBase(content);
			}
		}
	}

	/**
	 * Load and compile the rules definitions.
	 */
	private static void loadKnowledgeBase(byte[] rulesContent) {
		Logger.info("Loading rules from %s", rulesFileName);
	    
		// Configure the drools compiler to use Janino, instead of JDT, with the Play classloader, so that compilation will load 
		// model classes from the classloader, and not as .class file resources. https://jira.jboss.org/browse/JBRULES-1229 
		Properties properties = new Properties();
		properties.put("drools.dialect.java.compiler", "JANINO");
		// this is needed because it's not set in prod
		Thread.currentThread().setContextClassLoader(Play.classloader);
		final KnowledgeBuilderConfiguration configuration = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(properties, Play.classloader);
		final KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder(configuration);
		
		// Compile the process file.
		builder.add(ResourceFactory.newByteArrayResource(rulesContent), ResourceType.DRF);
		if (builder.hasErrors()) {
			Logger.error(builder.getErrors().toString());
			throw new RuntimeException("Drools process compilation failed: " + builder.getErrors().size() + " errors");
		}
		
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
	}

	public static void start(){
		StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
		// start a new process instance
		ksession.startProcess("com.sample.ruleflow");
		logger.close();
	}

	/**
	 * Wraps the given user name in a Principal object.
	 */
	private static Principal asPrincipal(final String user) {
		final Principal principal = new Principal() {
			public String getName() {
				return user;
			}
		};
		return principal;
	}


	private static class AgendaLogger extends DefaultAgendaEventListener {
		@Override
		public void afterActivationFired(AfterActivationFiredEvent event) {
			Logger.debug("RULE '%s' %s", event.getActivation().getRule().getName(), event.getActivation().getFactHandles());
		}
	}

}
