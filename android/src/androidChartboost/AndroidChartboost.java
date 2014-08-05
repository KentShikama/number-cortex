package androidChartboost;

import android.app.Activity;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Chartboost.CBAgeGateConfirmation;
import com.chartboost.sdk.Model.CBError.CBClickError;
import com.chartboost.sdk.Model.CBError.CBImpressionError;

public class AndroidChartboost implements CrossPlatformChartboost {

    private Chartboost cb;
    private ChartBoostListener listener;
    private Activity activity;

    public AndroidChartboost(Chartboost cb, Activity activity) {
        this.cb = cb;
        this.activity = activity;
    }

    @Override
    public void setListener(ChartBoostListener listener) {
        this.listener = listener;
    }

    @Override
    public void showMoreApps() {
        this.listener.showMoreApps();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cb.showMoreApps();
            }
        });
    }

    public ChartboostDelegate getChartBoostDelegate() {
        return chartBoostDelegate;
    }

    private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() {
        @Override
        public void didDismissMoreApps() {
            listener.didDismissMoreApps();
        }
        @Override
        public void didFailToLoadMoreApps(CBImpressionError error) {
            String errorMessage;
            switch (error) {
                case INTERNET_UNAVAILABLE:
                    errorMessage = "Your device is not connected to the internet. Please recheck your internet preferences.";
                    break;
                case NETWORK_FAILURE:
                    errorMessage = "There was an error communicating with the server. Please retry at a later time.";
                    break;
                case NO_AD_FOUND:
                    errorMessage = "There are currently no recommendations available for you. Please retry at a later time.";
                    break;
                default:
                    errorMessage = "Game recommendations are currently unavailable. Please retry at a later time.";
                    break;
            }
            listener.didFailToLoadMoreApps(errorMessage);
        }
        @Override
        public boolean shouldDisplayLoadingViewForMoreApps() {
            return false;
        }

        /**
         * Unused delegate methods more apps
         */
        @Override
        public boolean shouldRequestMoreApps() {
            return true;
        }
        @Override
        public boolean shouldDisplayMoreApps() {
            return true;
        }
        @Override
        public void didCacheMoreApps() {}
        @Override
        public void didCloseMoreApps() {}
        @Override
        public void didClickMoreApps() {}
        @Override
        public void didShowMoreApps() {}

        /**
         * Unused delegate methods interstitials
         */
        @Override
        public boolean shouldRequestInterstitialsInFirstSession() {
            return true;
        }
        @Override
        public boolean shouldDisplayInterstitial(String location) {
            return true;
        }
        @Override
        public boolean shouldRequestInterstitial(String location) {
            return true;
        }
        @Override
        public void didCacheInterstitial(String location) {}
        @Override
        public void didDismissInterstitial(String location) {}
        @Override
        public void didCloseInterstitial(String location) {}
        @Override
        public void didClickInterstitial(String location) {}
        @Override
        public void didShowInterstitial(String location) {}
        @Override
        public void didFailToRecordClick(String arg0, CBClickError arg1) {}
        @Override
        public boolean shouldPauseClickForConfirmation(CBAgeGateConfirmation arg0) {
            return false;
        }
        @Override
        public void didFailToLoadInterstitial(String arg0, CBImpressionError arg1) {}
    };

}
