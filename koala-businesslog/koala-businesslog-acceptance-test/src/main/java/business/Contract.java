package business;

import java.math.BigDecimal;

/**
 * User: zjzhai
 * Date: 12/10/13
 * Time: 10:15 AM
 */
public class Contract {

    private String name;

    private Invoice invoice;

    private Project project;

    private BigDecimal amount = BigDecimal.ZERO;

    public Contract() {
    }

    public Contract(BigDecimal amount, Invoice invoice) {
        this.amount = amount;
        this.invoice = invoice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract)) return false;

        Contract contract = (Contract) o;

        if (amount != null ? !amount.equals(contract.amount) : contract.amount != null) return false;
        if (invoice != null ? !invoice.equals(contract.invoice) : contract.invoice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoice != null ? invoice.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
