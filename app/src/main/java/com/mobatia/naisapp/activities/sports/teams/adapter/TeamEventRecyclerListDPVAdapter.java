package com.mobatia.naisapp.activities.sports.teams.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.teams.model.TeamDatesModel;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventClickModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.mobatia.naisapp.constants.NASCalendarConstants.EVENT_ID;
import static com.mobatia.naisapp.constants.NASCalendarConstants.METHOD;
import static com.mobatia.naisapp.constants.NASCalendarConstants.MINUTES;

public class TeamEventRecyclerListDPVAdapter extends RecyclerView.Adapter<TeamEventRecyclerListDPVAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TeamDatesModel> mStudentList;
    private ArrayList<TeamEventClickModel> mmStudent;
    String dept;
    private int mnthId;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayTxt,dateTxt,timeTxt,placeTxt;
        ImageView addicon,removeicon;

        public MyViewHolder(View view) {
            super(view);

            dateTxt= view.findViewById(R.id.dateTxt);
            timeTxt= view.findViewById(R.id.timeTxt);
            placeTxt= view.findViewById(R.id.placeTxt);
            addicon= view.findViewById(R.id.addicon);
            removeicon= view.findViewById(R.id.removeicon);

        }
    }


    public TeamEventRecyclerListDPVAdapter(Context mContext, ArrayList<TeamDatesModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_team_dpt_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.dateTxt.setText(AppUtils.dateConversionddmmyyyy(mStudentList.get(position).getDate()));
        holder.timeTxt.setText("("+AppUtils.convertTimeToAMPM(mStudentList.get(position).getStart_time())+"-"+AppUtils.convertTimeToAMPM(mStudentList.get(position).getEnd_time())+")");
        holder.placeTxt.setText(mStudentList.get(position).getVenue());
        holder.removeicon.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {


                                                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                                      if (AppController.eventIdList.size() == 0) {
                                                          if (!mStudentList.get(position).getId()
                                                                  .equalsIgnoreCase("")) {
                                                              Uri deleteUri = ContentUris.withAppendedId(
                                                                      CalendarContract.Events.CONTENT_URI, Long
                                                                              .parseLong(mStudentList.get(position)
                                                                                      .getId()));
                                                              mContext.getContentResolver().delete(deleteUri, null,
                                                                      null);
                                                              mStudentList.get(position).setId("");
                                                              Toast.makeText(mContext,
                                                                      mContext.getResources().getString(
                                                                              R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                            /*PreferenceManager.setCalendarEventNames(
                                    mContext,
                                    PreferenceManager.getCalendarEventNames(
                                            mContext).replace(
                                            (calendarModels.get(position).getEvent()
                                                    + calendarModels.get(position).getEvent() + ","), ""));*/
                                                              PreferenceManager.setTeamsCalendarEventNames(mContext, "");

                                                          } else {
                                                              Toast.makeText(mContext,
                                                                      mContext.getResources().getString(
                                                                              R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                                                          }
                                                      } else {
                                                          Uri deleteUri = null;
                                                          deleteUri = ContentUris.withAppendedId(
                                                                  CalendarContract.Events.CONTENT_URI, Long
                                                                          .parseLong(mStudentList.get(position)
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
                                                          PreferenceManager.setTeamsCalendarEventNames(mContext, "");
//                        AppController.eventIdList.remove(String.valueOf(AppController.eventIdList.get(position)));

                                                      }

                                                  }
                                              }
                                          }

        );


        // if button is clicked, close the custom dialog
        holder.addicon.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 String reqSentence = AppUtils.htmlparsing(String.valueOf(
                                                         Html.fromHtml(mStudentList.get(position)
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
                                               //  String allDay = "0";
                                                 year = Integer.parseInt(mStudentList.get(position).getYearDate());
                                                 month = getMonthDetails(mContext, mStudentList.get(position).getMonthDate());
                                                 day = Integer.parseInt(mStudentList.get(position).getDayDate());
                                                 if(mStudentList.get(position).getTime().equalsIgnoreCase(""))
                                                 {
                                                     hour = -1;
                                                     min = -1;
                                                 }
                                                 else {
                                                     timeString = mStudentList.get(position).getTime().split(":");
                                                     hour = Integer.parseInt(timeString[0]);
                                                     min = Integer.parseInt(timeString[1]);
                                                 }
                                                // allDay = mStudentList.get(position).getIsAllDay();

                                                 if(mStudentList.get(position).getTime().equalsIgnoreCase(""))
                                                 {
                                                     hour1 = -1;
                                                     min1 = -1;
                                                 }
                                                 else {
                                                     timeString1 = mStudentList.get(position).getTime().split(":");
                                                     hour = Integer.parseInt(timeString1[0]);
                                                     min = Integer.parseInt(timeString1[1]);
                                                 }

                                             /*    timeString1 = mCalendarEventModels.get(eventPosition).getToTime().split(":");
                                                 hour1 = Integer.parseInt(timeString1[0]);
                                                 min1 = Integer.parseInt(timeString1[1]);*/
                                                 boolean addToCalendar = true;
                                                 String[] prefData = PreferenceManager
                                                         .getTeamsCalendarEventNames(mContext).split(",");
                                                 for (int i = 0; i < prefData.length; i++) {
                                                     if (prefData[i].equalsIgnoreCase(mStudentList.get(position).getEvent() + mStudentList.get(position).getEvent())) {
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
                                                                     mStudentList.get(position).getEvent(),
                                                                     mStudentList.get(position).getEvent(), 0, position, mStudentList);

                                                         }
                                                         else {
                                                             addReminder(year, month, day, hour, min, year, month,
                                                                     (day), hour1, min1,
                                                                     mStudentList.get(position).getEvent(),
                                                                     mStudentList.get(position).getEvent(), 0, position,  mStudentList);
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
                                                 notifyDataSetChanged();

                                             }
                                         }

        );


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
                            String description, int count, int position,ArrayList<TeamDatesModel>mCalendarEventModelAdd) {
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
        /*if (allDay.equals("1")) {
            eventValues.put(CalendarContract.Events.ALL_DAY, true);
        } else {
            eventValues.put(CalendarContract.Events.ALL_DAY, false);
        }*/
        eventValues.put("eventStatus", 1);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri eventUri = mContext.getContentResolver().insert(
                Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        Log.d("TAG", "1----" + eventID);
        mCalendarEventModelAdd.get(position).setId(String.valueOf(eventID));
        Log.d("TAG", "2----");

        PreferenceManager.setTeamsCalendarEventNames(mContext,
                PreferenceManager.getTeamsCalendarEventNames(mContext) + name
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




    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

}
