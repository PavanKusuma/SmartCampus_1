package application;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.parse.Parse;

import utils.Constants;

/**
 * Created by Pavan on 4/14/15.
 */
public class App extends MultiDexApplication{
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable local Datastore
        Parse.enableLocalDatastore(this);

        // initialize the Parse database application
        Parse.initialize(this, Constants.parseApplicationId, Constants.parseClientKey);
    }
}
