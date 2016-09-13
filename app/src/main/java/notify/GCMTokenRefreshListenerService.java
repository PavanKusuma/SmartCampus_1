package notify;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Pavan_Kusuma on 8/21/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService{

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
