package com.used_products_store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/categories")
		
	public class CategoryController {
	@Autowired
	CategoryDao dao;
		
	@GetMapping("get_all_categories")
	public ResponseEntity<String> get_all_categories(){
			try {
				return new ResponseEntity<String>(dao.getallcategories(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		}
	
	@PostMapping("add_category")
	public ResponseEntity<String> add_category(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.addcategory(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("add failed:" + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("update_category")
	public ResponseEntity<String> update_category(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.updatecategory(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("update failed:" + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @DeleteMapping("delete_category")
	public ResponseEntity<String> delete_category(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.deletecategory(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("delete failed:" + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	
	}
	
	
	
	
	
	
	
	
	
	
}
	

