import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Store {
    private final List<Product> products = new ArrayList<>();
    private final Map<Integer, Product> productById = new HashMap<>();
    private final Map<Integer, Order> orders = new HashMap<>();
    private final Set<String> categories = new HashSet<>();
    private final Queue<Order> shippingList = new ArrayDeque<>();
    private final Map<Integer, Order> deliveredOrders = new LinkedHashMap<>();
    private final List<Review> reviews = new ArrayList<>();

    public boolean addProduct(Product product) {
        if (productById.containsKey(product.getId())) {
            System.out.println("A product with ID " + product.getId() + " already exists.");
            return false;
        }
        products.add(product);
        productById.put(product.getId(), product);
        categories.add(product.getCategory());
        System.out.println("Product added.");
        return true;
    }

    private void deleteProductEverywhere(int id) {
        deleteProductEverywhere(id, null);
    }

    private void deleteProductEverywhere(int id, Iterator<Product> activeIterator) {
        if (activeIterator != null) {
            activeIterator.remove();
        } else {
            Iterator<Product> it = products.iterator();
            while (it.hasNext()) {
                if (it.next().getId() == id) {
                    it.remove();
                    break;
                }
            }
        }
        productById.remove(id);
    }

    public void removeProduct(int id) {
        if (!productById.containsKey(id)) {
            System.out.println("Product not found.");
            return;
        }
        deleteProductEverywhere(id);
        System.out.println("Product removed.");
    }

    public void displayAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        for (Product p : products) {
            System.out.println(p);
        }
    }

    public void searchProduct(int id) {
        Product p = productById.get(id);
        if (p == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println(p);
        }
    }

    public void showAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
        for (String c : categories) {
            System.out.println(c);
        }
    }

    public void displayProductsByPrice() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        List<Product> copy = new ArrayList<>(products);
        Collections.sort(copy);
        for (Product p : copy) {
            System.out.println(p);
        }
    }

    public void createOrder(int orderId, String customerName) {
        if (orders.containsKey(orderId)) {
            System.out.println("An order with ID " + orderId + " already exists.");
            return;
        }
        orders.put(orderId, new Order(orderId, customerName));
        System.out.println("Order created with status " + OrderStatus.PENDING + ".");
    }

    public void addItemToOrder(int orderId, int productId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        Product product = productById.get(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        try {
            order.addItem(product, quantity);
            System.out.println("Item added. Order total: " + order.getTotal());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeItemFromOrder(int orderId, int productId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        try {
            if (order.removeItem(productId)) {
                System.out.println("Item removed. Order total: " + order.getTotal());
            } else {
                System.out.println("That product is not in the order.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        order.displayOrder();
    }

    public void addOrderToShippingList(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Only Pending orders can be added to the shipping list. Current status: " + order.getStatus());
            return;
        }
        if (order.getItems().isEmpty()) {
            System.out.println("An order with no items cannot be added to the shipping list.");
            return;
        }
        if (shippingList.contains(order)) {
            System.out.println("This order is already in the shipping list.");
            return;
        }
        shippingList.offer(order);
        order.updateStatus(OrderStatus.SHIPPED);
        System.out.println("Order added to the shipping list.");
    }

    public void shipNextOrder() {
        if (shippingList.isEmpty()) {
            System.out.println("There are no orders waiting to be shipped.");
            return;
        }
        Order next = shippingList.peek();
        if (next.getItems().isEmpty()) {
            System.out.println("This order has no items and cannot be shipped.");
            return;
        }
        shippingList.poll();
        next.updateStatus(OrderStatus.DELIVERED);
        deliveredOrders.put(next.getOrderId(), next);
        System.out.println("Order " + next.getOrderId() + " delivered.");
    }

    public void cancelOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        OrderStatus status = order.getStatus();
        if (status == OrderStatus.DELIVERED) {
            System.out.println("A delivered order cannot be cancelled.");
            return;
        }
        if (status == OrderStatus.CANCELLED) {
            System.out.println("This order is already cancelled.");
            return;
        }
        if (status == OrderStatus.SHIPPED) {
            shippingList.remove(order);
        }
        order.updateStatus(OrderStatus.CANCELLED);
        System.out.println("Order cancelled.");
    }

    public void searchOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.displayOrder();
        }
    }

    public void addReview(int productId, String customerName, String comment) {
        if (!productById.containsKey(productId)) {
            System.out.println("Product not found. Review rejected.");
            return;
        }
        reviews.add(new Review(productId, customerName, comment));
        System.out.println("Review added.");
    }

    public void showReviewsForProduct(int productId) {
        boolean found = false;
        for (Review r : reviews) {
            if (r.getProductId() == productId) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reviews found for this product.");
        }
    }

    public void removeOutOfStockProducts() {
        int removed = 0;
        Iterator<Product> it = products.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getStockQuantity() == 0) {
                deleteProductEverywhere(p.getId(), it);
                removed++;
            }
        }
        System.out.println(removed + " out-of-stock product(s) removed.");
    }

    public void displayOrdersByTotal() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        List<Order> copy = new ArrayList<>(orders.values());
        copy.sort(new OrderTotalComparator());
        for (Order o : copy) {
            System.out.println(o);
        }
    }

    public void displayDeliveredOrders() {
        if (deliveredOrders.isEmpty()) {
            System.out.println("No delivered orders.");
            return;
        }
        for (Order o : deliveredOrders.values()) {
            System.out.println(o);
        }
    }
}