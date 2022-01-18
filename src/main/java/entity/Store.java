package entity;

import exception.InputValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public final class Store{
    private final String name;
    private final HashMap <String, Product> products;

    public Store(String name){
        this.name = name;
        this.products = new LinkedHashMap<>();
    }

    public static Store parseStore(String store) throws InputValueException {
        store = store.trim();
        if(!store.matches("[a-zA-Z0-9]+")){
            throw new InputValueException("store's name", store);
        }else{
            return new Store(store);
        }
    }

    public BigDecimal sumCost(){
        BigDecimal allCost= new BigDecimal(0);
        for(Product product : products.values()){
            allCost = allCost.add(product.getCost());
        }
        return allCost;
    }

    public Product addProduct(Product product){
        return  products.putIfAbsent(product.getName(), product);
    }

    public BigDecimal avgCost(){
        return products.size() == 0 ? BigDecimal.ZERO : sumCost().divide(new BigDecimal(products.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal avgCostWith(List<Product> withProducts){
        BigDecimal newSum = sumCost();
        for(Product product : withProducts){
            newSum = newSum.add(product.getCost());
        }
        BigDecimal newSize = new BigDecimal(products.size() + withProducts.size());
        return newSize.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : newSum.divide(newSize, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal avgCostWithout(List<Product> withoutProducts){
        BigDecimal newSum = sumCost();
        for(Product product : withoutProducts){
            newSum = newSum.subtract(product.getCost());
        }
        BigDecimal newSize = new BigDecimal(products.size() - withoutProducts.size());
        return newSize.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : newSum.divide(newSize, 2, RoundingMode.HALF_UP);
    }

    public String info(){
        StringBuilder info = new StringBuilder();
        info.append(String.format("%s average cost: %,7.2f:%n", name, avgCost().setScale(2, RoundingMode.HALF_UP).doubleValue()));
        products.forEach((k,v) -> info.append(v.formatString()));
        return info.toString();
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Product> getProducts() {
        return new HashMap<>(products);
    }

    @Override
    public String toString() {
        return '{' + "name='" + name + '\'' + ", products=" + products + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(name, store.name) && Objects.equals(products, store.products);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
