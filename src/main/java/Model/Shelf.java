package Model;

import java.math.BigDecimal;

public class Shelf {
    private String productId;
    private BigDecimal relevancyScore;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getRelevancyScore() {
        return relevancyScore;
    }

    public void setRelevancyScore(BigDecimal relevancyScore) {
        this.relevancyScore = relevancyScore;
    }

}
