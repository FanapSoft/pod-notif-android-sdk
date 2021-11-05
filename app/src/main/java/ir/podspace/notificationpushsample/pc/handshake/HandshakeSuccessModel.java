package ir.podspace.notificationpushsample.pc.handshake;

public class HandshakeSuccessModel {
    private String keyId;
    private int expiresIn;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
