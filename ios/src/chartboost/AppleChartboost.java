package chartboost;

import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegate;
import org.robovm.objc.annotation.Method;

public class AppleChartboost implements CrossPlatformChartboost {

    private ChartBoostListener listener;
    private Chartboost cb;

    public AppleChartboost(Chartboost cb) {
        this.cb = cb;
    }
    
    @Override
    public void showMoreApps() {
        cb.showMoreApps();
    }

    @Override
    public Object getChartBoostDelegate() {
        return chartBoostDelegate;
    }

    @Override
    public void setListener(ChartBoostListener listener) {
        this.listener = listener;
    }

    private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() {
        @Override
        public void didDismissMoreApps() {
            listener.didDismissMoreApps();
        }
        @Override
        @Method(selector = "didFailToLoadMoreApps")
        public void didFailToLoadMoreApps() {            
            String errorMessage = "Game recommendations are currently unavailable. Please retry at a later time.";
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
        public boolean shouldDisplayMoreApps() {
            return true;
        }
        @Override
        public void didCacheMoreApps() {}
        @Override
        public void didCloseMoreApps() {}
        @Override
        public void didClickMoreApps() {}

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
        @Method(selector = "didFailToLoadInterstitial:")
        public void didFailToLoadInterstitial(String arg0) {
            // TODO Auto-generated method stub
            
        }
    };
}
