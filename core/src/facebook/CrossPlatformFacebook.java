package facebook;

public interface CrossPlatformFacebook {
    public void setListener(FacebookCallbackListener listener);
    public void post(String title, String description);
}
