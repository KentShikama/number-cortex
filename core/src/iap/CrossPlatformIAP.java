package iap;

public interface CrossPlatformIAP {
    public void setPurchaseListener(IAPListener listener);
    public void purchase();
    public void setRestoreListener(IAPListener listener);
    public void restore();
}
