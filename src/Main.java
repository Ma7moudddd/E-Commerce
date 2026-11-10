import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }

    private static int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Display All Products");
        System.out.println("4. Search Product by ID");
        System.out.println("5. Show All Categories");
        System.out.println("6. Display Products Ordered by Price");
        System.out.println("7. Create Order");
        System.out.println("8. Add Item to Order");
        System.out.println("9. Remove Item from Order");
        System.out.println("10. Display Order");
        System.out.println("11. Add Order to the Shipping List");
        System.out.println("12. Ship Next Order");
        System.out.println("13. Cancel Order");
        System.out.println("14. Search Order by ID");
        System.out.println("15. Add Review to a Product");
        System.out.println("16. Show All Reviews for a Product");
        System.out.println("17. Remove Out-of-Stock Products");
        System.out.println("18. Display Orders Ordered by Total");
        System.out.println("19. Exit");
    }

    public static void main(String[] args) {
        Store store = new Store();
        store.addProduct(new Product(1, "Wireless Mouse", 250, "Electronics", 15));
        store.addProduct(new Product(2, "Notebook", 30, "Stationery", 0));
        store.addProduct(new Product(3, "Desk Lamp", 180, "Home", 8));
        store.addProduct(new Product(4, "USB Cable", 60, "Electronics", 20));

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: {
                    int id = readInt("Product ID: ");
                    String name = readLine("Name: ");
                    double price = readDouble("Price: ");
                    String category = readLine("Category: ");
                    int stock = readInt("Stock quantity: ");
                    if (price < 0 || stock < 0) {
                        System.out.println("Price and stock cannot be negative.");
                    } else if (name.isEmpty() || category.isEmpty()) {
                        System.out.println("Name and category cannot be empty.");
                    } else {
                        store.addProduct(new Product(id, name, price, category, stock));
                    }
                    break;
                }
                case 2:
                    store.removeProduct(readInt("Product ID: "));
                    break;
                case 3:
                    store.displayAllProducts();
                    break;
                case 4:
                    store.searchProduct(readInt("Product ID: "));
                    break;
                case 5:
                    store.showAllCategories();
                    break;
                case 6:
                    store.displayProductsByPrice();
                    break;
                case 7: {
                    int id = readInt("Order ID: ");
                    String customer = readLine("Customer name: ");
                    if (customer.isEmpty()) {
                        System.out.println("Customer name cannot be empty.");
                    } else {
                        store.createOrder(id, customer);
                    }
                    break;
                }
                case 8: {
                    int orderId = readInt("Order ID: ");
                    int productId = readInt("Product ID: ");
                    int qty = readInt("Quantity: ");
                    store.addItemToOrder(orderId, productId, qty);
                    break;
                }
                case 9: {
                    int orderId = readInt("Order ID: ");
                    int productId = readInt("Product ID: ");
                    store.removeItemFromOrder(orderId, productId);
                    break;
                }
                case 10:
                    store.displayOrder(readInt("Order ID: "));
                    break;
                case 11:
                    store.addOrderToShippingList(readInt("Order ID: "));
                    break;
                case 12:
                    store.shipNextOrder();
                    break;
                case 13:
                    store.cancelOrder(readInt("Order ID: "));
                    break;
                case 14:
                    store.searchOrder(readInt("Order ID: "));
                    break;
                case 15: {
                    int productId = readInt("Product ID: ");
                    String customer = readLine("Customer name: ");
                    String comment = readLine("Comment: ");
                    store.addReview(productId, customer, comment);
                    break;
                }
                case 16:
                    store.showReviewsForProduct(readInt("Product ID: "));
                    break;
                case 17:
                    store.removeOutOfStockProducts();
                    break;
                case 18:
                    store.displayOrdersByTotal();
                    break;
                case 19:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}