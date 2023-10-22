package com.berenberg.library.model;

import java.util.Date;
import java.util.Objects;

public class Item{
	
    private int uniqueId;
    private int itemID;
    private ItemType type;
    private String title;
    private Date dueDate;
    
    
	public int getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Override
    public boolean equals(Object obj) {
       
        Item other = (Item) obj;
        
        return (this.itemID == other.itemID && this.title.equals(other.title)) ;
    }
	
	@Override
    public int hashCode() {
        return Objects.hash(itemID+title);
   }
    
	
	
	
}
	

