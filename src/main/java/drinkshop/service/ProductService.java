package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> validator;

    public ProductService(Repository<Integer, Product> productRepo, Validator<Product> validator) {
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addProduct(Product product) {
        validator.validate(product);
        productRepo.save(product);
    }

    public void updateProduct(Product updated) {
        validator.validate(updated);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
//        Iterable<Product> it=productRepo.findAll();
//        ArrayList<Product> products=new ArrayList<>();
//        it.forEach(products::add);
//        return products;

//        return StreamSupport.stream(productRepo.findAll().spliterator(), false)
//                    .collect(Collectors.toList());
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(String categorie) {
        if ("ALL".equals(categorie)) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> categorie.equals(p.getCategorie()))
                .toList();
    }

    public List<Product> filterByTip(String tip) {
        if ("ALL".equals(tip)) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> tip.equals(p.getTip()))
                .collect(Collectors.toList());
    }
}