package calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                String input = sc.nextLine();
                switch (input) {
                    case "/exit":
                        System.out.println("Bye!");
                        return;
                    case "/help":
                        System.out.println("Calculator app");
                        break;
                    default:
                        if (!input.isBlank()) {
                            InputParser parser = new InputParser(input);
                            parser.run();
                            break;
                        }
                }
            } catch (NullPointerException ignored) {
            }
        }
    }
}
