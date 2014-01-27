package test;

import java.util.List;


public interface AccountApplication2 {
	/**
	 * 查询帐户余额
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public int queryMoney(String accountId) throws Exception;
	
	public void deleteAccount(String accountId) throws Exception;
	
	/**
	 * 新开一个帐户
	 * @param accountId
	 * @param name
	 * @throws Exception
	 */
	public void addAccount(String accountId,String name) throws Exception;
	
	/**
	 * 从帐户上支取余额
	 * @param accountId
	 * @param money
	 * @throws Exception
	 */
	public void drawMoney(String accountId,int money) throws Exception;
	
	/**
	 * 存钱到指定的帐户上
	 * @param accountId
	 * @param money
	 * @throws Exception
	 */
	public void addMoney(String accountId,int money) throws Exception;
}
