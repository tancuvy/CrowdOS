package cn.crowdos.kernel.constraint.PrivacyConstraint;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class SimpleEncryptSpatioConstraint implements Constraint {
    private final EncryptCoordinate[] range;

    public SimpleEncryptSpatioConstraint(EncryptCoordinate topLeft, EncryptCoordinate bottomRight) throws InvalidConstraintException {
        this.range = new EncryptCoordinate[]{topLeft,bottomRight};
    }

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof EncryptCoordinate)) return false;
        EncryptCoordinate encryptCoordinate = (EncryptCoordinate) condition;
        Paillier paillier = new Paillier();
        BigInteger resTL = paillier.add(BigInteger.valueOf(range[0].encryptedlongitude),BigInteger.valueOf(range[0].encryptedlatitude));
        BigInteger resBR = paillier.add(BigInteger.valueOf(range[1].encryptedlongitude),BigInteger.valueOf(range[1].encryptedlatitude));
        BigInteger resParticipent = paillier.add(BigInteger.valueOf(encryptCoordinate.encryptedlongitude),BigInteger.valueOf(encryptCoordinate.encryptedlatitude));
        return resParticipent.compareTo(resTL) >0 && resParticipent.compareTo(resBR)<0;
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SimpleEncryptSpatioConstraint(range[0], range[1]));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) {
                //waring
                return this.trivialDecompose();
            }
        };
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
