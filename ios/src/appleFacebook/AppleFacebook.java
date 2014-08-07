package appleFacebook;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSURL;
import org.robovm.bindings.facebook.FBAppCall;
import org.robovm.bindings.facebook.dialogs.FBDialogAppCallCompletionHandler;
import org.robovm.bindings.facebook.dialogs.FBDialogs;
import org.robovm.bindings.facebook.dialogs.FBShareDialogParams;
import facebook.CrossPlatformFacebook;
import facebook.FacebookCallbackListener;

public class AppleFacebook implements CrossPlatformFacebook {

    private FacebookCallbackListener listener;

    @Override
    public void setListener(FacebookCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void post(String title, String description) {
        final String link = "http://www.numbercortex.com";
        final String pictureLink = "http://www.numbercortex.com/images/number_cortex_mobile_banner.jpg";
        FBShareDialogParams params = new FBShareDialogParams();
        params.setName(title);
        params.setCaption(description);
        params.setPicture(new NSURL(pictureLink));
        params.setLink(new NSURL(link));
        FBDialogAppCallCompletionHandler handler = new FBDialogAppCallCompletionHandler() {
            @Override
            public void invoke(FBAppCall call, NSDictionary<?, ?> results, NSError error) {
                if (error != null) {
                    String errorMessage = String.format("We were unable to connect to Facebook: " + error.getLocalizedDescription());
                    listener.showErrorDialog(errorMessage);
                }
            }
        };
        FBDialogs.presentShareDialog(params, null, handler);    
    }
}
