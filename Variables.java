package calculator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Variables {
    private static final Map<String, BigInteger> variables = new HashMap<>();

    void setVariable(String name, BigInteger value) {
        variables.put(name, value);
    }

    void setVariable(String first, String second) {
        if (variables.containsKey(second)) {
            variables.put(first, variables.get(second));
        } else {
            System.out.println(new ErrorCode(3).getError());
        }
    }

    BigInteger getVariable(String name) {
        return variables.get(name);
    }

    boolean contains(String name) {
        return variables.containsKey(name);
    }
}
