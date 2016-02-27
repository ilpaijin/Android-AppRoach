package instak.instacash.Model;

import java.math.BigDecimal;

public class PaymentMethod {
    public String name;
    public BigDecimal price;
    public String label;

    public PaymentMethod(String name, BigDecimal price, String label) {
        this.name = name;
        this.price = price;
        this.label = label;
    }
}
