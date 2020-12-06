package com.sibk.tasik.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.sibk.tasik.Api.NotifikasiApi;
import com.sibk.tasik.DB.DBUser;
import com.sibk.tasik.MainActivityGuru.MainActivityGuru;
import com.sibk.tasik.MainActivitySiswa.MainActivity;
import com.sibk.tasik.Model.User;
import com.sibk.tasik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.prilude.tms.driver.service.action.FOO";
    private static final String ACTION_BAZ = "com.prilude.tms.driver.service.action.BAZ";
    private String CHANNEL_ID = "";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.prilude.tms.driver.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.prilude.tms.driver.service.extra.PARAM2";

    // constant
    public static final long NOTIFY_INTERVAL = 5 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private Intent intent;
    //untuk SSL

    private AudioManager mgr;

    public NotificationService() {
        super("Notification");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        mgr = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        super.onCreate();
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    findNotification();
                }
            });
        }

    }

    public static int isi;

    private void findNotification() {
        DBUser dbUser = new DBUser(this);
        Log.d("masukNotif", "Masuk");
        if (!dbUser.isNull()) {
            User user = dbUser.findUser();
            final int idPengguna = user.getuserid();
            AndroidNetworking.post(NotifikasiApi.POST_FIND_INTERVAL)
                    .addBodyParameter("id_pengguna", String.valueOf(idPengguna))
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                        }
                    }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Response okHttpResponse, JSONObject response) {
                    try {
                        JSONObject result = response.getJSONObject("prilude");
                        String status = result.getString("status");
                        String message = result.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray data = result.getJSONArray("data");
                            isi = 0;
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String title = c.getString("judul");
                                String message2 = c.getString("pesan");
                                int usertypeid = c.getInt("usertypeid");
                                int idNotifikasi = c.getInt("id_notifikasi");
                                int isRead = c.getInt("is_read");
                                String timestamp = c.getString("timestamp");
                                if (isRead == 0) {
                                    isi = isi + 1;
                                    sendNotification(title, message2, usertypeid, timestamp);
                                    setRead(idNotifikasi);
                                } else if (isRead == 1) {
                                    isi = isi + 1;
                                } else {
                                    isi = isi + 1;
                                }

                            }
                        } else {
                            isi = 0;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(ANError anError) {
                }
            });
        }
    }

    //mengirim push notification jika ada order baru
    public void sendNotification(String title, String Message, int usertypeid, String timestamp) {
        //1 = image, 2 = informasi, 3 = notif
        if (usertypeid == 3) {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (usertypeid == 2) {
            intent = new Intent(this, MainActivityGuru.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

//        intent.putExtra("ib_id", Integer.parseInt(extraNa));


        Log.d("testmasukPaeko", "pasti");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int valuess = 15;
        mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, valuess, 0);

        Uri defaultSoundUri = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.ding_ding_sms);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(CHANNEL_ID, "My Notifications"));
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.sukaresik)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sukaresik))
                .setContentTitle(title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Message))
                .setContentIntent(pendingIntent);
        Random r = new Random();
        int i1 = r.nextInt(9999 - 1111) + 1111;
        notificationManager.notify(i1, builder.build());

    }

    private void setRead(final int notificationId) {
        Log.d("masukIsread", notificationId + "");
        AndroidNetworking.post(NotifikasiApi.POST_UPDATE)
                .addBodyParameter("id_notifikasi", String.valueOf(notificationId))
                .addBodyParameter("is_read", String.valueOf(1))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {

                    }
                }).getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("prilude");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
            }
        });
    }


}
