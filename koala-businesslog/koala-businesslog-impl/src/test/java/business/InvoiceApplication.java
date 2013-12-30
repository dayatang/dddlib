package business;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:05 AM
 */
public interface InvoiceApplication {
    Invoice findByInvoiceSn(String invoiceSn);

    Invoice addInvoice(String invoiceSn, long contractId);

    List<Invoice> addInvoices(List<Integer> ids);
}
