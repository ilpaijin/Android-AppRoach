package instak.instacash.Model;

public class Currency {
    public String countryName;
    public String countryCode;
    public String currencyCode;

    public Currency(String countryName, String countryCode, String currencyCode) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
    }
}
