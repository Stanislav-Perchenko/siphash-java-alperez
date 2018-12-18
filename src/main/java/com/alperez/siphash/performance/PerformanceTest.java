package com.alperez.siphash.performance;

import com.alperez.siphash.SipHash;
import com.alperez.siphash.SipHashKey;

import java.util.Random;

public class PerformanceTest {
    public static final int TIMES_PER_ROUND = 1000000;
    public static final int AVG_ROUNDS = 10;

    public static void main(String[] args) {
        new PerformanceTest().doTest();
    }

    private final Random rnd;
    private final SipHashKey key;

    private PerformanceTest() {
        rnd = new Random();
        key = SipHashKey.ofBytes(buildRandomByteArray(16));
    }

    public void doTest() {
        for (int i = 32; i<=2048; i *= 2) {
            System.out.println(String.format("SipHash of %d bytes message * %d times:", i, TIMES_PER_ROUND));
            double dt = testIteration(buildRandomByteArray(i));
            System.out.println(String.format("Avg. round time = %.3f us\n\n", dt));
        }
    }

    private byte[] buildRandomByteArray(final int lenght) {
        byte msg[] = new byte[lenght];
        rnd.nextBytes(msg);
        return msg;
    }

    /**
     * Executest the 'testRound' AVG_ROUNDS time and calculates average time
     * @param msg
     * @return average time in microseconds
     */
    private double testIteration(byte[] msg) {
        long accum = 0;
        for (int i=0; i<AVG_ROUNDS; i++) {
            long dt = testRound(msg);
            accum += dt;
            System.out.println("     round time = "+dt);
        }
        return accum / (AVG_ROUNDS * 1000.0);
    }

    /**
     * Calculates has of 'msg' TIMES_PER_ROUND times and returns time spent
     * @param msg
     * @return time spent in nanoseconds
     */
    private long testRound(byte[] msg) {
        long tStart = System.nanoTime();
        for (int i=0; i<TIMES_PER_ROUND; i++) SipHash.calculateHash(key, msg);
        return System.nanoTime() - tStart;
    }

}
