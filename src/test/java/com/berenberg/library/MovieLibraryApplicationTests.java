package com.berenberg.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemType;
import com.berenberg.library.model.Library;

@SpringBootTest
class MovieLibraryApplicationTests {

	@Autowired
	private Library library;
	
	Integer userLibCardNum = 1;

	@Test
	public void testBorrowItem() {

		Item book = new Item();
		book.setItemID(1);
		book.setUniqueId(1);
		book.setType(ItemType.Book);
		book.setTitle("MyBook");

		library.addItemToInventory(book);

		library.borrowItem(userLibCardNum,book);

		List<Item> currentInventory = library.getCurrentInventory();

		assertEquals(currentInventory.size(), 9);
	}

	@Test
	public void testReturnItem() {
		Item book = new Item();
		book.setItemID(1);
		book.setUniqueId(1);
		book.setType(ItemType.Book);
		book.setTitle("MyBook");

		library.addItemToInventory(book);

		library.borrowItem(userLibCardNum, book);

		library.returnItem(userLibCardNum, book);
		
		List<Item> currentInventory = library.getCurrentInventory();

		assertTrue(currentInventory.contains(book));
	}


	@Test 
	public void testOverdueItems() { 
	
		Item book = new Item();
		book.setItemID(1);
		book.setUniqueId(2);
		book.setType(ItemType.Book);
		book.setTitle("MyBook");

		library.addItemToInventory(book);

	    library.borrowItem(userLibCardNum, book);
	  
	    // Change the due date to a past date to make it overdue 
	    book.setDueDate(new Date(System.currentTimeMillis() - 86400000)); // 24 hours ago
	  
	    List<Item> overdueItems = library.getOverdueItems();
	    
	    assertTrue(overdueItems.contains(book)); 
	  }

}
