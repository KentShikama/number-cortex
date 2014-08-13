package appleIAP;

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.storekit.SKPaymentTransaction;
import org.robovm.apple.storekit.SKProduct;
import org.robovm.apple.storekit.SKRequest;
import org.robovm.bindings.inapppurchase.InAppPurchaseListener;
import org.robovm.bindings.inapppurchase.InAppPurchaseManager;
import iap.CrossPlatformIAP;
import iap.IAPListener;

public class AppleIAP implements CrossPlatformIAP {

    private IAPListener listener;
    
    private InAppPurchaseManager iapManager;
    
    protected static final String TWO_PLAYER_MODE = "two_player_mode";

    public AppleIAP() {
        iapManager = new InAppPurchaseManager(new InAppPurchaseListener() {
            @Override
            public void productsReceived(SKProduct[] products) {
                SKProduct twoPlayerUnlockProduct = products[0];
                if (iapManager.canMakePayments()) {
                    iapManager.purchaseProduct(twoPlayerUnlockProduct);
                }
            }
            @Override
            public void transactionCompleted(SKPaymentTransaction transaction) {
                String productId = transaction.getPayment().getProductIdentifier().toString();
                if (productId.equals(TWO_PLAYER_MODE)) {
                    listener.success();
                }
            }
            @Override
            public void transactionRestored(SKPaymentTransaction transaction) {
                String productId = transaction.getPayment().getProductIdentifier().toString();
                if (productId.equals(TWO_PLAYER_MODE)) {
                    listener.success();
                }
            }
            
            @Override
            public void productsRequestFailed(SKRequest request, NSError error) {
                listener.failure(error.getLocalizedDescription());
            }
            @Override
            public void transactionFailed(SKPaymentTransaction transaction, NSError error) {
                listener.failure(error.getLocalizedDescription());
            }
            @Override
            public void transactionRestoreFailed(NSArray<SKPaymentTransaction> transactions, NSError error) {
                listener.failure(error.getLocalizedDescription());
            }
        });
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
        iapManager.requestProducts(TWO_PLAYER_MODE);
    }
    @Override
    public void restore() {
        iapManager.restoreTransactions();
    }

}
