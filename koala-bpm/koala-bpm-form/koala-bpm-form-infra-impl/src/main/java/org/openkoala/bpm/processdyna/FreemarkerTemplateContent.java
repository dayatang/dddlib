package org.openkoala.bpm.processdyna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.inject.Named;

import org.openkoala.bpm.processdyna.infra.TemplateContent;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Named("templateContent")
public class FreemarkerTemplateContent implements TemplateContent {
	
	private static InputStream templateFileInputStream;
	private static BufferedReader templateFileBufferedReader;
	
	private final static String[] templateFilePaths = {
			"/widget/Checkbox.ftl",
			"/widget/Date.ftl",
			"/widget/DateTime.ftl",
			"/widget/DropDown.ftl",
			"/widget/File.ftl",
			"/widget/Password.ftl",
			"/widget/Radio.ftl",
			"/widget/Text.ftl",
			"/widget/TextArea.ftl",
			"/widget/Time.ftl"
	};

	public String process(Object params, String templateData) {
		try {
			Configuration cfg = new Configuration();
			StringTemplateLoader stringTemplate = new StringTemplateLoader();
			stringTemplate.putTemplate("INIT", templateData);
			cfg.setTemplateLoader(stringTemplate);
			
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate("INIT");
			
			StringWriter writer = new StringWriter();
			template.process(params, writer);
			String result = writer.toString();
			
			String marcoTempate = result  + marcoData;
			stringTemplate.putTemplate("PROCESS", marcoTempate);
			cfg.setTemplateLoader(stringTemplate);
			template = cfg.getTemplate("PROCESS");
			StringWriter processWrite = new StringWriter();
			template.process(params, processWrite);
			String htmlContent = processWrite.toString();
			
			return htmlContent;
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String marcoData;

	static {
		StringBuffer string = new StringBuffer();
		try {
			/*
			 //当获取资源路径的代码打入jar包时，不能通过以下方式获取资源路径
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Checkbox.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Date.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/DateTime.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/DropDown.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/File.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Password.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Radio.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Text.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/TextArea.ftl").getPath())));
			string.append(FileUtils.readFileToString(new File(
					FreemarkerTemplateContent.class.getClassLoader()
					.getResource("widget/Time.ftl").getPath())));
			*/
			for(String templateFilePath : templateFilePaths){
				templateFileInputStream=FreemarkerTemplateContent.class.getResourceAsStream(templateFilePath);
				templateFileBufferedReader=new BufferedReader(new InputStreamReader(templateFileInputStream)); 
				String templateFileEveryLine = "";
				while((templateFileEveryLine=templateFileBufferedReader.readLine())!=null){
					string.append(templateFileEveryLine);
				}
				closeStream();
			}
			
			marcoData = string.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void closeStream() throws IOException{
		templateFileBufferedReader.close();
		templateFileInputStream.close();
	}

}
