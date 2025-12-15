
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    // ----------------------------------------------------
    // 1. 아이템 맵 (Key: 아이템 이름, Value: 아이템 정보와 수량)
    // ----------------------------------------------------
    private Map<String, ItemQuantity> items;

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public Inventory() {
        items = new HashMap<>();
    }

    // ----------------------------------------------------
    // 3. 아이템 추가 (수량 관리)
    // ----------------------------------------------------
    public void addItem(Item item) {
        String itemName = item.getName();
        if (items.containsKey(itemName)) {
            // 이미 있는 아이템이면 수량만 증가
            items.get(itemName).incrementQuantity();
            System.out.println(itemName + "을(를) 추가로 가방에 넣었습니다. (현재 수량: " + items.get(itemName).getQuantity() + ")");
        } else {
            // 없는 아이템이면 새로 추가
            items.put(itemName, new ItemQuantity(item, 1));
            System.out.println(item.getName() + "을(를) 가방에 넣었습니다!");
        }
    }

    // ----------------------------------------------------
    // 4. 아이템 사용 (수량 관리)
    // ----------------------------------------------------
    public boolean useItem(Item item, ImaginaryBugi bugi) {
        String itemName = item.getName();
        ItemQuantity itemInBag = items.get(itemName);

        if (itemInBag == null || itemInBag.getQuantity() <= 0) {
            System.out.println(itemName + "이(가) 가방에 없습니다.");
            return false;
        }

        // 아이템 효과 적용
        boolean success = item.use(bugi);

        if (success) {
            // 사용 성공 시 수량 감소
            itemInBag.decrementQuantity();
            // 수량이 0이 되면 맵에서 제거
            if (itemInBag.getQuantity() <= 0) {
                items.remove(itemName);
                System.out.println(itemName + "을(를) 모두 사용했습니다.");
            } else {
                System.out.println(itemName + "을(를) 사용했습니다. (남은 수량: " + itemInBag.getQuantity() + ")");
            }
        }
        return success;
    }

    // ----------------------------------------------------
    // 5. 아이템 리스트 출력 (콘솔용)
    // ----------------------------------------------------
    public void showInventory() {
        System.out.println("===== 가방 목록 =====");
        if (items.isEmpty()) {
            System.out.println("가방이 비어 있습니다!");
        } else {
            for (ItemQuantity iq : items.values()) {
                System.out.println("- " + iq.getItem().getName() + " (수량: " + iq.getQuantity() + ")");
            }
        }
        System.out.println("=====================");
    }

    // ----------------------------------------------------
    // 6. Getter
    // ----------------------------------------------------
    public int getItemCount() {
        return items.size(); // 고유 아이템 종류의 수
    }

    public Collection<ItemQuantity> getItems() {
        return items.values();
    }
}
