package database;

import model.Product;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ShopDatabase {
    private List<Product> products = new ArrayList<Product>();

    public Product getProductById(int id){
        for (Product p : products){
            if (p.getId() == id){
                return p;
            }
        }
        return null;
    }

    public List<Product> getProductsByName(String name){
        throw new NotImplementedException();
    }

    public List<Product> getProductsWithPriceInLimits(double min, double max){
        throw new NotImplementedException();
    }
}
