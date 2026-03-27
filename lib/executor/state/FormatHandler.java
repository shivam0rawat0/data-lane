package lib.executor.state;

import java.util.Arrays;

import lib.db.Retry;
import lib.db.Status;

public class FormatHandler implements StateHandler {
    @Override
    public void handle(Retry retry) {
        System.out.printf("Handling format for retry: id=%d, uid=%d, data=%s\n", retry.getId(), retry.getUid(), retry.getData());
        String primed = Arrays.stream(retry.getData().split(","))
            .map(x -> findNthPrime(Integer.parseInt(x)))
            .map(x -> String.valueOf(x))
            .reduce((a, b) -> String.valueOf(a) + "," + String.valueOf(b))
            .orElse("");
        retry.setData(primed);
        retry.setStatus(Status.CLASSIFY);
    }

    private int findNthPrime(int n) {
        int count = 0;
        int num = 1;
        while (count < n) {
            num++;
            if (isPrime(num)) {
                count++;
            }
        }
        return num;
    }
    
    private boolean isPrime(int next) {
        if (next <= 1) return false;
        for (int i = 2; i <= Math.sqrt(next); i++) {
            if (next % i == 0) return false;
        }
        return true;
    }
}
