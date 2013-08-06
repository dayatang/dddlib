package org.openkoala.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.openkoala.application.util.AbstractIntegratedTestCase;
import org.openkoala.auth.application.MenuApplication;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.springframework.util.Assert;

import com.dayatang.domain.InstanceFactory;

public class SystemInit  extends AbstractIntegratedTestCase{
	public static UserApplication getUserApplication(){
		return InstanceFactory.getInstance(UserApplication.class);
	}
	public static RoleApplication getRoleApplication(){
		return InstanceFactory.getInstance(RoleApplication.class);
	}
	public static MenuApplication getMenuApplication(){
		return InstanceFactory.getInstance(MenuApplication.class);
	}
	public static ResourceApplication getUrlApplication(){
		return InstanceFactory.getInstance(ResourceApplication.class);
	}
	
	
	
	public void testGetMenu() throws Exception 
	{
		List<ResourceVO> ls = getMenuApplication().findChildByParentAndUser(null, "caoyong");
		for(ResourceVO tv : ls)
		{
			printChild(tv);
			
		}
		Assert.isTrue(true);
		
	}

	private void printChild(ResourceVO child)
	{
		System.out.println(child.getName());
		for(ResourceVO ss : child.getChildren())
		{
			printChild(ss);
		}
	}
	@Test
	public void testInitMenu() throws Exception 
	{
		
		

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		ResourceVO sysVO1 = new ResourceVO();
		sysVO1.setIcon("abc.jpg");
		sysVO1.setLevel("1");
		sysVO1.setName("系统管理");
		sysVO1.setIdentifier("abc.jsp");
		sysVO1.setSerialNumber("2");
		getMenuApplication().saveMenu(sysVO1);
		//by hanst
		ResourceVO sysVO5 = new ResourceVO();
		sysVO5.setIcon("");
		sysVO5.setLevel("1");
		sysVO5.setName("资源管理");
		sysVO5.setIdentifier("");
		sysVO5.setSerialNumber("2");
		getMenuApplication().saveMenu(sysVO5);
		
		ResourceVO childRes = new ResourceVO();
		childRes.setIcon("");
		childRes.setLevel("2");
		childRes.setName("URL资源管理");
		childRes.setIdentifier("/auth-UrlResource-list.action");
		childRes.setSerialNumber("3");
		getMenuApplication().saveMenu(childRes);
		
		ResourceVO childRes2 = new ResourceVO();
		childRes2.setIcon("");
		childRes2.setLevel("2");
		childRes2.setName("菜单资源管理");
		childRes2.setIdentifier("/auth-Menu-list.action");
		childRes2.setSerialNumber("3");
		getMenuApplication().saveMenu(childRes2);
		
		Assert.isTrue(true);
		
		getMenuApplication().assign(sysVO5, childRes);
		getMenuApplication().assign(sysVO5, childRes2);
		

		Assert.isTrue(true);
		ResourceVO child1 = new ResourceVO();
		child1.setIcon("lib/icons/32X32/sitemap.gif");
		child1.setLevel("2");
		child1.setName("菜单管理");
		child1.setIdentifier("abc.jsp");
		child1.setSerialNumber("3");
		getMenuApplication().saveMenu(child1);
		Assert.isTrue(true);
		
		ResourceVO child2 = new ResourceVO();
		child2.setIcon("lib/icons/32X32/link.gif");
		child2.setLevel("2");
		child2.setName("权限设置");
		child2.setIdentifier("abc.jsp");
		child2.setSerialNumber("4");
		getMenuApplication().saveMenu(child2);
		Assert.isTrue(true);
		
		ResourceVO childsub3 = new ResourceVO();
		childsub3.setIcon("lib/icons/32X32/link.gif");
		childsub3.setLevel("3");
		childsub3.setName("测试三级菜单");
		childsub3.setIdentifier("abc.jsp");
		childsub3.setSerialNumber("4");
		getMenuApplication().saveMenu(childsub3);
		Assert.isTrue(true);
		
		ResourceVO childsub4 = new ResourceVO();
		childsub4.setIcon("lib/icons/32X32/link.gif");
		childsub4.setLevel("4");
		childsub4.setName("测试四级菜单");
		childsub4.setIdentifier("abc.jsp");
		childsub4.setSerialNumber("4");
		getMenuApplication().saveMenu(childsub4);
		Assert.isTrue(true);
		
		getMenuApplication().assign(sysVO1, child1);
		getMenuApplication().assign(sysVO1, child2);
		getMenuApplication().assign(child1, childsub3);
		getMenuApplication().assign(childsub3, childsub4);
		
		ResourceVO sysVO2 = new ResourceVO();

		sysVO2.setIcon("abc.jpg");
		sysVO2.setLevel("1");
		sysVO2.setName("组织管理");
		sysVO2.setIdentifier("abc.jsp");
		sysVO2.setSerialNumber("5");

		getMenuApplication().saveMenu(sysVO2);
		Assert.isTrue(true);
		ResourceVO child3 = new ResourceVO();
		child3.setIcon("lib/icons/32X32/sitemap.gif");
		child3.setLevel("2");
		child3.setName("用户管理");
		child3.setIdentifier("/auth-User-list.action");
		child3.setSerialNumber("6");
		getMenuApplication().saveMenu(child3);
		Assert.isTrue(true);
		
		ResourceVO child4 = new ResourceVO();
		child4.setIcon("lib/icons/32X32/link.gif");
		child4.setLevel("2");
		child4.setName("角色管理");
		child4.setIdentifier("/auth-Role-list.action");
		child4.setSerialNumber("7");
		getMenuApplication().saveMenu(child4);
		Assert.isTrue(true);
		
		getMenuApplication().assign(sysVO2, child3);
		getMenuApplication().assign(sysVO2, child4);
		
		
		
		//新增用户 
		UserVO userVO = new UserVO();
		userVO.setUserAccount("caoyong");
		userVO.setUserPassword("3fcfde830c51688805840714ebcde48f");
		userVO.setCreateOwner("admin");
		userVO.setAbolishDate(formatter.format(new Date()));
		userVO.setCreateDate(formatter.format(new Date()));
		userVO.setName("曹勇");
		userVO.setValid(true);
		userVO.setSerialNumber("1");
		userVO.setSortOrder(8);
		userVO.setUserDesc("测试用户");
		getUserApplication().saveUser(userVO);
		
		//新增资源
		ResourceVO res1 = new ResourceVO();
		res1.setIdentifier("/");
		res1.setName("登录控制");
		res1.setSerialNumber("1");
		res1.setSortOrder(1);
		getUrlApplication().saveResource(res1);
		Assert.isTrue(true);
		
		ResourceVO res2 = new ResourceVO();
		res2.setIdentifier("/index.jsp");
		res2.setName("查询控制");
		res2.setSerialNumber("2");
		res2.setSortOrder(2);
		getUrlApplication().saveResource(res2);
		Assert.isTrue(true);
		
		ResourceVO res3 = new ResourceVO();
		res3.setIdentifier("/auth-Menu-findAllMenuByUser.action");
		res3.setName("登录控制");
		res3.setSerialNumber("1");
		res3.setSortOrder(1);
		getUrlApplication().saveResource(res3);
		Assert.isTrue(true);
		
		ResourceVO res4 = new ResourceVO();
		res4.setIdentifier("/auth-Menu-findAllSubMenuByParent.action");
		res4.setName("登录控制");
		res4.setSerialNumber("1");
		res4.setSortOrder(1);
		getUrlApplication().saveResource(res4);
		Assert.isTrue(true);
		
		ResourceVO res5 = new ResourceVO();
		res5.setIdentifier("/auth-User-pageJson.action");
		res5.setName("用户管理");
		res5.setSerialNumber("1");
		res5.setSortOrder(1);
		getUrlApplication().saveResource(res5);
		Assert.isTrue(true);
		
		ResourceVO res6 = new ResourceVO();
		res6.setIdentifier("/auth-Role-pageJson.action");
		res6.setName("角色管理");
		res6.setSerialNumber("1");
		res6.setSortOrder(1);
		getUrlApplication().saveResource(res6);
		Assert.isTrue(true);
		
		//新增角色
		RoleVO roleVO1 = new RoleVO();
		roleVO1.setAbolishDate(formatter.format(new Date()));
		roleVO1.setCreateDate(formatter.format(new Date()));
		roleVO1.setCreateOwner("admin");
		roleVO1.setName("系统菜单配置员");
		roleVO1.setRoleDesc("专门配置系统菜单的");
		roleVO1.setSerialNumber("1");
		roleVO1.setSortOrder(1);
		roleVO1.setValid(true);
		getRoleApplication().saveRole(roleVO1);
		
		///auth-Menu-findAllSubMenuByParent.action?ResourceVO.id=1
		
		RoleVO roleVO2 = new RoleVO();
		roleVO2.setAbolishDate(formatter.format(new Date()));
		roleVO2.setCreateDate(formatter.format(new Date()));
		roleVO2.setCreateOwner("admin");
		roleVO2.setName("组织菜单管理员");
		roleVO2.setRoleDesc("专门配置组织机构菜单的");
		roleVO2.setSerialNumber("1");
		roleVO2.setSortOrder(1);
		roleVO2.setValid(true);
		getRoleApplication().saveRole(roleVO2);
		
		RoleVO roleVO3 = new RoleVO();
		roleVO3.setAbolishDate(formatter.format(new Date()));
		roleVO3.setCreateDate(formatter.format(new Date()));
		roleVO3.setCreateOwner("admin");
		roleVO3.setName("URL资源管理员");
		roleVO3.setRoleDesc("专门配置URL资源地址的");
		roleVO3.setSerialNumber("1");
		roleVO3.setSortOrder(1);
		roleVO3.setValid(true);
		getRoleApplication().saveRole(roleVO3);
		
		RoleVO roleVO4 = new RoleVO();
		roleVO4.setAbolishDate(formatter.format(new Date()));
		roleVO4.setCreateDate(formatter.format(new Date()));
		roleVO4.setCreateOwner("admin");
		roleVO4.setName("组织角色维护资源管理员");
		roleVO4.setRoleDesc("专门配置用户和角色的");
		roleVO4.setSerialNumber("1");
		roleVO4.setSortOrder(1);
		roleVO4.setValid(true);
		getRoleApplication().saveRole(roleVO4);
		
		//用户授权角色
		getUserApplication().assignRole(userVO, roleVO1);
		getUserApplication().assignRole(userVO, roleVO2);
		getUserApplication().assignRole(userVO, roleVO3);
		getUserApplication().assignRole(userVO, roleVO4);
		
		//角色授权资源
		getRoleApplication().assignMenuResource(roleVO1, sysVO1);
		getRoleApplication().assignMenuResource(roleVO1, child1);
		getRoleApplication().assignMenuResource(roleVO1, childsub3);
		getRoleApplication().assignMenuResource(roleVO1, childsub4);
		getRoleApplication().assignMenuResource(roleVO1, child2);
		getRoleApplication().assignMenuResource(roleVO2, sysVO2);
		getRoleApplication().assignMenuResource(roleVO2, child3);
		getRoleApplication().assignMenuResource(roleVO2, child4);
		getRoleApplication().assignResource(roleVO3, res1);
		getRoleApplication().assignResource(roleVO3, res2);
		getRoleApplication().assignResource(roleVO3, res3);
		getRoleApplication().assignResource(roleVO3, res4);
		getRoleApplication().assignResource(roleVO4, res5);
		getRoleApplication().assignResource(roleVO4, res6);
		//by hanst
		getRoleApplication().assignMenuResource(roleVO4, sysVO5);
		getRoleApplication().assignMenuResource(roleVO4, childRes);
		getRoleApplication().assignMenuResource(roleVO4, childRes2);
		
		Assert.isTrue(true);
		
	}
	

	public  static void main(String[] args)
	{
		
	}

}
