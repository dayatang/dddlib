package test;

import java.util.List;
import javax.ejb.Local;

@Local()
public interface AccountApplicationLocal {

    /**
	 * 鏌ヨ甯愭埛浣欓
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
    public int queryMoney(String accountId) throws Exception;

    public void deleteAccount(String accountId) throws Exception;

    /**
	 * 鏂板紑涓�釜甯愭埛
	 * @param accountId
	 * @param name
	 * @throws Exception
	 */
    public void addAccount(String accountId, String name) throws Exception;

    /**
	 * 浠庡笎鎴蜂笂鏀彇浣欓
	 * @param accountId
	 * @param money
	 * @throws Exception
	 */
    public void drawMoney(String accountId, int money) throws Exception;

    /**
	 * 瀛橀挶鍒版寚瀹氱殑甯愭埛涓�
	 * @param accountId
	 * @param money
	 * @throws Exception
	 */
    public void addMoney(String accountId, int money) throws Exception;
}
