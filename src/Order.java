import java.util.ArrayList;

public class Order {
    private int id;
    private int id_buyer;
    private int note;
    private int total;
    private int discount;
    private boolean is_paid;
    private ArrayList<OrderDetail> orderDetailArrayList;

    public Order(int id, int id_buyer, int note, int total, int discount, boolean is_paid) {
        this.id = id;
        this.id_buyer = id_buyer;
        this.note = note;
        this.total = total;
        this.discount = discount;
        this.is_paid = is_paid;
        orderDetailArrayList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_buyer() {
        return id_buyer;
    }

    public void setId_buyer(int id_buyer) {
        this.id_buyer = id_buyer;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public void addOrderDetailArrayList(OrderDetail orderDetail){
        orderDetailArrayList.add(orderDetail);
    }
}
