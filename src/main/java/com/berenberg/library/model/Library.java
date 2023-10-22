package com.berenberg.library.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class Library {
	
	private Map<ItemType, List<Item>> inventory; // Map of item types to a list of items

	private Map<Integer, User> users; // Map of library card numbers to users
	
	
	public Library() {
		//for thread safe
        inventory = new ConcurrentHashMap<>();
        
        //assuming the users are pre-populated
        users = new HashMap<>();
        users.put(1, new User("user1", 1));
        users.put(2, new User("user2", 2));
        users.put(3, new User("user3", 3));
        users.put(4, new User("user4", 4));
        
        //loading the initial inventory 
        loadInventoryFromCSV("items.csv");
	}
	
	private void loadInventoryFromCSV(String csvFilePath) {
		
        try {
        	
            FileReader reader = new FileReader(csvFilePath);
    
            String[] HEADERS = { "UniqueID", "ItemID", "Type", "Title"};
            
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .build();
            
            CSVParser parse = csvFormat.parse(reader);
                    
            
            Map<Integer, Item> records = new HashMap<>();
            
            for (CSVRecord record : parse.getRecords()) {
                String title = record.get("Title");
                String type = record.get("Type");
                String uniqueID = record.get("UniqueID");
                String itemID = record.get("ItemID");
                
                
                // Create an item from the CSV data and add it to the inventory
                Item item = new Item();
                item.setItemID(Integer.parseInt(itemID));
                item.setUniqueId(Integer.parseInt(uniqueID));
                item.setTitle(title);
                item.setType(ItemType.valueOf(type));
                
                //if duplicate unique id it keeps only the latest record 
                records.put(item.getUniqueId(), item);
               
            }
            
            parse.close();
            
            for (Item item : records.values()) {
            	 addItemToInventory(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemToInventory(Item item) {
        ItemType itemType = item.getType();
        if (!inventory.containsKey(itemType)) {
            inventory.put(itemType, new CopyOnWriteArrayList<>());
        }
        inventory.get(itemType).add(item);
    }


    public List<Item> getCurrentInventory() {
        List<Item> availableItems = new CopyOnWriteArrayList<>();
        
        
        for (List<Item> items : inventory.values()) {
        	for (Item item : items) {
        		if( item.getDueDate() == null) 
        			availableItems.add(item);
        	}
        }
        
        //filter duplicate unique ID
         return availableItems.stream().distinct().toList();
         
        
    }
    
    
    public List<Item> getCurrentInventory(ItemType itemType) {
        
    	List<Item> availableItems = new CopyOnWriteArrayList<>();
        
        List<Item> items = inventory.get(itemType);
        
        //if no items are available for the selected type
        if(items != null)
    	for (Item item : items) {
    		if( item.getDueDate() == null) 
    			availableItems.add(item);
    	}
        
        //filter duplicate unique ID
        return availableItems.stream().distinct().toList();


    }

    public List<Item> getOverdueItems() {
        List<Item> overdueItems = new ArrayList<>();
        Date today = new Date();
        for (List<Item> items : inventory.values()) {
            for (Item item : items) {
                if (item.getDueDate() != null && item.getDueDate().before(today)) {
                    overdueItems.add(item);
                }
            }
        }
        return overdueItems;
    }

    public List<Item> getBorrowedItemsForUser(Integer libraryCardNumber) {
    	
    	User user = users.get(libraryCardNumber);
        return user.getBorrowedItems();
    }
   

    public Item getIfItemIsAvailable(ItemType type, String title) {
    	
        for (Item item : inventory.get(type)) {
        	
        	//If due date is null means item available
            if (item.getTitle().equals(title) && item.getDueDate() == null) {
                return item;
            }
        }
            
        return null;
    }

    public boolean borrowItem(Integer libraryCardNumber, Item item) {
        
    	Item itemAvailable = getIfItemIsAvailable(item.getType(), item.getTitle());
    	
    	if (itemAvailable != null) {
    		
    		//set duedate +7 days after
    		itemAvailable.setDueDate(calculateDueDate());
    		User user = users.get(libraryCardNumber);
    		
    		user.getBorrowedItems().add(itemAvailable);
            return true;
        }
        return false;
    }

    public boolean returnItem(Integer libraryCardNumber,Item returnItem) {
    	
    	 User user = users.get(libraryCardNumber);
    	
    	 for (Item item :  user.getBorrowedItems()) {
         	
         	//If due date is null means item available
             if (item.getTitle().equals(returnItem.getTitle()) && item.getType().equals(returnItem.getType()) && item.getDueDate() != null) {
            	 
            	 for (Item inventoryItem : inventory.get(returnItem.getType())) {
                 	
                     if (inventoryItem.getTitle().equals(returnItem.getTitle()) ) {
                    	 
                    	 inventoryItem.setDueDate(null);
                    	 break;
                     }
                 }
            	 
                 user.getBorrowedItems().remove(item);
                 return true;
             }
         }
    	 
    	 return false;
    	
       
    }

    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return calendar.getTime();
    }
}
