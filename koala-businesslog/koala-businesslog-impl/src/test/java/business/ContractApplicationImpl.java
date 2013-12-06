package business;

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

    public static Project findByContractIdAndProject(long contractId, Project project) {
        return new Project("项目11");
    }
}
