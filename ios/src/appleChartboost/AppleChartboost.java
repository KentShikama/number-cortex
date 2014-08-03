package appleChartboost;

import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegateAdapter;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;

public class AppleChartboost implements CrossPlatformChartboost{

    private ChartBoostListener listener;
    private Chartboost cb;

    public AppleChartboost(Chartboost cb) {
        this.cb = cb;
        cb.setDelegate(new ChartboostDelegateAdapter() {
            @Override
            public void didDismissMoreApps() {
                listener.didDismissMoreApps();
            }
            @Override
            public void didFailToLoadMoreApps() {
                String errorMessage = "Game recommendations are currently unavailable. Please retry at a later time.";
                listener.didFailToLoadMoreApps(errorMessage);        
            }
        });
    }
    
    @Override
    public void showMoreApps() {
        System.out.println("More apps about to be shown");
        listener.showMoreApps();
        cb.showMoreApps();
        System.out.println("More apps shown");
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
