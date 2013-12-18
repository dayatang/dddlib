package business.impl;

import business.Contract;
import business.ContractApplication;
import business.Project;

import java.math.BigDecimal;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 9:59 AM
 */
public class ContractApplicationImpl implements ContractApplication {
    @Override
    public String addInvoice(String projectName, long contractId, long invoiceId) {
        //返回发票编号
        return "K-8999";
    }

    @Override
    public String addContract(long contractId) {
        //返回合同名
        return "合同名";
    }

    @Override
    public Contract findContractById(long contractId) {
        Contract contract = new Contract();
        contract.setName("一期合同");
        return contract;
    }

    public Project findByContractIdAndProject(long contractId, Project project) {
        return new Project("项目11");
    }

    public static void kk(double k, float f, int i, BigDecimal bigDecimal) {

    }
}
