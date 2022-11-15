package lotto;

public class Application {
    private static Money purchaseAmount() {
        Output.purchaseEventMessage();
        Money money = new Money(Input.input());
        Output.blankLine();

        return money;
    }

    private static NumberOfLotto buyLotto(Money money) {
        NumberOfLotto numberOfLotto = new NumberOfLotto(money);
        Output.numberOfPurchaseEventMessage(numberOfLotto.getNumberOfLotto());

        return numberOfLotto;
    }

    private static Lottos issueLotto(NumberOfLotto numberOfLotto) {
        Lottos lottos = new Lottos(numberOfLotto);
        Output.issuedLottosEventMessage(lottos.getLottos());
        Output.blankLine();

        return lottos;
    }
    public static void main(String[] args) {
        try {
            Money money = purchaseAmount();
            NumberOfLotto numberOfLotto = buyLotto(money);
            Lottos lottos = issueLotto(numberOfLotto);

            Output.winNumberEventMessage();
            String winNumbers = Input.input();
            Output.blankLine();
            Output.bonusNumberEventMessage();
            String bonusNumber = Input.input();
            WinningNumbers winningNumbers = new WinningNumbers(winNumbers, bonusNumber);
            Output.blankLine();

            Compute compute = new Compute(lottos, winningNumbers, money);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
