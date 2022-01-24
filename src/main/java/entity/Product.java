package entity;

import exception.InputValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Product {
    private final String name;
    private final double weight;
    private final BigDecimal cost;

    public Product(String name, double weight, BigDecimal cost){
       this.name = name;
        this.weight = weight;
        this.cost = cost;
    }

    public static Product parseProduct(String name, String weight, String cost) throws InputValueException {
        name = name.trim();
        weight = weight.trim();
        cost = cost.trim();
        BigDecimal convertedCost;
        double convertedWeight;

        if(!name.matches("[a-zA-Z0-9-]+")){
            throw new InputValueException("product's name", name);
        }

        weight = weight.replace(",", ".");
        try {
            convertedWeight = Double.parseDouble(weight);
        }catch (NumberFormatException e){
            throw new InputValueException("weight", weight);
        }
        if(convertedWeight < 0 || ((convertedWeight * 1000 % 1 > 0))) {
            throw new InputValueException("weight", weight);
        }

        cost = cost.replace(",", ".");
        try {
            convertedCost = new BigDecimal(cost);
            //convertedCost.setScale(2, RoundingMode.HALF_UP);
        }catch (NumberFormatException e){
            throw new InputValueException("cost", cost);
        }
        if(convertedCost.compareTo(BigDecimal.ZERO) < 0 || convertedCost.ulp().compareTo(new BigDecimal("0.01")) < 0) {
            throw new InputValueException( "cost", cost);
        }
        return new Product(name, convertedWeight, convertedCost);
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public String toString(){
        return String.format("%1$s  %2$,.3f  %3$,.2f", name, weight, cost);
    }

    public String formatString() {
        return String.format("%1$-15s %2$,10.3f %3$,7.2f%n", name, weight, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.weight, weight) == 0 && Objects.equals(name, product.name) && Objects.equals(cost, product.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, cost);
    }
}
