package com.mobatia.naisapp.fragments.calendar.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.NASCalendarConstants;
import com.mobatia.naisapp.fragments.calendar.model.CalendarModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Rijo K Jose on 15/3/17.
 */
public class EventsAdapter extends BaseAdapter implements NASCalendarConstants {
    Context mContext;
    ArrayList<CalendarModel> calendarModels;
    int colors, mPosition;
    LayoutInflater minfalter;
    ViewHolder viewHolder;
    private int mnthId;
    boolean isRead;


    public EventsAdapter(Context mContext, ArrayList<CalendarModel> calendarModels) {
        this.mContext = mContext;
        this.calendarModels = calendarModels;
    }

    public EventsAdapter(Context mContext, ArrayList<CalendarModel> calendarModels, int colors) {
        this.mContext = mContext;
        this.calendarModels = calendarModels;
        this.colors = colors;
    }

    public EventsAdapter(Context mContext, ArrayList<CalendarModel> calendarModels, int colors, int mPosition,boolean isRead) {
        this.mContext = mContext;
        this.calendarModels = calendarModels;
        this.colors = colors;
        this.mPosition = mPosition;
        this.isRead = isRead;
    }

    @Override
    public int getCount() {
        return calendarModels.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final LayoutInflater minfalter = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            minfalter = LayoutInflater.from(mContext);
            convertView = minfalter.inflate(R.layout.layout_calendar_events_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.eventName);
            viewHolder.eventTime = (TextView) convertView.findViewById(R.id.eventTime);
            viewHolder.addiconImageView = (ImageView) convertView.findViewById(R.id.addicon);
            viewHolder.removeiconImageView = (ImageView) convertView.findViewById(R.id.removeicon);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.statusImg);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventTime.setTextColor(colors);
        viewHolder.eventName.setTextColor(colors);
        viewHolder.eventName.setText(calendarModels.get(position).getEvent());
        if (isRead)
        {
            viewHolder.statusImg.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(calendarModels.get(position).getStatus().equalsIgnoreCase("0"))
            {
                viewHolder.statusImg.setVisibility(View.VISIBLE);

                viewHolder.statusImg.setImageResource(R.drawable.shape_circle_red);
            }
            else if(calendarModels.get(position).getStatus().equalsIgnoreCase("1"))
            {
                viewHolder.statusImg.setVisibility(View.INVISIBLE);
            }
            else if(calendarModels.get(position).getStatus().equalsIgnoreCase("2"))
            {
                viewHolder.statusImg.setVisibility(View.VISIBLE);

                viewHolder.statusImg.setImageResource(R.drawable.shape_circle_orange);
            }
        }

        if(calendarModels.get(position).getIsAllDay().equalsIgnoreCase("1") )
        {
            viewHolder.eventTime.setText("All Day Event");
        }
        else if (!(calendarModels.get(position).getStartTime().equalsIgnoreCase("")) && !(calendarModels.get(position).getEndTime().equalsIgnoreCase(""))){
            viewHolder.eventTime.setText(calendarModels.get(position).getStartTime() + " - " + calendarModels.get(position).getEndTime());
        }
        else if (!(calendarModels.get(position).getStartTime().equalsIgnoreCase(""))){
            viewHolder.eventTime.setText(calendarModels.get(position).getStartTime());
        }else if (!(calendarModels.get(position).getEndTime().equalsIgnoreCase(""))){
            viewHolder.eventTime.setText(calendarModels.get(position).getEndTime());
        }
        if (viewHolder.eventName.getCurrentTextColor()==mContext.getResources().getColor(R.color.cal_row_1)) {
            viewHolder.addiconImageView.setImageResource(R.drawable.addicon4);
            viewHolder.removeiconImageView.setImageResource(R.drawable.minimize4);
        } else if (viewHolder.eventName.getCurrentTextColor()==mContext.getResources().getColor(R.color.cal_row_2)) {
            viewHolder.addiconImageView.setImageResource(R.drawable.addicon3);
            viewHolder.removeiconImageView.setImageResource(R.drawable.minimize3);
        } else if (viewHolder.eventName.getCurrentTextColor()==mContext.getResources().getColor(R.color.cal_row_3)){
            viewHolder.addiconImageView.setImageResource(R.drawable.addicon2);
            viewHolder.removeiconImageView.setImageResource(R.drawable.minimize2);
        } else if (viewHolder.eventName.getCurrentTextColor()==mContext.getResources().getColor(R.color.cal_row_4)){
            viewHolder.addiconImageView.setImageResource(R.drawable.addicon1);
            viewHolder.removeiconImageView.setImageResource(R.drawable.minimize1);
        }

        viewHolder.addiconImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String reqSentence = AppUtils.htmlparsing(String.valueOf(
                        Html.fromHtml(calendarModels.get(position)
                                .getEvent())).replaceAll("\\s+", " "));
                String[] splited = reqSentence.split("\\s+");
                String[] dateString;
                int year = -1;
                int month = -1;
                int day = -1;
                String[] timeString;
                int hour = -1;
                int min = -1;
                String[] timeString1;
                int hour1 = -1;
                int min1 = -1;
                String allDay = "0";
                year = Integer.parseInt(calendarModels.get(position).getYearDate());
                month = getMonthDetails(mContext, calendarModels.get(position).getMonthDate());
                day = Integer.parseInt(calendarModels.get(position).getDayDate());
                if (!(calendarModels.get(position).getFromTime().equalsIgnoreCase(""))) {

                    timeString = calendarModels.get(position).getFromTime().split(":");
                    hour = Integer.parseInt(timeString[0]);
                    min = Integer.parseInt(timeString[1]);
                }
                else
                {
                    hour=-1;
                    min=-1;
                }
//                allDay = calendarModels.get(position).getEventModels().get(position).getIsAllDay();
                allDay = calendarModels.get(position).getIsAllDay();
if (!(calendarModels.get(position).getToTime().equalsIgnoreCase(""))) {
    timeString1 = calendarModels.get(position).getToTime().split(":");
    hour1 = Integer.parseInt(timeString1[0]);
    min1 = Integer.parseInt(timeString1[1]);
}
                else
{
    hour1=-1;
    min1=-1;
}
                boolean addToCalendar = true;
                String[] prefData = PreferenceManager
                        .getCalendarEventNames(mContext).split(",");
                for (int i = 0; i < prefData.length; i++) {
                    if (prefData[i].equalsIgnoreCase(calendarModels
                            .get(position).getEvent() + calendarModels.get(position).getEvent())) {
                        addToCalendar = false;
                        break;
                    }
                }
                System.out.println("addToCalendar---" + addToCalendar);
                if (addToCalendar) {
                    if (year != -1 && month != -1 && day != -1 && hour != -1
                            && min != -1) {
                        if (hour1==-1 && min1==-1)
                        {
                            addReminder(year, month, day, hour, min, year, month,
                                    (day), hour, min,
                                    calendarModels.get(position).getEvent(),
                                    calendarModels.get(position).getEvent(), 0, position, allDay);
                        }
                        else {
                            addReminder(year, month, day, hour, min, year, month,
                                    (day), hour1, min1,
                                    calendarModels.get(position).getEvent(),
                                    calendarModels.get(position).getEvent(), 0, position, allDay);
                        }
                    } else {
                        Toast.makeText(mContext,
                                mContext.getResources().getString(
                                        R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            mContext.getResources().getString(
                                    R.string.add_cal_success), Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.removeiconImageView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    if (AppController.eventIdList.size() == 0) {
                        if (!calendarModels.get(position).getId()
                                .equalsIgnoreCase("")) {
                            Uri deleteUri = ContentUris.withAppendedId(
                                    CalendarContract.Events.CONTENT_URI, Long
                                            .parseLong(calendarModels.get(position)
                                                    .getId()));
                            mContext.getContentResolver().delete(deleteUri, null,
                                    null);
                            calendarModels.get(position).setId("");
                            Toast.makeText(mContext,
                                    mContext.getResources().getString(
                                            R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                            /*PreferenceManager.setCalendarEventNames(
                                    mContext,
                                    PreferenceManager.getCalendarEventNames(
                                            mContext).replace(
                                            (calendarModels.get(position).getEvent()
                                                    + calendarModels.get(position).getEvent() + ","), ""));*/
                            PreferenceManager.setCalendarEventNames(mContext, "");

                        } else {
                            Toast.makeText(mContext,
                                    mContext.getResources().getString(
                                            R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Uri deleteUri = null;
                        deleteUri = ContentUris.withAppendedId(
                                CalendarContract.Events.CONTENT_URI, Long
                                        .parseLong(calendarModels.get(position)
                                                .getId()));
                        //                        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(AppController.eventIdList.get(position))));
                        int rows = mContext.getContentResolver().delete(deleteUri, null, null);
                        Log.d("deletion1 ", rows + " events deleted");

                        Toast.makeText(mContext,
                                mContext.getResources().getString(
                                        R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                                                            /*PreferenceManager.setCalendarEventNames(
                                mContext,
                                PreferenceManager.getCalendarEventNames(
                                        mContext).replace(
                                        (calendarModels.get(position).getEvent()
                                                + calendarModels.get(position).getEvent() + ","), ""));*/
                        PreferenceManager.setCalendarEventNames(mContext, "");
//                        AppController.eventIdList.remove(String.valueOf(AppController.eventIdList.get(position)));

                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView eventName;
        TextView eventTime;
        ImageView addiconImageView;
        ImageView removeiconImageView;
        ImageView statusImg;

    }

    /*******************************************************
     * Method name : addReminder() Description : add reminder to calendar
     * without popup Parameters : statrYear, startMonth, startDay, startHour,
     * startMinute, endYear, endMonth, endDay, endHour, endMinutes, name Return
     * type : void Date : 21-Jan-2015 Author : Rijo K Jose
     *****************************************************/

    public void addReminder(int startYear, int startMonth, int startDay,
                            int startHour, int startMinute, int endYear, int endMonth,
                            int endDay, int endHour, int endMinutes, String name,
                            String description, int count, int position, String allDay) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinutes);
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();
        if (Build.VERSION.SDK_INT >  Build.VERSION_CODES.M) {
            // Marshmallow+
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else if (Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP_MR1) {
            // lollipop
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else{
            //below Marshmallow
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);//1

        }

//        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);//1
        eventValues.put(CalendarContract.Events.TITLE, name);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.SHORT);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        if (allDay.equals("1")) {
            eventValues.put(CalendarContract.Events.ALL_DAY, true);
        } else {
            eventValues.put(CalendarContract.Events.ALL_DAY, false);
        }
        eventValues.put("eventStatus", 1);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri eventUri = mContext.getContentResolver().insert(
                Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        Log.d("TAG", "1----" + eventID);
        calendarModels.get(position).setId(String.valueOf(eventID));
        Log.d("TAG", "2----");

        PreferenceManager.setCalendarEventNames(mContext,
                PreferenceManager.getCalendarEventNames(mContext) + name
                        + description + ",");
        if (count == 0) {
            Toast.makeText(mContext, mContext.getResources()
                    .getString(R.string.add_cal_success), Toast.LENGTH_SHORT).show();
        }
        /***************** Event: Reminder(with alert) Adding reminder to event *******************/
        String reminderUriString = "content://com.android.calendar/reminders";
        ContentValues reminderValues = new ContentValues();
        reminderValues.put(EVENT_ID, eventID);
        reminderValues.put(MINUTES, 1440);
        reminderValues.put(METHOD, 1);
        Uri reminderUri = mContext.getContentResolver().insert(
                Uri.parse(reminderUriString), reminderValues);
        long eventIDlong = Long.parseLong(reminderUri.getLastPathSegment());
        AppController.eventIdList.add(String.valueOf(eventID));

    }

    public int getMonthDetails(Context mContext, String descStringTime) {
        // january
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.january))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jan_short));
//            }
            mnthId = 0;
        } // february
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.february))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.feb_short));
//            }
            mnthId = 1;
        } // march
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.march))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.mar_short));
//            }
            mnthId = 2;
        } // april
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.april))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.apr_short));
//            }
            mnthId = 3;
        }// may
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.may))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.may_short));
//            }
            mnthId = 4;
        } // june
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.june))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jun_short));
//            }
            mnthId = 5;
        } // july
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.july))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jul_short));
//            }
            mnthId = 6;
        } // august
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.august))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.aug_short));
//            }
            mnthId = 7;
        } // september
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.september))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.sep_short));
//            }
            mnthId = 8;
        } // october
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.october))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.oct_short));
//            }
            mnthId = 9;
        } // november
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.november))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.nov_short));
//            }
            mnthId = 10;
        } // december
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.december))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.dec_short));
//            }
            mnthId = 11;
        }
        return mnthId;
    }


}
