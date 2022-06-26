package com.project.projectTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Warehouse {

    private static final int SHELF_CAPACITY = 500;
    private static final List<String> SHELF_LITERS = new ArrayList<>();
    private static final List<String> SHELF_KG = new ArrayList<>();
    private static final Map<String, Object> WAREHOUSE = new HashMap<>();
    private static final Scanner SCAN = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String UNIT_KG = "kg";
    private static final String UNIT_LITER = "liter";
    
    private static String product = "";
    private static String brand = "";
    private static String entryDate = "";
    private static String expiryDate = "";
    private static String unit = "";
    private static String quantity = "";
    private static String comment = "";

	public static void main(String[] args) {
		do {
			switch (chooseWhatToDo()) {
			case "0": 
				break;
			case "1":
				printProductData();
				break;
			case "2":
				addNewDelivery();
				break;
			case "3":
				printDeliveriesForTimePeriod();
			}
			System.out.println("Press enter to continue or input \"exit\" to close the program.");
		} while (!(SCAN.nextLine()).equals("exit"));
	}

    private static String chooseWhatToDo(){
        System.out.println("Please choose what to do (0 - Exit from command menu; 1 - List all items; 2 - Add new delivery; 3 - List deliveries for time period)");
        String command;
        while (!validateInput(command = SCAN.nextLine())) {
        }
        return command;
    }

    private static boolean validateInput(String input) {
        if (input.length() == 1 && (input.charAt(0) > 47 && input.charAt(0) < 52)) {
            return true;
        }
        System.out.println("Invalid command! Try again!");
        return false;
    }

    private static void putProductOnTheShelf(List<String> shelf, int quantity) {
         for (int i = 0; i < quantity; i++) {
             if (shelf.size()-1 == SHELF_CAPACITY){
                 System.out.println("This shelf is full! There is no more space for:" + (quantity - i + 1) + " products.");
                 break;
             }
                 shelf.add(i, "1");
             if ( i == quantity-1 ){
                 System.out.println("Product was added successfully!");
             }
         }
    }
    
    private static void printProductData() {
        for (Map.Entry<String, Object> a : WAREHOUSE.entrySet()) {
            for (Map.Entry<String, Object> b : ((HashMap<String, Object>) a.getValue()).entrySet()) {
                System.out.println(b.getKey());
                System.out.println(b.getValue());
            }
        }
    }

    private static void getAllDeliveriesForTimePeriod(LocalDate date1, LocalDate date2) {
        for (Map.Entry<String, Object> a : WAREHOUSE.entrySet()) {
            for (Map.Entry<String, Object> b : ((HashMap<String, Object>) a.getValue()).entrySet()) {
                for (Map.Entry<String, Object> c : ((HashMap<String, Object>) b.getValue()).entrySet()) {
                    if (c.getKey().equalsIgnoreCase("entryDate")) {
                        printSelectedProduct((HashMap<String, Object>) b.getValue(), b.getKey(), (String) c.getValue(), date1, date2, a.getKey());
                    }
                }
            }
        }
    }

	private static void printSelectedProduct(Map<String, Object> entryDate, String brand, String parsedDate, LocalDate date1,
			LocalDate date2, String product) {
		LocalDate currentDate = LocalDate.parse(parsedDate, FORMATTER);
		if (currentDate.isAfter(date1) && currentDate.isBefore(date2)) {
			System.out.println("-----------------------------------------");
			System.out.println("product: " + product);
			System.out.println("brand: " + brand);
			for (Map.Entry<String, Object> a : entryDate.entrySet()) {
				System.out.print(a.getKey() + ": " + (String) a.getValue());
				System.out.println();
			}
			System.out.println("-----------------------------------------");
		}
	}

    private static void addNewDelivery(){
        System.out.println("Enter product:");
        while (!validateProduct()) {
            if (WAREHOUSE.get(product) == null) {
                WAREHOUSE.put(product, new HashMap<>());
            }
            validateAllInputData();
            checkShelfSpace(unit, Integer.parseInt(quantity));
            putProductInWarehouse(product, brand, entryDate, expiryDate, unit, comment, quantity);
            
            System.out.println("Enter product or go back to the menu (input command - back) ");
        }
    }

    private static boolean validateProduct() {
        while(!(product = SCAN.nextLine()).equals("back") && product.isEmpty()) {
            System.out.println("Product name can't be null or empty! Try again!");
        }
        if (product.equals("back")) {
            return true;
        }
        return false;
    }

    private static void validateAllInputData() {
        System.out.println("Enter brand:");
        validateBrand();

        System.out.println("Enter comment:");
        validateComment();

        System.out.println("Enter entry date:");
        validateEntryDate();

        System.out.println("Enter expiry date:");
        validateExpiryDate();

        System.out.println("Enter unit (liter or kg):");
        validateUnit();

        System.out.println("Enter quantity:");
        validateQuantity();
    }

    private static void validateBrand() {
        while ((brand = SCAN.nextLine()).isEmpty()) {
            System.out.println("Invalid brand name! Try again!");
        }
    }

    private static void validateComment() {
        while ((comment = SCAN.nextLine()).isEmpty() || comment.length() < 2 || comment.length() > 100) {
            System.out.println("Invalid comment! Try again! Comment must be between 2 and 100 characters.");
        }
    }

    private static String validateEntryDate() {
        boolean isValidDate = false;
        do {
            try {
                entryDate = SCAN.nextLine();
                isValidDate = !entryDate.isEmpty() && LocalDate.parse(entryDate, FORMATTER).isAfter(LocalDate.now());
            } catch (DateTimeParseException e) {
                System.out.println("You must input correct date format! Example: 10-10-2021");
            }
            if (!isValidDate) {
                System.out.println("Invalid entry date! Try again!");
            }
        } while (!isValidDate);
        return entryDate;
    }

    private static String validateExpiryDate() {
        boolean isValidDate = false;
        do {
            try {
                expiryDate = SCAN.nextLine();
                isValidDate = !expiryDate.isEmpty() && !LocalDate.parse(expiryDate, FORMATTER).isBefore(LocalDate.now());
            } catch (DateTimeParseException e) {
                System.out.println("You must input correct date format! Example: 10-10-2024");
            }
            if (!isValidDate) {
                System.out.println("Invalid expiry date! Try again!");
            }
        } while (!isValidDate);
        return expiryDate;
    }

    private static void validateUnit() {
        while (!(unit = SCAN.nextLine()).equalsIgnoreCase(UNIT_LITER) && !unit.equalsIgnoreCase(UNIT_KG) ) {
            System.out.println("Invalid unit! Enter liter or kg!");
        }
    }

    private static void validateQuantity() {
        boolean isValid = false;
        do {
            quantity = SCAN.nextLine();
            try {
                isValid = !quantity.isEmpty() && Integer.parseInt(quantity) > 0;
            } catch (NumberFormatException e) {
                System.out.println("You must enter digit!");
            }
            if (!isValid) {
                System.out.println("Invalid type of quantity! Try again!");
            }
        } while(!isValid);
    }

    private static void putProductInWarehouse(String product, String brand, String entryDate, String expiryDate, String unit, String comment, String quantity) {
    	((HashMap<String, Object>) WAREHOUSE.get(product)).put(brand, new HashMap<>());
        HashMap<String, Object> brands = ((HashMap<String, Object>) ((HashMap<String, Object>) WAREHOUSE.get(product)).get(brand));
        brands.put("entryDate", entryDate);
        brands.put("expiryDate", expiryDate);
        brands.put("unit", unit);
        brands.put("comment", comment);
        brands.put("quantity", quantity);
    }
    
    private static void checkShelfSpace(String unit, int currentQuantity) {
        if (unit.equalsIgnoreCase(UNIT_KG)) {
            putProductOnTheShelf(SHELF_KG, currentQuantity);
        } else if (unit.equalsIgnoreCase(UNIT_LITER)) {
            putProductOnTheShelf(SHELF_LITERS, currentQuantity);
        }
    }

    private static void printDeliveriesForTimePeriod(){
        System.out.println("After date :");
        String afterDate = validateEntryDate();
        LocalDate date1 = LocalDate.parse(afterDate, FORMATTER);

        System.out.println("Before date :");
        String beforeDate = validateExpiryDate();
        LocalDate date2 = LocalDate.parse(beforeDate, FORMATTER);
        
        getAllDeliveriesForTimePeriod(date1, date2);
    }

}
