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
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders")
	public class OrderController {
	@Autowired
	OrderDao dao;
	
    @GetMapping("")
	public ResponseEntity<String> get_all_orders(){
		try {
			return new ResponseEntity<String>(dao.getAllOrders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	

    @GetMapping("{id}")
	public ResponseEntity<String> get_order(@PathVariable("id") String id){
		try {
			return new ResponseEntity<String>(dao.getOrder(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("")
	public ResponseEntity<String> add_order(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.addOrder(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @PutMapping("")
	public ResponseEntity<String> update_order(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.updateOrder(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("update failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @DeleteMapping("")
	public ResponseEntity<String> delete_order(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.deleteOrder(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("delete failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	
	}
    
    
    
}
