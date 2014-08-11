package iap;

public interface IAPListener {
    public void success();
    public void failure(String errorMessage);
}
