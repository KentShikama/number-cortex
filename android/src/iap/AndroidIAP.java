package iap;

import com.numbercortex.android.AndroidLauncher;
import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;

public class AndroidIAP implements CrossPlatformIAP {

    protected static final String TWO_PLAYER_MODE = "two_player_mode";

    private IAPListener listener;
    private IabHelper iabHelper;
    private AndroidLauncher launcher;

    public AndroidIAP(AndroidLauncher launcher, IabHelper iabHelper) {
        this.launcher = launcher;
        this.iabHelper = iabHelper;
    }

    protected boolean verifyDeveloperPayload(Purchase premiumPurchase) {
        String payload = premiumPurchase.getDeveloperPayload();
        if (payload.equals("numbercortex")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPurchaseListener(IAPListener listener) {
        this.listener = listener;
    }
    @Override
    public void setRestoreListener(IAPListener listener) {
        this.listener = listener;
    }

    @Override
    public void purchase() {
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                        if (result.isFailure()) {
                            String errorMessage = "Error purchasing: " + result;
                            listener.failure(errorMessage);
                        } else if (purchase.getSku().equals(TWO_PLAYER_MODE)) {
                            listener.success();
                        } else {
                            System.out.println("Unknown purchase: " + result);
                        }
                    }
                };
                iabHelper.launchPurchaseFlow(launcher, TWO_PLAYER_MODE, 10001, mPurchaseFinishedListener, "numbercortex");
            }
        });
    }
    @Override
    public void restore() {
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        if (result.isFailure()) {
                            String errorMessage = "Problem setting up In-app Billing: " + result;
                            listener.failure(errorMessage);
                            return;
                        }
                        Purchase premiumPurchase = inventory.getPurchase(TWO_PLAYER_MODE);
                        boolean isTwoPlayerModeUnlocked = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
                        if (isTwoPlayerModeUnlocked) {
                            listener.success();
                        } else {
                            listener.failure("No records for the premium upgrade was found. If you believe you have purchased the upgrade, please email support@numbercortex.com.");
                        }
                    }
                };
                iabHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

}
