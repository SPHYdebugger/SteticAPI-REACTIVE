package com.home.SteticApi.service;

import com.home.SteticApi.domain.Product;
import com.home.SteticApi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll()  {

        return productRepository.findAll();
    }

    public Optional<Product> findProductById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }
    public List<Product> findDangerousProducts(boolean dangerous) { return productRepository.findByDangerous(dangerous);}
    public List<Product> findProductsByPrice(float price){
        return productRepository.findByPrice(price);
    }
    public List<Product> findProductByIdAndNameAndPrice(int id, String name, float price) {
        return productRepository.findByIdAndNameAndPrice(id, name, price);
    }

    public void saveProduct(Product product) {
        product.setRegistrationDate(LocalDate.now().toString());
        productRepository.save(product);
    }

    public void removeProduct(long productId) {
        productRepository.deleteById(productId);
    }

  public void modifyProduct(Product newProduct, long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
          Product existingProduct = product.get();
          existingProduct.setName(newProduct.getName());
          existingProduct.setSize(newProduct.getSize());
          existingProduct.setDescription(newProduct.getDescription());
          existingProduct.setPrice(newProduct.getPrice());
          existingProduct.setDangerous(newProduct.isDangerous());
          productRepository.save(existingProduct);
        }
    }
}