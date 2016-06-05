package www.wearable.com.docnoteswear;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.zzd;
import com.google.gson.Gson;

import java.util.List;

import www.wearable.com.docnoteswear.model.PatientData;
import www.wearable.com.docnoteswear.preferences.PreferenceUtil;

public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "sync";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.d(TAG, "onDataChanged: changed");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for(DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();
            final String path = uri!=null ? uri.getPath() : null;
            if("/patient_event".equals(path)) {
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                // read your values from map:
                DataMap item = map.getDataMap("data");
                PatientData data = new PatientData().getFromDataMap(item);
                Log.d(TAG, "onDataChanged: "+ data.name);
                PreferenceUtil util = new PreferenceUtil(this);
                util.saveModel(data);
                notifyWear(data);

            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals("/patient_event")) {
            final String message = new String(messageEvent.getData());
            PatientData data = new Gson().fromJson(message , PatientData.class);
            PreferenceUtil util = new PreferenceUtil(this);
            util.saveModel(data);
           // notifyWear(data);
        }
    }

    public void notifyWear(PatientData data)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, GridViewPagerActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.docnotes_logo_notification)
                        .setContentTitle(data.name)
                        .setContentText("June 4 , 2016")
                        .setContentIntent(viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}