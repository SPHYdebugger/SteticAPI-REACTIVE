package com.home.SteticApi.service;

import com.home.SteticApi.domain.Shop;
import com.home.SteticApi.exception.ShopException.ShopNotFoundException;
import com.home.SteticApi.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    public Optional<Shop> findById(long id) {
        return shopRepository.findById(id);
    }
    public List<Shop> findShopByName(String name) { return shopRepository.findByName(name);}
    public List<Shop> findShopsByCity (String city) { return shopRepository.findByCity(city);}
    public List<Shop> findShopsBySolarium (boolean solarium) { return shopRepository.findBySolarium(solarium);}

    public void saveShop(Shop shop) {
        shop.setOpenDate(LocalDate.now().toString());
        shopRepository.save(shop);
    }

    @Transactional
    public void removeShop(long id) {
        shopRepository.deleteById(id);
    }

    public void modifyShop(Shop newShop, long id) throws ShopNotFoundException {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isPresent()) {
            Shop existingShop = shop.get();
            existingShop.setName(newShop.getName());
            existingShop.setAddress(newShop.getAddress());
            existingShop.setEmployeesNumber(newShop.getEmployeesNumber());
            existingShop.setCity(newShop.getCity());
            existingShop.setMeters(newShop.getMeters());
            existingShop.setSolarium(newShop.isSolarium());
            existingShop.setOpenDate(newShop.getOpenDate());
            shopRepository.save(existingShop);
        } else {
            throw new ShopNotFoundException(id);
        }
    }
}
