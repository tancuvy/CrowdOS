package cn.crowdos.kernel.constraint.PrivacyConstraint;

import cn.crowdos.kernel.constraint.Condition;

import java.math.BigInteger;

public class EncryptCoordinate implements Condition {
    final Integer encryptedlongitude;
    final Integer encryptedlatitude;


    public EncryptCoordinate(Integer encryptedlongitude, Integer encryptedlatitude) {
        this.encryptedlongitude = encryptedlongitude;
        this.encryptedlatitude = encryptedlatitude;
    }


    public EncryptCoordinate(double longitude,double latitude){
        Paillier paillier = new Paillier();
        this.encryptedlongitude = Integer.valueOf(String.valueOf(paillier.encrypt(longitude)));
        this.encryptedlatitude = Integer.valueOf(String.valueOf(paillier.encrypt(latitude)));
    }

    @Override
    public int hashCode() {
        return this.encryptedlatitude+this.encryptedlongitude;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj instanceof EncryptCoordinate){
            EncryptCoordinate anEnCoord = (EncryptCoordinate) obj;
            Paillier paillier = new Paillier();
            BigInteger result1 = paillier.add(BigInteger.valueOf(this.encryptedlongitude),BigInteger.valueOf(this.encryptedlatitude));
            BigInteger result2 = paillier.add(BigInteger.valueOf(anEnCoord.encryptedlongitude),BigInteger.valueOf(anEnCoord.encryptedlatitude));
            return result1.compareTo(result2)==0;
        }
        return false;
    }
}
