import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ElecBill {
    private static ArrayList<Bill> billHistory = new ArrayList<>();
    private static Map<String, Bill> customerBills = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            System.out.print("Enter the customer type (1 for Residential, 2 for Commercial): ");
            int customerType = scanner.nextInt();

            System.out.print("Enter the number of units consumed: ");
            int unitsConsumed = scanner.nextInt();

            System.out.print("Enter the customer name or ID: ");
            String customerIdentifier = scanner.next();

            Bill bill = calculateBill(unitsConsumed, customerType, customerIdentifier);
            displayBill(bill);

            billHistory.add(bill); // Add the bill to the history
            customerBills.put(customerIdentifier, bill); // Store the customer's bill in the HashMap

            System.out.print("Do you want to calculate for another customer? (yes/no): ");
            choice = scanner.next().charAt(0);
        } while (choice == 'y' || choice == 'Y');

        // Show bill history for a specific customer
        System.out.print("Do you want to view the bill history for a specific customer? (yes/no): ");
        choice = scanner.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            System.out.print("Enter the customer name or ID: ");
            String customerIdentifier = scanner.next();
            viewBillHistory(customerIdentifier);
        }

        // Search for customers and view their bill details
        System.out.print("Do you want to search for a customer and view their bill details? (yes/no): ");
        choice = scanner.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            System.out.print("Enter the customer name: ");
            String customerName = scanner.next();
            searchCustomerAndDisplayBill(customerName);
        }

        System.out.println("Thank you for using the Electricity Bill Calculator.");
        scanner.close();
    }

    public static Bill calculateBill(int unitsConsumed, int customerType, String customerIdentifier) {
        double billAmount = 0.0;
        int fixedCharge;
        int chargePerUnit;

        if (customerType == 1) { // Residential customer
            fixedCharge = 50;
            chargePerUnit = 10;
        } else if (customerType == 2) { // Commercial customer
            fixedCharge = 100;
            chargePerUnit = 15;
        } else {
            System.out.println("Invalid customer type!");
            return null;
        }

        if (unitsConsumed <= 100) {
            billAmount = fixedCharge + (unitsConsumed * chargePerUnit);
        } else if (unitsConsumed <= 200) {
            int first100Units = 100 * chargePerUnit;
            int remainingUnits = unitsConsumed - 100;
            int additionalUnitsCharge = remainingUnits * (chargePerUnit + 5);
            billAmount = fixedCharge + first100Units + additionalUnitsCharge;
        } else {
            int first100Units = 100 * chargePerUnit;
            int next100Units = 100 * (chargePerUnit + 5);
            int remainingUnits = unitsConsumed - 200;
            int additionalUnitsCharge = remainingUnits * (chargePerUnit + 10);
            billAmount = fixedCharge + first100Units + next100Units + additionalUnitsCharge;
        }

        double taxPercentage = 0.10;
        double taxAmount = billAmount * taxPercentage;
        billAmount += taxAmount;

        double discountPercentage = 0.05;
        double discountThreshold = 200;
        if (billAmount > discountThreshold) {
            double discountAmount = billAmount * discountPercentage;
            billAmount -= discountAmount;
        }

        Bill bill = new Bill(customerType, unitsConsumed, fixedCharge, chargePerUnit, taxAmount, discountPercentage, billAmount, customerIdentifier);
        return bill;
    }

    public static void displayBill(Bill bill) {
        System.out.println("\n------- Electricity Bill -------");
        System.out.println("Customer Type: " + (bill.getCustomerType() == 1 ? "Residential" : "Commercial"));
        System.out.println("Customer Identifier: " + bill.getCustomerIdentifier());
        System.out.println("Units Consumed: " + bill.getUnitsConsumed());
        System.out.println("Fixed Charge: $" + bill.getFixedCharge());
        System.out.println("Charge per Unit: $" + bill.getChargePerUnit());
        System.out.println("Tax: $" + bill.getTaxAmount());
        System.out.println("Discount: " + (bill.getDiscountPercentage() * 100) + "%");
        System.out.println("Total Bill Amount: $" + bill.getBillAmount());
        System.out.println("-------------------------------\n");
    }

    public static void viewBillHistory(String customerIdentifier) {
        System.out.println("\n------- Bill History for " + customerIdentifier + " -------");
        boolean found = false;

        for (Bill bill : billHistory) {
            if (bill.getCustomerIdentifier().equalsIgnoreCase(customerIdentifier)) {
                displayBill(bill);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No bill history found for " + customerIdentifier);
        }

        System.out.println("--------------------------\n");
    }

    public static void searchCustomerAndDisplayBill(String customerName) {
        System.out.println("\n------- Bill Details for Customer: " + customerName + " -------");
        Bill bill = customerBills.get(customerName);

        if (bill != null) {
            displayBill(bill);
        } else {
            System.out.println("Customer not found or no bill details available.");
        }

        System.out.println("--------------------------\n");
    }
}

class Bill {
    private int customerType;
    private int unitsConsumed;
    private int fixedCharge;
    private int chargePerUnit;
    private double taxAmount;
    private double discountPercentage;
    private double billAmount;
    private String customerIdentifier;

    public Bill(int customerType, int unitsConsumed, int fixedCharge, int chargePerUnit, double taxAmount, double discountPercentage, double billAmount, String customerIdentifier) {
        this.customerType = customerType;
        this.unitsConsumed = unitsConsumed;
        this.fixedCharge = fixedCharge;
        this.chargePerUnit = chargePerUnit;
        this.taxAmount = taxAmount;
        this.discountPercentage = discountPercentage;
        this.billAmount = billAmount;
        this.customerIdentifier = customerIdentifier;
    }

    // Getters
    public int getCustomerType() {
        return customerType;
    }

    public int getUnitsConsumed() {
        return unitsConsumed;
    }

    public int getFixedCharge() {
        return fixedCharge;
    }

    public int getChargePerUnit() {
        return chargePerUnit;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }
}