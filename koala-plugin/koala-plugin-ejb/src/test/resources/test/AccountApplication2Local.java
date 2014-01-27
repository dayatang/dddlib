package test;

import java.util.List;
import javax.ejb.Local;

@Local()
public interface AccountApplication2Local {

    /**
	 * 鏌ヨ甯愭埛浣欓
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
    public int queryMoney(String accountId) throws Exception;

    public void deleteAccount(String accountId) throws Exception;
}
