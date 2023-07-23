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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
	public class UserController {
	@Autowired
	UserDao dao;
	
    @GetMapping("get_all_users")
	public ResponseEntity<String> get_all_users(){
		try {
			return new ResponseEntity<String>(dao.getAllUsers(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping("get_user")
	public ResponseEntity<String> get_user(@RequestParam("user_name") String user_name){
		try {
			return new ResponseEntity<String>(dao.getUser(user_name), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.register(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("register failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @PutMapping("update_user")
	public ResponseEntity<String> update_user(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.updateUser(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("update failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
    @DeleteMapping("delete_user")
	public ResponseEntity<String> delete_user(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.deleteUser(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("delete failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	
	}
    
    @PostMapping("login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.login(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("login failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
    
    @PostMapping("reset_password")
	public ResponseEntity<String> reset_password(@RequestBody Map<String, String> hmap){
		try {
			return new ResponseEntity<String>(dao.resetPassword(hmap), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("reset failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
