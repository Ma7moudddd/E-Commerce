import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order {
    private final int orderId;
    private final String customerName;
    private final List<CartItem> items = new ArrayList<>();
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.status = OrderStatus.PENDING;
        this.total = 0;
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getTotal() { return total; }
    public OrderStatus getStatus() { return status; }
    public List<CartItem> getItems() { return items; }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    private void ensureModifiable() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order " + orderId + " is " + status + " and cannot be modified.");
        }
    }

    public void addItem(Product product, int quantity) {
        ensureModifiable();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        boolean merged = false;
        for (int i = 0; i < items.size(); i++) {
            CartItem existing = items.get(i);
            if (existing.getProduct().getId() == product.getId()) {
                existing.setQuantity(existing.getQuantity() + quantity);
                merged = true;
                break;
            }
        }
        if (!merged) {
            items.add(new CartItem(product, quantity));
        }
        calculateTotal();
    }

    public boolean removeItem(int productId) {
        ensureModifiable();
        boolean removed = false;
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getProduct().getId() == productId) {
                it.remove();
                removed = true;
                break;
            }
        }
        calculateTotal();
        return removed;
    }

    public void calculateTotal() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.calculateSubtotal();
        }
        this.total = sum;
    }

    public void displayOrder() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customerName);
        System.out.println("Status: " + status);
        if (items.isEmpty()) {
            System.out.println("  (no items)");
        }
        for (CartItem item : items) {
            System.out.println("  " + item.getProduct().getName() + " x " + item.getQuantity()
                    + " = " + item.calculateSubtotal());
        }
        System.out.println("Total: " + total);
    }

    @Override
    public String toString() {
        return "Order{id=" + orderId + ", customer='" + customerName + "', status=" + status + ", total=" + total + "}";
    }
}