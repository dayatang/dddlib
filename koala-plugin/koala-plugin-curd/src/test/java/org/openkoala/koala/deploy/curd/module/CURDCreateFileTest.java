package org.openkoala.koala.deploy.curd.module;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.deploy.curd.generator.CodeGenerator;
import org.openkoala.koala.deploy.curd.module.analysis.CURDCoreAnalysis;
import org.openkoala.koala.deploy.curd.module.analysis.CURDDefaultUIView;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.pojo.NewFile;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.util.ZipToFile;
import org.openkoala.koala.pojo.MavenProject;

public class CURDCreateFileTest {

	private String entitySrcPath;
	private String testProjectPath;
	private CURDCoreAnalysis analysis;

	@Before
	public void setUp() throws Exception {
		testProjectPath = getTestProjectPath();
		unZipToDir();
		entitySrcPath = getEntitySrcPath();
		analysis = CURDCoreAnalysis.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		removeTestProject(getUnZipTestProjectPath());
	}

	@Test
	public void testCreateFile() throws Exception {
		EntityModel entityModel = analysis.analysis(entitySrcPath);
		EntityViewUI entityViewUI = CURDDefaultUIView.getDefaultEntityViewUI(entityModel);
		MavenProject mavenProject = analysis.getProject();
		List<NewFile> newFiles = CodeGenerator.getGenerateFiles(entityViewUI, mavenProject);
		assertEquals(8, newFiles.size());
		CodeGenerator.generateCode(entityViewUI, mavenProject, newFiles);
		checkCreateFileIsExists();
	}

	private void checkCreateFileIsExists() {
		String dtoFile = getUnZipToDir() + "/demo/demo-application/src/main/java/org/openkoala/demo/application/dto/TestDTO.java";
		String applicationFile = getUnZipToDir() + "/demo/demo-application/src/main/java/org/openkoala/demo/application/core/TestApplication.java";
		String applicationImplFile = getUnZipToDir() + "/demo/demo-applicationImpl/src/main/java/org/openkoala/demo/application/impl/core/TestApplicationImpl.java";
		String controllerFile = getUnZipToDir() + "/demo/demo-web/src/main/java/org/openkoala/demo/web/controller/core/TestController.java";
		checkFileIsExist(dtoFile);
		checkFileIsExist(applicationFile);
		checkFileIsExist(applicationImplFile);
		checkFileIsExist(controllerFile);
	}
	
	private void checkFileIsExist(String filePath) {
		File file = new File(filePath);
		assertEquals(true, file.exists());
	}

	private String getCurrentProjectPath() {
		return System.getProperty("user.dir");
	}

	private String getTestProjectPath() {
		return getCurrentProjectPath() + "/src/test/resources/project/demo.zip";
	}

	private String getUnZipToDir() {
		return getCurrentProjectPath() + "/target/";
	}

	private String getEntitySrcPath() {
		return getUnZipToDir() + "/demo/demo-core/src/main/java/org/openkoala/demo/core/Test.java";
	}
	
	private String getUnZipTestProjectPath() {
		return getUnZipToDir() + "/demo";
	}

	private void unZipToDir() {
		try {
			ZipToFile.upZipFile(testProjectPath, getUnZipToDir());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeTestProject(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			if (file.listFiles().length == 0) {
				file.delete();
			} else {
				File delFile[] = file.listFiles();
				int fileNum = file.listFiles().length;
				for (int j = 0; j < fileNum; j++) {
					if (delFile[j].isDirectory()) {
						removeTestProject(delFile[j].getAbsolutePath());
					}
					delFile[j].delete();
				}
			}
		}
	}

}
