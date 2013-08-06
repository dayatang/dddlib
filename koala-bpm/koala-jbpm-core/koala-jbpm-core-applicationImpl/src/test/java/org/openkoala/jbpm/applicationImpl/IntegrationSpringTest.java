package org.openkoala.jbpm.applicationImpl;

import java.io.IOException;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openkoala.jbpm.applicationImpl.util.KoalaWSHumanTaskHandler;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IntegrationSpringTest {
	public void testSpring() throws IOException {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("HelloWorld.bpmn2"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("humantaskflow.bpmn"), ResourceType.BPMN2);
        
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/root.xml");
		KnowledgeBase kbase = (KnowledgeBase) context.getBean("kbase");
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		StatefulKnowledgeSession ksession = (StatefulKnowledgeSession) context.getBean("ksession");
		KoalaWSHumanTaskHandler humanTaskHandler = (KoalaWSHumanTaskHandler)context.getBean("humanTaskHandler");
		
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
		
		JPAWorkingMemoryDbLogger logger = new JPAWorkingMemoryDbLogger(ksession);
		
		ksession.startProcess("com.sample.bpmn.hello");
		
		logger.dispose();
		ksession.dispose();
		
		
		try {
			humanTaskHandler.dispose();
		} catch (Exception e) {
			
		}
		

		/*
		 * org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
		 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
		 */
	}

	public static void main(String[] args) {
		IntegrationSpringTest test = new IntegrationSpringTest();
		try {
			test.testSpring();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
}
