package com.example.task4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static int[] curr_date=new int[3];
    private final String ActAction = "MainActivity";
    private Timer timer=new Timer();
    Context context_t;

    public static void setCurr_date(int d,int m,int y)
    {
        //Log.e("sterr",String.valueOf(getCurr_date(2)));

        curr_date[0]=d;
        curr_date[1]=m;
        curr_date[2]=y;
       //Log.e("sterrar",String.valueOf(getCurr_date(2)));
    }

    public static int getCurr_date(int idx)
    {

        Log.e("this",String.valueOf(curr_date[idx])+":"+String.valueOf(idx));

        return curr_date[idx];}
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        Calendar c = Calendar.getInstance();

        c.setTime(new Date());

        int doy = c.get(Calendar.DAY_OF_YEAR); //получаем нынешний день

        int hod = c.get(Calendar.HOUR_OF_DAY); // час

        int moh = c.get(Calendar.MINUTE); // минуту

        //int som = c.get(Calendar.SECOND);

        int diy = 0;

        if (isLeapViaPopeGregory(c.YEAR)) {

            diy = 366;

        } else {

            diy = 365;

        }

        int daysLeft = diy - doy;

        String yearLeft=String.valueOf(getCurr_date(2)-c.get(Calendar.YEAR));
        String dayLeft=String.valueOf(getCurr_date(1)-doy);
        String hoursLeft = Integer.toString(24 - hod);
        String minutesLeft = Integer.toString(60 - moh);
        //String secondsLeft = Integer.toString(60 — som);

        if (hoursLeft.length() < 2) {

            hoursLeft = "0"+hoursLeft;

        }

        if (minutesLeft.length() < 2) {

            minutesLeft = "0"+minutesLeft;

        }

       if((hod==9)&&(moh==0))
           notificationEventStarted(context, appWidgetId);

        views.setTextViewText(R.id.dayCounter,"Дней "+ dayLeft);
        views.setTextViewText(R.id.yearCounter,"Лет "+yearLeft);
        views.setTextViewText(R.id.hmCounter, "Часов "+hoursLeft +  ":" +minutesLeft);
        appWidgetManager.updateAppWidget(appWidgetId,views);

        Intent intent=new Intent(context,MainActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,appWidgetId,intent,0);
        views.setOnClickPendingIntent(R.id.clv,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        if(getCurr_date(0)==0 && getCurr_date(1)==0&&getCurr_date(2)==0)
        {
            setCurr_date(1,1,2020);

        }

        startTimer(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void startTimer(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                for (int i = 0; i < appWidgetIds.length; i++) {

                    updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
                }
            }
        }, 0, 600);
    }

    private void stopTimer() {
        timer.cancel();
    }

    public static final boolean isLeapViaPopeGregory ( int yyyy ) {

        if ( yyyy < 0 ) return( yyyy + 1 ) % 4 == 0;

        if ( yyyy < 1582 ) return yyyy % 4 == 0;

        if ( yyyy % 4 != 0 ) return false;

        if ( yyyy % 100 != 0 ) return true;

        if ( yyyy % 400 != 0 ) return false;

        return true;
    }

    @Override
    public void onEnabled(Context context) {

        Log.e("sterr",String.valueOf(getCurr_date(2)));

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    public static void notificationEventStarted(Context context, int widgetID) {
        final int NOTIFY_ID = 101;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Resources res = context.getResources();
            NotificationChannel notificationChannel = new NotificationChannel("EventStarted", "Событие наступило", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "EventStarted")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("time has come")
                    .setContentText("Ваше время на этой планете подошло к концу")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
    }
}