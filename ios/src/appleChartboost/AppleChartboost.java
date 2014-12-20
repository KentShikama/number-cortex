package appleChartboost;

import org.robovm.bindings.chartboost.Chartboost;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;

public class AppleChartboost implements CrossPlatformChartboost {

    @Override
    public void showMoreApps() {
        Chartboost.showInterstitial("After Screen");
    }

    @Override
    public void setListener(ChartBoostListener listener) {
        // No listener needed
    }
}
