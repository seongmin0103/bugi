// package bugi;

import java.util.HashMap;

public class PlaceManager {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private HashMap<String, Place> placeMap;   // 장소 목록
    private Place currentPlace;                // 현재 장소

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public PlaceManager() {
        placeMap = new HashMap<>();
        loadPlaces();   // 기본 장소 로딩
        currentPlace = placeMap.get("집"); // 시작 장소는 집
    }

    // ----------------------------------------------------
    // 3. 장소 초기 설정
    // ----------------------------------------------------
    private void loadPlaces() {
        placeMap.put("집", new Place("집", "sleep"));          // 나중에 eat도 UI에서 관리
        placeMap.put("학교", new Place("학교", "study"));
        placeMap.put("카페", new Place("카페", "work"));
        placeMap.put("공원", new Place("공원", "exercise"));
        placeMap.put("욕실", new Place("욕실", "wash"));
        placeMap.put("병원", new Place("병원", "heal"));
    }

    // ----------------------------------------------------
    // 4. 장소 이동
    // ----------------------------------------------------
    public void moveTo(String placeName, ImaginaryBugi bugi) {

        if (!placeMap.containsKey(placeName)) {
            System.out.println("❌ 존재하지 않는 장소입니다: " + placeName);
            return;
        }

        // 장소 변경
        currentPlace = placeMap.get(placeName);

        System.out.println("📍 장소 이동: " + currentPlace.getName() + " 으로 이동했습니다.");

        // 이동한 장소에서 행동 수행
        currentPlace.performAction(bugi);
    }

    // ----------------------------------------------------
    // 5. 현재 장소 가져오기
    // ----------------------------------------------------
    public Place getCurrentPlace() {
        return currentPlace;
    }

    // ----------------------------------------------------
    // 6. 장소 목록 출력 (콘솔 테스트용)
    // ----------------------------------------------------
    public void showPlaces() {
        System.out.println("===== 이동할 수 있는 장소 =====");
        for (String p : placeMap.keySet()) {
            System.out.println("- " + p);
        }
        System.out.println("=============================");
    }
    
 // 장소만 변경 (행동은 하지 않음, UI용)
    public void changePlace(String placeName) {
        if (!placeMap.containsKey(placeName)) {
            System.out.println("❌ 존재하지 않는 장소입니다: " + placeName);
            return;
        }
        currentPlace = placeMap.get(placeName);
        System.out.println("📍 장소 이동(행동 없음): " + currentPlace.getName());
    }
    
    

}

