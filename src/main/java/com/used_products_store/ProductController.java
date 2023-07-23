package com.used_products_store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/products")
	public class ProductController {
	@Autowired
	ProductDao dao;
	
    @GetMapping("get_all_products")
	public ResponseEntity<String> get_all_products(){
		try {
			return new ResponseEntity<String>(dao.getAllProducts(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping("get_product")
	public ResponseEntity<String> get_product(@RequestParam("name") String name){
		try {
			return new ResponseEntity<String>(dao.getProductByName(name), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
    
    @GetMapping("category/{id}")
	public ResponseEntity<String> get_category_products(@PathVariable("id") String id){
		try {
			return new ResponseEntity<String>(dao.getCategoryProducts(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("add_product")
	public ResponseEntity<String> add_product(@RequestParam String hmap, @RequestParam(value="image") MultipartFile file){
		try {
			return new ResponseEntity<String>(dao.addproduct(hmap, file), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("add failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @PutMapping("update_product")
	public ResponseEntity<String> update_product(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.updateproduct(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("update failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @DeleteMapping("delete_product")
	public ResponseEntity<String> delete_product(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.deleteproduct(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("delete failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	
	}
    
    
    
}
