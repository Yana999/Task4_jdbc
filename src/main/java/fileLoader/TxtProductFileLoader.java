package fileLoader;

import org.postgresql.util.PSQLException;
import util.ConnectionUtil;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class TxtProductFileLoader implements ProductFileLoader {

    private final Connection connection = ConnectionUtil.getConnection();

    public void readProductList(String path){
        try{
            connection.setAutoCommit(false);
        }catch (SQLException e){
            System.out.println("Something went wrong with db");
            return;
        }
        long lineNumber = 0;
        String line = "";

        try(BufferedReader fileReader = new BufferedReader(new FileReader(path))){

            while (fileReader.ready()) {

                try (PreparedStatement getStoreId = connection.prepareStatement("SELECT id FROM Task_4.store WHERE name = ?");
                     PreparedStatement insertStore = connection.prepareStatement("INSERT INTO Task_4.store (id, name) VALUES (nextval('Task_4.gen_store_id'), ?)", Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement insertProduct = connection.prepareStatement("INSERT INTO Task_4.product ( name, weight, cost, store_id) VALUES (?, ?, ?, ?)")) {

                    ++lineNumber;
                    line = fileReader.readLine();
                    String[] splitLine = line.split(";");
                    if (splitLine.length == 4) {
                        getStoreId.setString(1, splitLine[3].trim());
                        ResultSet storeID = getStoreId.executeQuery();
                        if (!storeID.next()) {
                            storeID = saveStore(insertStore, splitLine[3].trim());
                            storeID.next();
                        }
                        try {
                            saveProduct(insertProduct, splitLine[0].trim(), Float.parseFloat(splitLine[1].trim()), new BigDecimal(splitLine[2].trim()), storeID.getLong(1));
                            connection.commit();
                        } catch (NumberFormatException e) {
                            System.out.println("Please, check the number format in line " + lineNumber);
                            connection.rollback();
                        }
                        storeID.close();
                    } else if (line.isEmpty()) {
                        System.out.println("Faced an empty line number " + lineNumber);
                    } else {
                        System.out.println("Impossible to read product in wrong format in line " + lineNumber);
                    }
                } catch (PSQLException e) {
                    System.out.println("Occurs an error" + lineNumber + " " + line + " " + e.getMessage());
                    try {
                        connection.rollback();
                    }catch (SQLException e1){
                        System.out.println("Impossible make a rollback");
                    }
                } catch (SQLException e) {
                    System.out.println("Impossible to save line " + line + " number " + lineNumber + " into db");
                    try {
                        connection.rollback();
                    }catch (SQLException e1){
                        System.out.println("Impossible make a rollback");
                    }
                }

            }
        }catch (FileNotFoundException e) {
            System.out.printf("File %s not found %n", path);
        }catch (IOException e){
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Something went wrong with db");
                return;
            }
        }
    }

    private ResultSet saveStore(PreparedStatement insertStore, String name)throws SQLException{
        insertStore.setString(1, name);
        insertStore.executeUpdate();
        return insertStore.getGeneratedKeys();
    }

    private void saveProduct(PreparedStatement insertProduct, String name, float weight, BigDecimal cost, long store_id) throws SQLException{
        insertProduct.setString(1, name);
        insertProduct.setBigDecimal(2, BigDecimal.valueOf(weight));
        insertProduct.setBigDecimal(3, cost);
        insertProduct.setLong(4, store_id);
        insertProduct.executeUpdate();
    }

}
