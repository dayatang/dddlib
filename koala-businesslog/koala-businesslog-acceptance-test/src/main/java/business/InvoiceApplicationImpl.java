package business;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:05 AM
 */
public class InvoiceApplicationImpl implements InvoiceApplication {
    @Override
    public Invoice findByInvoiceSn(String invoiceSn) {
        return new Invoice(invoiceSn);
    }

    @Override
    public Invoice addInvoice(String invoiceSn, long contractId) {
        return new Invoice(invoiceSn);
    }

    @Override
    public List<Invoice> addInvoices(List<Integer> ids) {
        List<Invoice> result = new ArrayList<Invoice>();
        result.add(new Invoice("c1"));
        result.add(new Invoice("c2"));
        result.add(new Invoice("c3"));
        result.add(new Invoice("c4"));
        return result;
    }


}
