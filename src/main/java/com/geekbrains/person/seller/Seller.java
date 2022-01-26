package com.geekbrains.person.seller;

import com.geekbrains.person.Person;
import com.geekbrains.person.customer.Customer;
import com.geekbrains.product.Product;

import java.util.List;

public class Seller extends Person {
    private String name;
    private String lastName;
    private List<Product> products;

    public boolean sellProducts(Customer customer, Product expectedProduct) {
        Product product = productExists(expectedProduct);
        if (product != null) {
            // Проверяем что кэш покупателя позволяет купить товар
            long requiredCash = (long) product.getPrice() * expectedProduct.getQuantity();
            if (customer.getCash() >= requiredCash) {
                // Уменьшаем количество продукта у продавца
                product.setQuantity(product.getQuantity() - expectedProduct.getQuantity());

                //Создаем новый объект для покупателя, чтобы ссылка не дублировалась
                Product customerProduct = new Product();
                customerProduct.setQuantity(expectedProduct.getQuantity());
                customerProduct.setName(expectedProduct.getName());

                //Добавляем количество продуктов у покупателя
                customer.addPurchase(customerProduct);
                //Увеличиваем кэш продавца
                setCash(getCash() + requiredCash);
                //Уменьшаем кэш покупателя
                customer.setCash(customer.getCash() - requiredCash);
                //Сообщаем потребителю метода, что покупка совершена
                return true;
            }
        }
        return false;
    }

    // попытка продать все у одного продавца, если удалось - возврат = истина, иначе возврат = ложь
    public boolean canSellProducts(Customer customer, List<Product> expectedPurchaseList) {
        long needCash = 0;
        int needProducts = 0;
        for (Product expectedProduct : expectedPurchaseList) {
            Product product = productExists(expectedProduct);
            if (product != null) {
                needProducts++;
                needCash += (long) product.getPrice() * expectedProduct.getQuantity();
            }
        }
        if (expectedPurchaseList.size() == needProducts && customer.getCash() >= needCash) {
            for (Product expectedProduct : expectedPurchaseList) {
                Product product = productExists(expectedProduct);
                // Уменьшаем количество продукта у продавца
                product.setQuantity(product.getQuantity() - expectedProduct.getQuantity());

                //Создаем новый объект для покупателя, чтобы ссылка не дублировалась
                Product customerProduct = new Product();
                customerProduct.setName(expectedProduct.getName());
                customerProduct.setQuantity(expectedProduct.getQuantity());
                customerProduct.setPrice(product.getPrice());

                //Добавляем количество продукт у покупателя
                customer.addPurchase(customerProduct);

                long requiredCash = (long) product.getPrice() * expectedProduct.getQuantity();
                //Увеличиваем кэш продавца
                setCash(getCash() + requiredCash);
                //Уменьшаем кэш покупателя
                customer.setCash(customer.getCash() - requiredCash);
                //Сообщаем потребителю метода, что покупка совершена
            }
            return true;
        }
        return false;
    }

    // полная или частичная продажа по одному продавцу
    public void sellProductsBySeller(Customer customer, List<Product> expectedPurchaseList) {
        for (Product expectedProduct : expectedPurchaseList) {
            for (Product product : this.products) {
                if (product.getName().equals(expectedProduct.getName())) {
                    // учитываем тот факт, что у продавца может быть меньше товара, но следующего продавца может не быть
                    int quantity = Math.min(product.getQuantity(), expectedProduct.getQuantity());
                    if (quantity > 0 && ((long) quantity * product.getPrice()) <= customer.getCash()) {
                        // Уменьшаем количество продукта у продавца
                        product.setQuantity(product.getQuantity() - quantity);
                        expectedProduct.setQuantity(expectedProduct.getQuantity() - quantity);

                        //Создаем новый объект для покупателя, чтобы ссылка не дублировалась
                        Product customerProduct = new Product();
                        customerProduct.setName(expectedProduct.getName());
                        customerProduct.setQuantity(quantity);
                        customerProduct.setPrice(product.getPrice());

                        //Добавляем количество продукт у покупателя
                        customer.addPurchase(customerProduct);

                        long requiredCash = (long) product.getPrice() * quantity;
                        //Увеличиваем кэш продавца
                        setCash(getCash() + requiredCash);
                        //Уменьшаем кэш покупателя
                        customer.setCash(customer.getCash() - requiredCash);
                    }
                }
            }
        }
    }

    //определяет наличие продукта в достаточном количестве
    public Product productExists(Product expectedProduct) {
        for (Product product : products) {
            // Проверяем по имени товара что у продавца есть продукт
            if (product.getName().equals(expectedProduct.getName())) {
                // Проверяем что количество товара >= чем мы хотим купить
                if (product.getQuantity() >= expectedProduct.getQuantity()) {
                    return product;
                }
            }
        }
        return null;
    }

    // это искомый продавец?
    public boolean itsHim(String name, String lastName) {
        return this.name.equals(name) && this.lastName.equals(lastName);
    }

    //info about seller
    public void info() {
        StringBuilder result = new StringBuilder("Продавец: ");
        result.append(name);
        result.append(" ");
        result.append(lastName);
        result.append("; ");
        for (Product product : products) {
            result.append(product.getName());
            result.append(", ");
            result.append(product.getQuantity());
            result.append(" шт., по ");
            result.append(product.getPrice());
            result.append(" руб.; ");
        }
        result.append("Кэш: ");
        result.append(getCash());
        result.append(" руб.");
        System.out.println(result);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
