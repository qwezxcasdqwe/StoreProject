package com.example.storeproject.services;

import com.example.storeproject.models.Image;
import com.example.storeproject.repositories.ProductRepository;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.storeproject.models.Product;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private long ID = 0;

    public List<Product> ListProducts(String author){
        if(author != null) return productRepository.findByAuthor(author);

        return productRepository.findAll();
    }
    public List<Product> ListByTitle(String title){
        if(title!=null) return productRepository.findByTitle(title);
        return productRepository.findAll();//обязательно данный метод должен быть реализован в репе
    }

    @Transactional
    public void saveProduct(Product product, MultipartFile file1,MultipartFile file2,MultipartFile file3 ) throws IOException{   //принимаем на загрузку 3 фото(ниже пример с массивом)
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize()!= 0){
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
        }

        log.info("Saving new {}",product);//метод toString подставит сюда строковое представление product
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setOriginalName(file.getOriginalFilename());//здесь методы тянутся от класса MultiPartFile а то я запутался :)
        image.setSize(file.getSize());
        image.setContentType(file.getContentType());
        image.setBytes(file.getBytes());
        image.setName(file.getName());
        return image;
    }

    public void deleteProduct(long id){
//        products.removeIf(product -> product.getId() == id);
        productRepository.deleteById(id);
    }
    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);//пробрасываем ошибку чтобы сделать соответсвие типов
    }


}






//    private List<Product> products = new ArrayList<>();
//    {
//        products.add(new Product(1,"Example tittle","Example authort","Examle city",5000,"Example description"));
//        products.add(new Product(2,"Gaming platform","Sony","New York",52000,"Simple description"));
//    }
//    public List<Product> listOfAll(String title){
//        return products;             //simple return added products
//    }



//    public void saveProduct(Product product, MultipartFile[] images) {
//        for (MultipartFile image : images) {
//            // Обработка каждого изображения
//        }
//    } пример приема изображений не по одному, а массивом