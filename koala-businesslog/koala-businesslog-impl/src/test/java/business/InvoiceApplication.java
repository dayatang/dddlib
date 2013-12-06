package business;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:05 AM
 */
public interface InvoiceApplication {
    Invoice findByInvoiceSn(String invoiceSn);
}
