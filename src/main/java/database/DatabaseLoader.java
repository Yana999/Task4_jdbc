package database;

import entity.Product;
import entity.Store;
import exception.InputValueException;
import util.ConnectionUtil;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseLoader {
    public List<Store> getProducts(){
        List<Store> stores = new LinkedList<>();
        Connection connection = ConnectionUtil.getConnection();
        try(PreparedStatement getProducts = connection.prepareStatement("SELECT * FROM Task_4.product WHERE store_id = ?");
            Statement getStore = connection.createStatement()){
            ResultSet getStoreResult = getStore.executeQuery("SELECT * FROM Task_4.store");
            while(getStoreResult.next()) {
                try {
                    Store store = Store.parseStore(getStoreResult.getString(2));
                    getProducts.setInt(1, getStoreResult.getInt(1));
                    ResultSet getProductResult = getProducts.executeQuery();
                    while (getProductResult.next()) {
                        try {
                            Product product = Product.parseProduct(getProductResult.getString(2),
                                    getProductResult.getString(4),
                                    getProductResult.getString(3));
                            store.addProduct(product);
                        } catch (InputValueException e) {
                            System.out.println(e.getMessage() + "impossible to load from db");
                        }
                    }
                    stores.add(store);
                }catch (InputValueException e){
                    System.out.println("wrong store name " + getStoreResult.getString(2));
                }
            }
            getStoreResult.close();

        }catch (SQLException e){
            System.out.println("Impossible to get products from db");
            e.printStackTrace();
        }
        return stores;
    }
}
