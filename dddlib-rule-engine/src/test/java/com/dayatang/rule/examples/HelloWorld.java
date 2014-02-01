package com.dayatang.rule.examples;

import org.drools.core.audit.WorkingMemoryFileLogger;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class HelloWorld {

	public static final void main(final String[] args) throws Exception {
		
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.getKieClasspathContainer();
		KieBase kBase1 = kContainer.getKieBase("rules");
		KieSession session = kContainer.newKieSession("ksession-stateful");

		session.addEventListener(new DebugAgendaEventListener());
		session.addEventListener(new DebugRuleRuntimeEventListener());

		
		
		final WorkingMemoryFileLogger logger = new WorkingMemoryFileLogger();
		logger.setFileName("log/helloworld");

		final Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		session.insert(message);

		session.fireAllRules();

		logger.writeToDisk();

		session.dispose();
	}

	public static class Message {
		public static final int HELLO = 0;
		public static final int GOODBYE = 1;

		private String message;

		private int status;

		public Message() {

		}

		public String getMessage() {
			return this.message;
		}

		public void setMessage(final String message) {
			this.message = message;
		}

		public int getStatus() {
			return this.status;
		}

		public void setStatus(final int status) {
			this.status = status;
		}
	}
}
