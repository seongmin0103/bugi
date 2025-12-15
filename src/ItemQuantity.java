
public class ItemQuantity {
    private Item item;
    private int quantity;

    public ItemQuantity(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        this.quantity--;
    }

    @Override
    public String toString() {
        return "ItemQuantity{" +
                "item=" + item.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
