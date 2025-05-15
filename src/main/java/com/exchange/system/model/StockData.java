package com.exchange.system.model;


public class StockData {

    private ProductType productType;
    private String symbol;
    private double curPrice;
    private double strikePrice;
    private double expectedReturn;
    private double annualizedStandardDeviation;
    private String maturity;

    private StockData(Builder builder) {
        this.productType = builder.productType;
        this.symbol = builder.symbol;
        this.curPrice = builder.curPrice;
        this.strikePrice = builder.strikePrice;
        this.expectedReturn = builder.expectedReturn;
        this.annualizedStandardDeviation = builder.annualizedStandardDeviation;
        this.maturity = builder.maturity;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(double curPrice) {
        this.curPrice = curPrice;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public double getAnnualizedStandardDeviation() {
        return annualizedStandardDeviation;
    }

    public String getMaturity() {
        return maturity;
    }

    public static class Builder {

        private ProductType productType;
        private String symbol;
        private double curPrice;
        private double strikePrice;
        private double expectedReturn;
        private double annualizedStandardDeviation;
        private String maturity;

        public Builder() {
        }

        public Builder productType(String productType) {
            this.productType = ProductType.valueOf(productType);
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder curPrice(double curPrice) {
            this.curPrice = curPrice;
            return this;
        }

        public Builder strikePrice(double strikePrice) {
            this.strikePrice = strikePrice;
            return this;
        }

        public Builder expectedReturn(double expectedReturn) {
            this.expectedReturn = expectedReturn;
            return this;
        }

        public Builder annualizedStandardDeviation(double annualizedStandardDeviation) {
            this.annualizedStandardDeviation = annualizedStandardDeviation;
            return this;
        }

        public Builder maturity(String maturity) {
            this.maturity = maturity;
            return this;
        }

        public StockData build() {
            if (symbol == null || symbol.trim().isEmpty()) {
                throw new IllegalStateException("Symbol cannot be null or empty");
            }
            if (curPrice < 0) {
                throw new IllegalStateException("Price cannot be negative");
            }
            return new StockData(this);
        }
    }

}
