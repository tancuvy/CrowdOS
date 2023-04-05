package cn.crowdos.kernel.constraint.PrivacyConstraint;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Paillier {
    private BigInteger p, q, lambda;
    public BigInteger n, nsquare, g;
    private SecureRandom random;

    public Paillier() {
        random = new SecureRandom();
        p = new BigInteger(1024, 64, random);
        q = new BigInteger(1024, 64, random);
        n = p.multiply(q);
        nsquare = n.multiply(n);
        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
    }

    public BigInteger encrypt(double coordinate) {
        // 转换为整数类型
        BigInteger m = BigInteger.valueOf((long) (coordinate * 1000000));
        BigInteger r = new BigInteger(512, random);
        BigInteger c = g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
        return c;
    }

    public BigInteger multiply(BigInteger a, int k) {
        return a.modPow(BigInteger.valueOf(k), nsquare);
    }

    public BigInteger add(BigInteger a, BigInteger b) {
        return a.multiply(b).mod(nsquare);
    }

    public BigInteger decrypt(BigInteger c) {
        BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        BigInteger m = c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
        return m;
    }
}
