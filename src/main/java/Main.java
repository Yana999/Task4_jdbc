import database.DatabaseLoader;
import entity.Store;
import fileLoader.TxtProductFileLoader;
import util.ConnectionUtil;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length > 0) {
            new TxtProductFileLoader().readProductList(args[0]);
            List<Store> stores = new DatabaseLoader().getProducts();
            System.out.println();
            stores.forEach(x -> System.out.println(x.info()));
            ConnectionUtil.close();
        }else{
            System.out.println("Loading file is absent");
        }
    }
}
