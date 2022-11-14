package lotto;

/*
3개 일치 (5,000원) - 1개
4개 일치 (50,000원) - 0개
5개 일치 (1,500,000원) - 0개
5개 일치, 보너스 볼 일치 (30,000,000원) - 0개
6개 일치 (2,000,000,000원) - 0개
 */

import java.util.HashMap;
import java.util.List;

public class Compute {
    private final HashMap<String, Integer> winRecords;
    private enum WinType {
        MATCH3(3,"match3", 5000, "3개 일치 (5,000원) - $match3개"),
        MATCH4(4, "match4", 50000, "4개 일치 (50,000원) - $match4개"),
        MATCH5(5, "match5", 1500000, "5개 일치 (1,500,000원) - $match5개"),
        // 당첨 번호 5개 일치 시, MATCH5_PLUS_BONUS와 MATCH5의 값이 겹치므로 MATCH5_PLUS_BONUS의 value를 사용하지 않는다.
        MATCH5_PLUS_BONUS(-1, "match5PlusBonus", 30000000, "5개 일치, 보너스 볼 일치 (30,000,000원) - $match5PlusBonus개"),
        MATCH6(6, "match6", 2000000000, "6개 일치 (2,000,000,000원) - $match6개");

        private final int winTypeValue;
        private final String winTypeKey;
        private final float prizeMoney;
        private final String winTypeMessage;

        WinType(int winTypeValue, String winTypeKey, float prizeMoney, String winTypeMessage) {
            this.winTypeValue = winTypeValue;
            this.winTypeKey = winTypeKey;
            this.prizeMoney = prizeMoney;
            this.winTypeMessage = winTypeMessage;
        }
    }

    public Compute(Lottos lottos, WinningNumbers winningNumbers, Money money) {
        HashMap<String, Integer> winRecords = initializeWinRecords();

        for (Lotto lotto : lottos.getLottos()) {
            winRecords = computeWinRecords(
                    winRecords,
                    lotto.getNumbers(),
                    winningNumbers.getWinningNumbers(),
                    winningNumbers.getBonusNumber()
            );
        }
        this.winRecords = winRecords;

        printWinRecords(winRecords);

        float profit = computeProfit(money.getMoney(), winRecords);
        printProfit(profit);
    }
    private HashMap<String, Integer> initializeWinRecords() {
        HashMap<String, Integer> winRecords = new HashMap<>();

        for (WinType winType : WinType.values()) {
            winRecords.put(winType.winTypeKey, 0);
        }
        return winRecords;
    }

    private HashMap<String, Integer> computeWinRecords(
            HashMap<String, Integer> winRecords,
            List<Integer> lotto,
            List<Integer> winningNumbers,
            int bonusNumber
    ) {
        boolean containsBonusNumber = lotto.contains(bonusNumber);

        List<Integer> matchingNumbers = lotto;
        matchingNumbers.retainAll(winningNumbers);
        int count = matchingNumbers.size();

        for (WinType winType : WinType.values()) {
            if (containsBonusNumber && count == WinType.MATCH5.winTypeValue) {
                String key = WinType.MATCH5_PLUS_BONUS.winTypeKey;
                winRecords.put(key, winRecords.get(key) + 1);
            }
            else if (count == winType.winTypeValue) {
                String key = winType.winTypeKey;
                winRecords.put(key, winRecords.get(key) + 1);
            }
        }

        return winRecords;
    }

    private void printWinRecords(HashMap<String, Integer> winRecords) {
        final String TITLE = "당첨 통계";
        final String DIVISION_LINE = "---";

        System.out.println(TITLE);
        System.out.println(DIVISION_LINE);

        for (WinType winType : WinType.values()) {
            String winMessage = winType.winTypeMessage;
            int winNumber = winRecords.get(winType.winTypeKey);
            String replaceFrom = "$";
            replaceFrom = replaceFrom.concat(winType.winTypeKey);
            winMessage = winMessage.replace(replaceFrom, Integer.toString(winNumber));

            System.out.println(winMessage);
        }
    }

    private float computeProfit(int money, HashMap<String, Integer> winRecords) {
        float profit = 0;

        for (WinType winType : WinType.values()) {
            profit += winRecords.get(winType.winTypeKey) * winType.prizeMoney;
        }

        profit = (float) money / profit;

        return profit;
    }

    private void printProfit(float profit) {
        String text = "총 수익률은 $profit%입니다.";
        String replaceFrom = "$profit";
        String rateOfProfit = String.format("%.1f", profit);
        text = text.replace(replaceFrom, rateOfProfit);

        System.out.println(text);
    }
}

