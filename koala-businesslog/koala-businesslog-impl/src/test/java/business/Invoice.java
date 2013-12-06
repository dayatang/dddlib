package business;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 10:18 AM
 */
public class Invoice {
    private String sn;

    public Invoice(String sn) {
        this.sn = sn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
