// package bugi;

public class CoinManager {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private int balance; // 코인 잔액

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public CoinManager() {
        this.balance = 30;  // 게임 시작 시 기본 금액 (부모님이 챙겨준 돈)
    }

    public CoinManager(int initialCoins) {
        this.balance = initialCoins;
    }

    // ----------------------------------------------------
    // 3. 코인 증가
    // ----------------------------------------------------
    public void earn(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("코인 +" + amount + " 획득! 현재 코인: " + balance);
        }
    }

    // ----------------------------------------------------
    // 4. 코인 감소
    // ----------------------------------------------------
    public boolean spend(int amount) {
        if (amount <= 0) {
            return false;
        }

        if (balance >= amount) {
            balance -= amount;
            System.out.println("코인 -" + amount + " 사용! 현재 코인: " + balance);
            return true;
        } else {
            System.out.println("코인이 부족합니다! 현재 코인: " + balance);
            return false;
        }
    }

    // ----------------------------------------------------
    // 5. 구매 가능한지 체크
    // ----------------------------------------------------
    public boolean canAfford(int amount) {
        return balance >= amount;
    }

    // ----------------------------------------------------
    // 6. Getter
    // ----------------------------------------------------
    public int getBalance() {
        return balance;
    }

    // ----------------------------------------------------
    // 7. toString()
    // ----------------------------------------------------
    @Override
    public String toString() {
        return "CoinManager{balance=" + balance + "}";
    }
}

