package appleChartboost;

import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegate;
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
                if (listener == null) {
                    System.out.println("Null pointer at line 20");
                }
                listener.didDismissMoreApps();
            }
            @Override
            public void didFailToLoadMoreApps() {
                String errorMessage = "Game recommendations are currently unavailable. Please retry at a later time.";
                if (listener == null) {
                    System.out.println("Null pointer at line 28 and error: " + errorMessage);
                }
                listener.didFailToLoadMoreApps(errorMessage);        
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
