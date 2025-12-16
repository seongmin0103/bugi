
// package bugi;

import java.util.ArrayList;

public class ItemManager {

    // ----------------------------------------------------
    // 1. 아이템 목록
    // ----------------------------------------------------
    private ArrayList<Item> items;

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public ItemManager() {
        items = new ArrayList<>();
        loadDefaultItems();
    }

    // ----------------------------------------------------
    // 3. 기본 아이템 로딩
    // ----------------------------------------------------
    private void loadDefaultItems() {
        // 음식
        items.add(new Item("샐러드", 10, 5, 20, -5, 0)); // name - price, hunger, health, mood(기분), energy
        items.add(new Item("피자", 20, 20, -10, 20, 5));
        items.add(new Item("햄버거", 20, 20, -10, 20, 5));
        items.add(new Item("마라탕", 20, 20, -10, 20, 5));
        items.add(new Item("라면", 10, 10, -5, 10, 2));

        items.add(new Item("에너지 드링크", 7, 0, -20, 5, 20));
        items.add(new Item("학식", 10, 10, 10, 10, 10));
        items.add(new Item("고구마", 5, 5, 10, 3, 5));
        items.add(new Item("바나나", 5, 5, 7, 5, 5));

        items.add(new Item("햇반", 5, 5, 5, 5, 5));
        items.add(new Item("김", 5, 1, 3, 5, 1));
        items.add(new Item("물", 1, 0, 2, 2, 0));
    }

    // ----------------------------------------------------
    // 4. 아이템 목록 출력
    // ----------------------------------------------------
    public void showItemList() {
        System.out.println("===== 상점 아이템 목록 =====");

        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.println((i + 1) + ". " + it.getName() +
                    " - 가격: " + it.getPrice() + "코인");
        }

        System.out.println("===========================");
    }

    // ----------------------------------------------------
    // 5. 아이템 가져오기
    // ----------------------------------------------------
    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    // ----------------------------------------------------
    // 6. 아이템 구매
    // ----------------------------------------------------
    public boolean buyItem(int index, ImaginaryBugi bugi) {
        Item item = getItem(index);
        if (item == null) {
            System.out.println("유효하지 않은 아이템 번호입니다.");
            return false;
        }

        boolean success = item.use(bugi);

        if (success) {
            System.out.println(item.getName() + " 구매 및 사용 완료!");
        }

        return success;
    }

    // 7. 전체 아이템 리스트 반환
    // ----------------------------------------------------
    public ArrayList<Item> getAvailableItems() {
        return items;
    }

    // ----------------------------------------------------
    // 8. 전체 아이템 수 
    // ----------------------------------------------------
    public int getItemCount() {
        return items.size();
    }
}

