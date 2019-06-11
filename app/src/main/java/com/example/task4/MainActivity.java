package com.example.task4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.task4.NewAppWidget.getCurr_date;

public class MainActivity extends Activity {

    final Context context = this;
    private int mYear, mMonth, mDay;
    private int mAppWidgetId;
    Intent resultValue;
    Activity contextActivity;
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.activity_main);
        contextActivity = this;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Log.e("YearTag",String.valueOf(dayOfMonth));

                        setResult(RESULT_OK, resultValue);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(contextActivity);
                        NewAppWidget.setCurr_date(dayOfMonth,monthOfYear,year);
                        NewAppWidget.updateAppWidget(contextActivity, appWidgetManager, widgetID);
                        finish();

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(contextActivity);
                NewAppWidget.updateAppWidget(contextActivity, appWidgetManager, widgetID);
                finish();
            }
        });

        datePickerDialog.show();
    }
    }