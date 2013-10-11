package org.openkoala.koala.deploy.curd.module.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.core.RelationModel;
import org.openkoala.koala.deploy.curd.module.core.model.ObjectValueFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PKFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PersistenceFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;
import org.openkoala.koala.deploy.curd.module.core.relation.ManyToManyRelationModel;
import org.openkoala.koala.deploy.curd.module.core.relation.ManyToOneRelationModel;
import org.openkoala.koala.deploy.curd.module.core.relation.OneToManyRelationModel;
import org.openkoala.koala.deploy.curd.module.core.relation.OneToOneRelationModel;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.maven.MavenExcuter;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;
import org.openkoala.koala.util.ProjectParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 
 * 类    名：CoreAnalysis.java
 *   
 * 功能描述：领域核心的分析逻辑，完成领域到MODEL的建模工作
 *  
 * 创建日期：2013-1-21上午10:02:53     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
@SuppressWarnings("restriction")
public class CURDCoreAnalysis {
    
    /*项目的类加载器*/
    private CURDClassLoader curdClassLoader;
    
    private static final Logger logger = LoggerFactory.getLogger(CURDCoreAnalysis.class);
    
    /*存储已经解析过的实体类*/
    private Map<String,EntityModel> entityMap = new HashMap<String,EntityModel>();
    
    private MavenProject project;
    
    private  CURDCoreAnalysis() {
        
    }
    
    public static CURDCoreAnalysis getInstance(){
        return new CURDCoreAnalysis();
    }
    
    /**
     * 传入选中的JAVA源文件的完整路径，对此源文件进行领域建模分析
     * @param srcJava
     */
    public EntityModel analysis(String srcPath){
        
        String projectPath = null;
        String srcJava = srcPath.substring(srcPath.indexOf("/src/main/java")+1+"/src/main/java".length(),srcPath.lastIndexOf(".java"));
        logger.info("要分析的实体类是:"+srcJava);
        srcJava = srcJava.replaceAll("/", ".");
        projectPath = srcPath.substring(0,srcPath.indexOf("/src/main/java"));
        projectPath = projectPath.substring(0,projectPath.lastIndexOf("/"));
        logger.info("项目路径是:"+projectPath);
        

        MavenExcuter.runMaven(projectPath);
       
        //获取到当前的PROJECT
        project = ProjectParseUtil.parseProject(projectPath);
        logger.info("加载选中项目所有类");
        curdClassLoader = CURDClassLoader.getCURDClassLoader(project);
        
        if(isEntity(srcJava)==false){
        	throw new KoalaException("指定的类不是ENTITY");
        }
        
        if(project.getDbType().toUpperCase().equals("JPA")==false){
        	throw new KoalaException("当前仅支持JPA方式下的CURD功能");
        }
        
        //分析获取到所有的ENTITY类，只对涉及到的领域层进行分析建模，如果ENTITY写在非领域层，则会出现分析不出的问题
        List<MavenProject> childs =  project.getChilds();
        
        //查看待分析的所有实体
        List<String> entitys = new ArrayList<String>();
        for(MavenProject child:childs){
            if(child.getType().equals(ModuleType.BizModel)){
                List<String> javas = child.getSrcMainJavas();
                for(String java:javas){
                     String javaPath = java.substring(0,java.lastIndexOf(".java"));
                     javaPath = javaPath.replaceAll("/", ".");
                   boolean isEntity = isEntity(javaPath);
                   if(isEntity){
                       entitys.add(javaPath);
                       logger.info("分析到Entity对象:"+javaPath);
                   }
                }
            }
        }
        //实体本身进行分析
        EntityModel entity = analysisField(srcJava);
        curdClassLoader.close();
        return entity;
    }
    
    /**
     * 分析用户选中的ENTITY类
     * @param srcJava
     */
    private EntityModel analysisField(String srcJava){
        if(entityMap.containsKey(srcJava))return entityMap.get(srcJava);
        logger.info("==============对"+srcJava+"进行建模工作============");
        EntityModel entity = new EntityModel();
        entityMap.put(srcJava, entity);
        Class classEntity = null;
        try {
            classEntity = curdClassLoader.forName(srcJava);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        entity.setName(classEntity.getSimpleName());
        entity.setClassName(classEntity.getName());
        analysisField(classEntity,entity);
        logger.info("==============对"+srcJava+"进行建模完成=============");
        return entity;
    }
    
    /**
     * 传入一个类，解析这个类所拥有的属性
     * @param classEntity
     */
    private void analysisField(Class classEntity,EntityModel entity){
        Field[] fields = classEntity.getDeclaredFields();
        Method[] methods = classEntity.getDeclaredMethods();
        Map<String,Method> methodMap = new HashMap<String,Method>();
        for(Method method:methods){
            methodMap.put(method.getName().toLowerCase(), method);
        }
        for(Field field:fields){
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if(isStatic)continue;
            String fieldName =field.getName();
            if("serialVersionUID".equals(fieldName))continue;
            if("version".equals(fieldName.toLowerCase()))continue;
            FieldType fieldType = null;
            if(Modifier.isTransient(field.getModifiers())){
                fieldType = FieldType.Other;
            }else{
                fieldType = queryFieldType(field.getDeclaredAnnotations());
            }
            
            //如果从属性上查询不出任何数据库特性，尝试从属性的GET方法中去查询
            if(fieldType.equals(FieldType.Other)){
                String methodName = ("get"+fieldName).toLowerCase();
                if(methodMap.containsKey(methodName)){
                    Method method = methodMap.get(methodName);
                    fieldType = queryFieldType(method.getDeclaredAnnotations());
                }
            }
            logger.info("分析到属性:"+field.getName()+";类型是:【"+fieldType.toString()+"】");
            //分析FIELD
            createModel(field,fieldType,entity);
        }
        Class parent = classEntity.getSuperclass();
        if(parent!=null)analysisField(parent,entity);
    }

    private void createModel(Field field,FieldType fieldType,EntityModel entity){
        if(fieldType.equals(FieldType.Column)){
            FieldModel fieldModel = new PersistenceFieldModel(field.getName(),field.getType().getName());
            entity.getFields().add(fieldModel);
         }
         else  if(fieldType.equals(FieldType.ID)){
        	 //将主键的基本类型转换成对应的封装类型
        	 String type = field.getType().getName();
        	 type = convertPrimitive(type);
             FieldModel fieldModel = new PKFieldModel(field.getName(),type);
             entity.getFields().add(fieldModel);
             
         }else if(fieldType.equals(FieldType.EmbeddedId)){
             String javasrc = field.getType().getSimpleName();
             EntityModel entityModel = analysisField(javasrc);
             for(PersistenceFieldModel persistence:entityModel.getPersistenceFieldModel()){
                 FieldModel fieldModel = new PKFieldModel(persistence.getName(),persistence.getType());
                 entity.getFields().add(fieldModel);
             }
         }
         else if(fieldType.equals(FieldType.OneToMany)){
             RelationFieldModel fieldModel = new RelationFieldModel(field.getName(),field.getType().getName());
             RelationModel relation = new OneToManyRelationModel();
             ParameterizedTypeImpl type =(ParameterizedTypeImpl) field.getGenericType();
             Class actual = (Class)type.getActualTypeArguments()[0];
             EntityModel fieldEntity = analysisField(actual.getName());
             relation.setEntityModel(fieldEntity);
             fieldModel.setRelationModel(relation);
             entity.getFields().add(fieldModel);
         }
         else if(fieldType.equals(FieldType.OneToOne)){
             RelationFieldModel fieldModel = new RelationFieldModel(field.getName(),field.getType().getName());
             RelationModel relation = new OneToOneRelationModel();
             EntityModel fieldEntity = analysisField(field.getType().getName());
             relation.setEntityModel(fieldEntity);
             fieldModel.setRelationModel(relation);
             entity.getFields().add(fieldModel);
         }
         else if(fieldType.equals(FieldType.ManyToOne)){
             RelationFieldModel fieldModel = new RelationFieldModel(field.getName(),field.getType().getName());
             RelationModel relation = new ManyToOneRelationModel();
             EntityModel fieldEntity = analysisField(field.getType().getName());
             relation.setEntityModel(fieldEntity);
             fieldModel.setRelationModel(relation);
             entity.getFields().add(fieldModel);
         }
         else if(fieldType.equals(FieldType.ManyToMany)){
             RelationFieldModel fieldModel = new RelationFieldModel(field.getName(),field.getType().getName());
             RelationModel relation = new ManyToManyRelationModel();
             ParameterizedTypeImpl type =(ParameterizedTypeImpl) field.getGenericType();
             Class actual = (Class)type.getActualTypeArguments()[0];
             EntityModel fieldEntity = analysisField(actual.getName());
             relation.setEntityModel(fieldEntity);
             fieldModel.setRelationModel(relation);
             entity.getFields().add(fieldModel);
         }
         else if(fieldType.equals(FieldType.Other)){
             FieldModel fieldModel = new ObjectValueFieldModel(field.getName(),field.getType().getName());
             entity.getFields().add(fieldModel);
         }
    }
    
    
    private static final String Byte = "byte";
    
    private static final String BYTE = "Byte";
    
    private static final String Short = "short";
    
    private static final String SHORT = "Short";
    
    private static final String Int = "int";
    
    private static final String INT = "Integer";
    
    private static final String Long =  "long";
    
    private static final String LONG = "Long";
    
    private static final String Float = "float";
    
    private static final String FLOAT = "Float";
    
    private static final String Double = "double";
    
    private static final String DOUBLE = "Double";
    
    private static final String Char = "char";
    
    private static final String CHAR = "Character";
    
    /**
     * 将基本类型组装成封闭类型
     * @param type
     * @return
     */
    private String convertPrimitive(String type) {
    	if(type.equals(Byte)){
    		return BYTE;
    	}
    	if(type.equals(Int)){
    		return INT;
    	}
    	if(type.equals(Long)){
    		return LONG;
    	}
    	if(type.equals(Float)){
    		return FLOAT;
    	}
    	if(type.equals(Double)){
    		return DOUBLE;
    	}
    	if(type.equals(Char)){
    		return CHAR;
    	}
		return type;
	}

	/**
     * 传入Field或method上的注释，以判断它是属于数据库，关联还是其它类型
     * @param annotations
     * @return
     */
    private FieldType queryFieldType(Annotation[] annotations){
        for(Annotation annotation:annotations){
            String name = annotation.annotationType().getSimpleName();
            if(name.equals("Column")){
                return FieldType.Column;
            }
            if(name.equals("Id")){
                return FieldType.ID;
            }
            if(name.equals("EmbeddedId")){
                return FieldType.EmbeddedId;
            }
            if(name.equals("OneToOne")){
                return FieldType.OneToOne;
            }
            if(name.equals("OneToMany")){
                return FieldType.OneToMany;
            }
            if(name.equals("ManyToOne")){
                return FieldType.ManyToOne;
            }
            if(name.equals("ManyToMany")){
                return FieldType.ManyToMany;
            }
            if(name.equals("Transient")){
                return FieldType.Other;
            }
        }
        return FieldType.Column;
    }
    
    /**
     *  检测某个对象是否是实体
     * @param javasrc
     * @return
     */
    private boolean isEntity(String javasrc){
        try {
            Class entityClass = curdClassLoader.forName(javasrc);
            Annotation[] annotations = entityClass.getDeclaredAnnotations();
            if(annotations==null)return false;
            for(Annotation annotation:annotations){
                if(annotation.annotationType().getName().equals("javax.persistence.Entity")){
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MavenProject getProject() {
        return project;
    }
    
}