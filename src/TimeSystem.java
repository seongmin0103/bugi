
// package bugi;

public class TimeSystem {

    // ----------------------------------------------------
    // 1. 시간 / 날 / 학기 / 계절
    // ----------------------------------------------------
    private String[] times = {"아침", "낮", "밤"};
    private String[] seasons = {"봄", "여름", "가을", "겨울"};

    private int currentTimeIndex;  // 0=아침, 1=낮, 2=밤
    private int day;               // 1~8
    private int semester;          // 1~8학기 (4학년 2학기까지)
    private int seasonIndex;       // 봄=0, 여름=1, 가을=2, 겨울=3

    // ----------------------------------------------------
    // 2. 생성자 (초기값: 첫날 아침, 봄, 1학기)
    // ----------------------------------------------------
    public TimeSystem() {
        currentTimeIndex = 0;  // 아침
        day = 1;               // 첫째 날
        semester = 1;          // 1학기 시작
        seasonIndex = 0;       // 봄
    }

    // ----------------------------------------------------
    // 3. 시간 진행 (행동 1번 = 시간 1칸 진행)
    // ----------------------------------------------------
    public void nextTime() {

        currentTimeIndex++;

        // 하루 끝났을 때
        if (currentTimeIndex >= times.length) {
            currentTimeIndex = 0;
            nextDay();
        }

        System.out.println("⏰ 현재 시간: " + getCurrentTime());
    }

    // ----------------------------------------------------
    // 4. 다음 날로 이동
    // ----------------------------------------------------
    private void nextDay() {
        day++;

        System.out.println("📅 새로운 하루가 시작되었습니다! Day " + day);

        // 8일 = 1학기 종료
        if (day > 8) {
            nextSemester();
            day = 1;
        }
    }

    // ----------------------------------------------------
    // 5. 학기 진행
    // ----------------------------------------------------
    private void nextSemester() {
        semester++;
        System.out.println("🎓 새로운 학기가 시작되었습니다! Semester " + semester);

        // 학기 변경 시 계절도 변경
        seasonIndex = (seasonIndex + 1) % seasons.length;

        System.out.println("🍃 계절이 바뀌었습니다! 현재 계절: " + getCurrentSeason());
    }

    // ----------------------------------------------------
    // 6. Getter
    // ----------------------------------------------------
    public String getCurrentTime() {
        return times[currentTimeIndex];
    }

    public int getDay() {
        return day;
    }

    public int getSemester() {
        return semester;
    }

    public String getCurrentSeason() {
        return seasons[seasonIndex];
    }

    // ----------------------------------------------------
    // 7. 디버그용 출력
    // ----------------------------------------------------
    public void printStatus() {
        System.out.println("===== Time System =====");
        System.out.println("시간대 : " + getCurrentTime());
        System.out.println("Day : " + day);
        System.out.println("Semester : " + semester);
        System.out.println("Season : " + getCurrentSeason());
        System.out.println("=========================");
    }
}

