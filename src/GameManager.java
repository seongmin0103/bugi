

// package bugi;

public class GameManager {

    // ----------------------------------------------------
    // 1. 게임 전체 구성요소
    // ----------------------------------------------------
    private ImaginaryBugi bugi;
    private PlaceManager placeManager;
    private TimeSystem timeSystem;
    private ItemManager itemManager;
    private Inventory inventory;

    private Quest todayQuest;

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public GameManager() {
        bugi = new ImaginaryBugi("상상부기");
        placeManager = new PlaceManager();
        timeSystem = new TimeSystem();
        itemManager = new ItemManager();
        inventory = new Inventory();

        // 첫 퀘스트 생성
        todayQuest = Quest.createRandomQuest();
    }

    // ----------------------------------------------------
    // 3. 장소 이동 + 행동
    // ----------------------------------------------------
    public void goToPlace(String placeName) {
        placeManager.moveTo(placeName, bugi);
        timeSystem.nextTime(); // 행동 1번 = 시간 1칸 진행
        showStatus();
    }

    // ----------------------------------------------------
    // 4. 상점에서 아이템 구매
    // ----------------------------------------------------
    /** @deprecated use purchaseItemToInventory instead */
    public void buyItem(int index) {
        boolean bought = itemManager.buyItem(index, bugi);

        if (bought) {
            Item item = itemManager.getItem(index);
            inventory.addItem(item); // 가방에도 넣기
        }
    }

    // ----------------------------------------------------
    // 4-1. (New) 상점에서 아이템 구매 -> 보관함으로
    // ----------------------------------------------------
    public boolean purchaseItemToInventory(Item item) {
        if (bugi.getCoins().canAfford(item.getPrice())) {
            bugi.getCoins().spend(item.getPrice());
            inventory.addItem(item);
            System.out.println(item.getName() + "을(를) 구매하여 보관함에 넣었습니다.");
            return true;
        } else {
            System.out.println("코인이 부족하여 " + item.getName() + "을(를) 구매할 수 없습니다.");
            return false;
        }
    }

    // ----------------------------------------------------
    // 6. 퀘스트 완료
    // ----------------------------------------------------
    public void completeQuest() {
        todayQuest.complete(bugi);

        // 다음 날 새 퀘스트 생성
        todayQuest = Quest.createRandomQuest();
    }

    // ----------------------------------------------------
    // 7. 현재 게임 상태 출력
    // ----------------------------------------------------
    public void showStatus() {
        System.out.println("\n===== 게임 현황 =====");
        System.out.println("현재 장소: " + placeManager.getCurrentPlace().getName());
        System.out.println("시간대: " + timeSystem.getCurrentTime());
        System.out.println("Day: " + timeSystem.getDay());
        System.out.println("Semester: " + timeSystem.getSemester());
        System.out.println("Season: " + timeSystem.getCurrentSeason());  // 수정됨!

        System.out.println("부기 상태: " + bugi.getGauge());
        System.out.println("보유 코인: " + bugi.getCoins().getBalance());
        System.out.println("현재 퀘스트: " + todayQuest.getDescription());
        System.out.println("======================\n");
    }

    
 // 장소만 이동 (행동 X)
    public void movePlaceOnly(String placeName) {
        placeManager.changePlace(placeName);
    }

    // 현재 장소에서 행동 수행 + 시간 진행
    public String doCurrentPlaceAction() {
        Place currentPlace = placeManager.getCurrentPlace();
        String action = currentPlace.getAction();
        ImaginaryBugi bugi = this.bugi;
        StatusGauge gauge = bugi.getGauge();
        CoinManager coins = bugi.getCoins();

        switch (action) {
            case "sleep":
                gauge.updateStatus("sleep");
                break;

            case "study":
                if (gauge.getEnergy() < 10 || gauge.getHunger() < 10) {
                    return "에너지가 부족하거나 배가 고파서 공부할 수 없습니다.";
                }
                bugi.study();
                break;

            case "work":
                if (gauge.getEnergy() < 15 || gauge.getHunger() < 15) {
                    return "에너지가 부족하거나 배가 고파서 아르바이트를 할 수 없습니다.";
                }
                bugi.work();
                break;

            case "exercise":
                if (gauge.getEnergy() < 10 || gauge.getHunger() < 12) {
                    return "에너지가 부족하거나 배가 고파서 운동할 수 없습니다.";
                }
                bugi.exercise();
                break;

            case "wash":
                bugi.wash();
                break;

            case "heal":
                if (!coins.canAfford(20)) {
                    return "코인이 부족하여 치료를 받을 수 없습니다.";
                }
                bugi.heal();
                break;
            
            case "eat":
                 return "아이템을 사용해서 먹어야 합니다.";

            default:
                // 아무 행동도 없는 곳
                return null; 
        }

        // 행동 성공 시 시간 진행
        boolean isNewDay = timeSystem.nextTime();
        if (isNewDay) {
            bugi.getGauge().decreaseOverTime();
        }
        return null; // 성공
    }

    // ----------------------------------------------------
    // 8. Getter (UI에서 접근할 때 필요)
    // ----------------------------------------------------
    public ImaginaryBugi getBugi() {
        return bugi;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public TimeSystem getTimeSystem() {
        return timeSystem;
    }

    public PlaceManager getPlaceManager() {
        return placeManager;
    }

    public Quest getTodayQuest() {
        return todayQuest;
    }
    

}
