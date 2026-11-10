import java.util.Comparator;

public class OrderTotalComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        return Double.compare(a.getTotal(), b.getTotal());
    }
}