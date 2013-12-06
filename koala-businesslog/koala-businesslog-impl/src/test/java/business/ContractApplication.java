package business;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 9:59 AM
 */
public interface ContractApplication {
    String addInvoice(String projectName, long contractId, long invoiceId);

    String addContract(long contractId);

}
