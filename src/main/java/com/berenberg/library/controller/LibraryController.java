package com.berenberg.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemType;
import com.berenberg.library.model.Library;

@RestController
@RequestMapping("/library")
public class LibraryController {

	@Autowired
	Library library;
	
	@GetMapping("v1/items/type/{type}")
	public List<Item> getItemsByType(@PathVariable ItemType type) {
		
		return library.getCurrentInventory(type);
	}
	
	@GetMapping("v1/items/")
	public List<Item> getItems() {
		
		return library.getCurrentInventory();
	}
	
	@PostMapping("v1/item/borrow/{libCardNum}")
	public String borrowItem(@PathVariable Integer libCardNum, @RequestParam("type") ItemType itemType, @RequestParam("title") String title) {
		
		Item borrowItem = new Item();
		borrowItem.setTitle(title);
		borrowItem.setType(itemType);
		
		 if(library.borrowItem(libCardNum, borrowItem)) {
			 return borrowItem.getType()+" with title "+borrowItem.getTitle()+" is successfully Borrowed";
		 }
		 
		 return "Not available";
	}
	
	@PostMapping("v1/item/return/{libCardNum}")
	public String returnItem(@PathVariable Integer libCardNum, @RequestParam("type") ItemType itemType, @RequestParam("title") String title) {
		
		Item borrowItem = new Item();
		borrowItem.setTitle(title);
		borrowItem.setType(itemType);
		
		
		if(library.returnItem(libCardNum, borrowItem)) {
			return "Successfully returned";
		}
		
		 
		 return "Not available";
	}
	
	@GetMapping("v1/item/borrowed/{libCardNum}")
	public List<Item> userBorrowedItems(@PathVariable Integer libCardNum) {
		
		return library.getBorrowedItemsForUser(libCardNum);
		
	}
	
	@GetMapping("v1/items/overdue/")
	public List<Item> getItemsOverdue() {
		
		 	return library.getOverdueItems();
		 
	}
}
