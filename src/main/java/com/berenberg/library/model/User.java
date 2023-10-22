package com.berenberg.library.model;

import java.util.ArrayList;
import java.util.List;

class User {
	
    private String name;
    private int libraryCardNumber;
    private List<Item> borrowedItems;

    public User(String name, int libraryCardNumber) {
        this.name = name;
        this.libraryCardNumber = libraryCardNumber;
        borrowedItems = new ArrayList<>();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLibraryCardNumber() {
		return libraryCardNumber;
	}

	public void setLibraryCardNumber(int libraryCardNumber) {
		this.libraryCardNumber = libraryCardNumber;
	}

	public List<Item> getBorrowedItems() {
		return borrowedItems;
	}

	public void setBorrowedItems(List<Item> borrowedItems) {
		this.borrowedItems = borrowedItems;
	}
    
    
}