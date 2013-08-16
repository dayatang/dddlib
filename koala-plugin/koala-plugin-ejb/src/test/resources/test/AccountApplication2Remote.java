package test;

import java.util.List;
import javax.ejb.Remote;

@Remote()
public interface AccountApplication2Remote {

    /**
	 * 鏌ヨ甯愭埛浣欓
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
    public int queryMoney(String accountId) throws Exception;
}
