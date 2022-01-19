package fileLoader;

import org.postgresql.util.PSQLException;
import util.ConnectionUtil;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class TxtProductFileLoader implements ProductFileLoader {

private final static Connection connection = ConnectionUtil.getConnection();

    public void readProductList(String path){

        long lineNumber = 0;
        String line = "";
        try(BufferedReader fileReader = new BufferedReader(new FileReader(path))){
            while (fileReader.ready()) {
                try (PreparedStatement getStoreId = connection.prepareStatement("SELECT id FROM Task_4.store WHERE name = ?")) {
                    ++lineNumber;
                    line = fileReader.readLine();
                    String[] splitLine = line.split(";");
                    if (splitLine.length == 4) {
                        getStoreId.setString(1, splitLine[3].trim());
                        ResultSet storeID = getStoreId.executeQuery();
                        if (!storeID.next()) {
                            saveStore(splitLine[3].trim());
                            storeID = getStoreId.executeQuery();
                            storeID.next();
                        }
                        try {
                            saveProduct(splitLine[0].trim(), Float.parseFloat(splitLine[1].trim()), new BigDecimal(splitLine[2].trim()), storeID.getInt(1));
                        } catch (NumberFormatException e) {
                            System.out.println("Please, check the number format in line " + lineNumber);
                        }
                    } else if (line.isEmpty()) {
                        System.out.println("Faced an empty line number " + lineNumber);
                    } else {
                        System.out.println("Impossible to read product in wrong format in line " + lineNumber);
                    }


                } catch (PSQLException e) {
                    System.out.println("The line " + " number " + lineNumber + " " + line + " contains wrong value of weight or cost. It cannot be negative");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("Impossible to save line " + line + " number " + lineNumber + " into db");
                    e.printStackTrace();
                }
            }
        }catch (FileNotFoundException e) {
            System.out.printf("File %s not found %n", path);
        }catch (IOException e){
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
    }

    private void saveStore(String name)throws SQLException{
        PreparedStatement insertStore = connection.prepareStatement("INSERT INTO Task_4.store (name) VALUES (?);");
        insertStore.setString(1, name);
        insertStore.executeUpdate();
        insertStore.close();
    }

    private void saveProduct(String name, float weight, BigDecimal cost, int store_id) throws SQLException{
        PreparedStatement insertProduct = connection.prepareStatement("INSERT INTO Task_4.product (name, weight, cost, store_id) VALUES (?, ?, ?, ?)");
        insertProduct.setString(1, name);
        insertProduct.setFloat(2, weight);
        insertProduct.setBigDecimal(3, cost);
        insertProduct.setFloat(4, store_id);
        insertProduct.executeUpdate();
        insertProduct.close();
    }

}
