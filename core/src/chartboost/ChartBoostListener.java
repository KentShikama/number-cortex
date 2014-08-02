package chartboost;

public interface ChartBoostListener {
    public void showMoreApps();
    public void didDismissMoreApps();
    public void didFailToLoadMoreApps(String errorMessage);
}
