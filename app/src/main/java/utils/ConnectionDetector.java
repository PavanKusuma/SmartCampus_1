package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Pavan_Kusuma on 4/29/2015.
 */
public class ConnectionDetector {

    // context
    Context context;

    public ConnectionDetector(Context context){
        this.context = context;
    }

    public boolean isInternetWorking(){

        // get connectivity system service
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check if it is not null
        if(connectivityManager != null){

            // get network info
            /*NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if(networkInfo != null){

                for(int i=0; i<networkInfo.length; i++){

                    if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED){

                        return true;
                    }
                }
            }*/


            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null){

                if (networkInfo.getState() == NetworkInfo.State.CONNECTED){

                    return true;
                }

                if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED){

                    return false;
                }
            }
        }

        return false;
    }
}
