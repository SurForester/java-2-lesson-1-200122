package com.geekbrains;

import com.geekbrains.market.Market;
import com.geekbrains.person.customer.Customer;
import com.geekbrains.person.seller.Seller;
import com.geekbrains.product.Product;

import java.util.ArrayList;
import java.util.List;

public class Main {
    //1) Seller, может добавлять, убирать продукты, выставлять стоимость
    //2) Customer может покупать товар, может что-то желать
    //3) Когда seller продает продукты, у него они отнимаются, но добавляются у customer
    // кэш отнимается у customer, добавляется к seller
    //4) Поиск товара про продавцу и продукту, либо по продукту

    public static void main(String[] args) {
        Market market = new Market();

        // first seller
        Seller firstSeller = createSeller("Виталий", "Еремин", List.of(
                createProduct(MarketConstants.TOMATOES_PRODUCT_NAME, 10, 2),
                createProduct(MarketConstants.CUCUMBER_PRODUCT_NAME, 8, 100)
        ));
        // second seller
        Seller secondSeller = createSeller("Алексей", "Ушаков", List.of(
                createProduct(MarketConstants.TOMATOES_PRODUCT_NAME, 8, 4),
                createProduct(MarketConstants.CUCUMBER_PRODUCT_NAME, 5, 12)
        ));
        // third seller
        Seller thirdSeller = createSeller("Алексей", "Давыдов", List.of(
                createProduct(MarketConstants.TOMATOES_PRODUCT_NAME, 9, 70),
                createProduct(MarketConstants.CUCUMBER_PRODUCT_NAME, 7, 80)
        ));

        market.addSeller(firstSeller);
        market.addSeller(secondSeller);
        market.addSeller(thirdSeller);

        System.out.println("Рынок до продажи.");
        for (Seller seller: market.getSellers()) {
            seller.info();
        }

        System.out.println("Рынок после продажи.");
        Customer customer = createFirstCustomer();
        //customer.findProductOnMarket(market);
        customer.findProductBySellersName(market,"Алексей","Ушаков");
        customer.info();
        for (Seller seller: market.getSellers()) {
            seller.info();
        }
    }

    private static Customer createFirstCustomer() {
        Product firstProduct = new Product();
        firstProduct.setName(MarketConstants.TOMATOES_PRODUCT_NAME);
        firstProduct.setQuantity(5);

        Product secondProduct = new Product();
        secondProduct.setName(MarketConstants.CUCUMBER_PRODUCT_NAME);
        secondProduct.setQuantity(2);

        return new Customer(List.of(firstProduct, secondProduct), 100);
    }

    private static Seller createSeller(String name, String lastname, List<Product> products) {
        Seller seller = new Seller();
        seller.setName(name);
        seller.setLastName(lastname);
        seller.setProducts(products);
        seller.setCash(0);
        return seller;
    }

    private static Product createProduct(String name, int price, int quantity) {
        Product product = new Product();
        product.setName(name);
        product.setQuantity(quantity);
        product.setPrice(price);
        return product;
    }

}
