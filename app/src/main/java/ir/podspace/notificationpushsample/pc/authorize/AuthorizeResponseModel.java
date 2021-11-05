package ir.podspace.notificationpushsample.pc.authorize;

public class AuthorizeResponseModel {
    private int status;
    private String error;
    private String message;
    private String path;
    private String timestamp;
    private String reference;
    private AuthorizeSuccessModel result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public AuthorizeSuccessModel getResult() {
        return result;
    }

    public void setResult(AuthorizeSuccessModel result) {
        this.result = result;
    }
}
