package com.numbercortex.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.view.Launch;
import com.chartboost.sdk.*;
import com.chartboost.sdk.Chartboost.CBAgeGateConfirmation;
import com.chartboost.sdk.Model.CBError.CBClickError;
import com.chartboost.sdk.Model.CBError.CBImpressionError;

public class AndroidLauncher extends AndroidApplication {

    private Chartboost cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cb = Chartboost.sharedChartboost();
        String appId = "53db59161873da6e205eaf25";
        String appSignature = "2423da041b609b5625b427b94f514ecc10a515b1";
        CBPreferences.getInstance().setImpressionsUseActivities(true);
        this.cb.onCreate(this, appId, appSignature, this.chartBoostDelegate);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Launch(), config);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.cb.onStart(this);
        this.cb.showInterstitial();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.cb.onStop(this);
    }

    @Override
    protected void onDestroy() {
        this.cb.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it. Otherwise continue as normal.
        if (this.cb.onBackPressed())
            return;
        else
            super.onBackPressed();
    }

    private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() {

        /*
         * shouldDisplayInterstitial(String location)
         * 
         * This is used to control when an interstitial should or should not be displayed If you should not display an interstitial, return FALSE
         * 
         * For example: during gameplay, return FALSE.
         * 
         * Is fired on: - showInterstitial() - Interstitial is loaded & ready to display
         */
        @Override
        public boolean shouldDisplayInterstitial(String location) {
            return true;
        }

        /*
         * shouldRequestInterstitial(String location)
         * 
         * This is used to control when an interstitial should or should not be requested If you should not request an interstitial from the server, return FALSE
         * 
         * For example: user should not see interstitials for some reason, return FALSE.
         * 
         * Is fired on: - cacheInterstitial() - showInterstitial() if no interstitial is cached
         * 
         * Notes: - We do not recommend excluding purchasers with this delegate method - Instead, use an exclusion list on your campaign so you can control it on the fly
         */
        @Override
        public boolean shouldRequestInterstitial(String location) {
            return true;
        }

        /*
         * didCacheInterstitial(String location)
         * 
         * Passes in the location name that has successfully been cached
         * 
         * Is fired on: - cacheInterstitial() success - All assets are loaded
         * 
         * Notes: - Similar to this is: cb.hasCachedInterstitial(String location) Which will return true if a cached interstitial exists for that location
         */
        @Override
        public void didCacheInterstitial(String location) {
            // Save which location is ready to display immediately
        }

        /*
         * didDismissInterstitial(String location)
         * 
         * This is called when an interstitial is dismissed
         * 
         * Is fired on: - Interstitial click - Interstitial close
         * 
         * #Pro Tip: Use the code below to immediately re-cache interstitials
         */
        @Override
        public void didDismissInterstitial(String location) {
            // Immediately re-caches an interstitial
            cb.cacheInterstitial(location);
        }

        /*
         * didCloseInterstitial(String location)
         * 
         * This is called when an interstitial is closed
         * 
         * Is fired on: - Interstitial close
         */
        @Override
        public void didCloseInterstitial(String location) {
            // Know that the user has closed the interstitial
        }

        /*
         * didClickInterstitial(String location)
         * 
         * This is called when an interstitial is clicked
         * 
         * Is fired on: - Interstitial click
         */
        @Override
        public void didClickInterstitial(String location) {
            System.out.println("Clicked: " + location.toString());
            // Know that the user has clicked the interstitial
        }

        /*
         * didShowInterstitial(String location)
         * 
         * This is called when an interstitial has been successfully shown
         * 
         * Is fired on: - showInterstitial() success
         */
        @Override
        public void didShowInterstitial(String location) {
            // Know that the user has seen the interstitial
        }

        /*
         * MoreApps delegate methods
         */

        /*
         * shouldDisplayLoadingViewForMoreApps()
         * 
         * Return FALSE to prevent the pretty MoreApps loading screen
         * 
         * Is fired on: - showMoreApps()
         */
        @Override
        public boolean shouldDisplayLoadingViewForMoreApps() {
            return true;
        }

        /*
         * shouldRequestMoreApps()
         * 
         * Return FALSE to prevent a MoreApps page request
         * 
         * Is fired on: - cacheMoreApps() - showMoreApps() if no MoreApps page is cached
         */
        @Override
        public boolean shouldRequestMoreApps() {
            return true;
        }

        /*
         * shouldDisplayMoreApps()
         * 
         * Return FALSE to prevent the MoreApps page from displaying
         * 
         * Is fired on: - showMoreApps() - MoreApps page is loaded and ready to display
         */
        @Override
        public boolean shouldDisplayMoreApps() {
            return true;
        }

        /*
         * didCacheMoreApps()
         * 
         * Is fired on: - cacheMoreApps() success - All assets are loaded
         */
        @Override
        public void didCacheMoreApps() {
            // Know that the MoreApps page is cached and ready to display
        }

        /*
         * didDismissMoreApps()
         * 
         * This is called when the MoreApps page is dismissed
         * 
         * Is fired on: - MoreApps click - MoreApps close
         */
        @Override
        public void didDismissMoreApps() {
            // Know that the MoreApps page has been dismissed
        }

        /*
         * didCloseMoreApps()
         * 
         * This is called when the MoreApps page is closed
         * 
         * Is fired on: - MoreApps close
         */
        @Override
        public void didCloseMoreApps() {
            // Know that the MoreApps page has been closed
        }

        /*
         * didClickMoreApps()
         * 
         * This is called when the MoreApps page is clicked
         * 
         * Is fired on: - MoreApps click
         */
        @Override
        public void didClickMoreApps() {
            // Know that the MoreApps page has been clicked

        }

        /*
         * didShowMoreApps()
         * 
         * This is called when the MoreApps page has been successfully shown
         * 
         * Is fired on: - showMoreApps() success
         */
        @Override
        public void didShowMoreApps() {
            // Know that the MoreApps page has been presented on the screen
        }

        /*
         * shouldRequestInterstitialsInFirstSession()
         * 
         * Return FALSE if the user should not request interstitials until the 2nd startSession()
         */
        @Override
        public boolean shouldRequestInterstitialsInFirstSession() {
            return true;
        }

        @Override
        public void didFailToLoadInterstitial(String arg0, CBImpressionError arg1) {
            System.out.println("Warning!!!!!: " + arg0.toString());
            System.out.println("Warning!!!!!: " + arg1.toString());

        }

        @Override
        public void didFailToLoadMoreApps(CBImpressionError arg0) {
            System.out.println("Warning!!!!!: " + arg0.toString());
        }

        @Override
        public void didFailToRecordClick(String arg0, CBClickError arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean shouldPauseClickForConfirmation(CBAgeGateConfirmation arg0) {
            // TODO Auto-generated method stub
            return false;
        }
    };

}
