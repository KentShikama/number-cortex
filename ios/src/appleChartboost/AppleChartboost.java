package appleChartboost;

import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegate;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;

public class AppleChartboost implements CrossPlatformChartboost {

    private ChartBoostListener listener;
    private Chartboost cb;

    public AppleChartboost(Chartboost cb) {
        this.cb = cb;
        cb.setDelegate(new ChartboostDelegate() {
            @Override
            public void didDismissMoreApps() {
                listener.didDismissMoreApps();
            }
            @Override
            public void didFailToLoadMoreApps() {
                String errorMessage = "Game recommendations are currently unavailable. Please retry at a later time.";
                listener.didFailToLoadMoreApps(errorMessage);        
            }
            @Override
            public void didCacheInterstitial(String arg0) {}
            @Override
            public void didCacheMoreApps() {}
            @Override
            public void didClickInterstitial(String arg0) {}
            @Override
            public void didClickMoreApps() {}
            @Override
            public void didCloseInterstitial(String arg0) {}
            @Override
            public void didCloseMoreApps() {}
            @Override
            public void didDismissInterstitial(String arg0) {}
            @Override
            public void didFailToLoadInterstitial(String arg0) {}
            @Override
            public boolean shouldDisplayInterstitial(String arg0) {
                return false;
            }
            @Override
            public boolean shouldDisplayLoadingViewForMoreApps() {
                return false;
            }
            @Override
            public boolean shouldDisplayMoreApps() {
                return true;
            }
            @Override
            public boolean shouldRequestInterstitial(String arg0) {
                return false;
            }
            @Override
            public boolean shouldRequestInterstitialsInFirstSession() {
                return true;
            }
        });
    }
    
    @Override
    public void showMoreApps() {
        cb.showMoreApps();
    }

    @Override
    public Object getChartBoostDelegate() {
        return null;
    }

    @Override
    public void setListener(ChartBoostListener listener) {
        this.listener = listener;
    }
}
