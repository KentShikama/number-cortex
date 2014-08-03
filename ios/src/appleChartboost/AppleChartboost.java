package appleChartboost;

import org.robovm.bindings.chartboost.Chartboost;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;

public class AppleChartboost implements CrossPlatformChartboost {

    private Chartboost chartboost;

    public void setChartBoost(Chartboost chartboost) {
        this.chartboost = chartboost;
    }

    @Override
    public void showMoreApps() {
        chartboost.showMoreApps();
    }

    @Override
    public void setListener(ChartBoostListener listener) {
        // No listener needed
    }
}
