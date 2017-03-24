package org.secuso.kachok.fragments;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.secuso.kachok.Factory;
import org.secuso.kachok.R;
import org.secuso.kachok.adapters.ReportAdapter;
import org.secuso.kachok.models.ActivityChartDataSet;
import org.secuso.kachok.models.ActivityDayChart;
import org.secuso.kachok.models.ActivitySummary;
import org.secuso.kachok.models.StepCount;
import org.secuso.kachok.models.WalkingMode;
import org.secuso.kachok.persistence.StepCountPersistenceHelper;
import org.secuso.kachok.persistence.WalkingModePersistenceHelper;
import org.secuso.kachok.services.AbstractStepDetectorService;
import org.secuso.kachok.utils.StepDetectionServiceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Report-fragment for one specific day
 * <p/>
 * Activities that contain this fragment must implement the
 * {@link DailyReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author Tobias Neidig
 * @version 20160727
 */
public class DailyReportFragment extends Fragment implements ReportAdapter.OnItemClickListener {
    public static String LOG_TAG = DailyReportFragment.class.getName();
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver();
    private ReportAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;
    private ActivitySummary activitySummary;
    private ActivityDayChart activityChart;
    private List<Object> reports = new ArrayList<>();
    private Calendar day;
    private boolean generatingReports;
    private AbstractStepDetectorService.StepDetectorBinder myBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (AbstractStepDetectorService.StepDetectorBinder) service;
            generateReports(true);
        }
    };

    public DailyReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of DailyReportFragment.
     */
    public static DailyReportFragment newInstance() {
        DailyReportFragment fragment = new DailyReportFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
           mParam1 = getArguments().getString(ARG_PARAM1);
        }*/
        // register for steps-saved-event
        day = Calendar.getInstance();
        registerReceivers();
        // Bind to stepDetector if today is shown
        if (isTodayShown() && StepDetectionServiceHelper.isStepDetectionEnabled(getContext())) {
            bindService();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_report, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // Generate the reports
        generateReports(false);

        mAdapter = new ReportAdapter(reports);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(day == null){
            day = Calendar.getInstance();
        }
        if(isTodayShown()){
            bindService();
        }
        registerReceivers();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isTodayShown()){
            bindService();
        }
    }

    @Override
    public void onDetach() {
        unbindService();
        unregisterReceivers();
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onPause(){
        unbindService();
        unregisterReceivers();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unbindService();
        unregisterReceivers();
        super.onDestroy();
    }


    private void registerReceivers(){
        // subscribe to onStepsSaved and onStepsDetected broadcasts
        IntentFilter filterRefreshUpdate = new IntentFilter();
        filterRefreshUpdate.addAction(StepCountPersistenceHelper.BROADCAST_ACTION_STEPS_SAVED);
        filterRefreshUpdate.addAction(AbstractStepDetectorService.BROADCAST_ACTION_STEPS_DETECTED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filterRefreshUpdate);
    }

    private void unregisterReceivers(){
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private void bindService(){
        if(myBinder == null) {
            Intent serviceIntent = new Intent(getContext(), Factory.getStepDetectorServiceClass(getContext().getPackageManager()));
            getActivity().getApplicationContext().bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindService(){
        if (this.isTodayShown() && mServiceConnection != null && myBinder != null && myBinder.getService() != null) {
            getActivity().getApplicationContext().unbindService(mServiceConnection);
            myBinder = null;
        }
    }

    /**
     * @return is the day which is currently shown today?
     */
    private boolean isTodayShown() {
        return (Calendar.getInstance().get(Calendar.YEAR) == day.get(Calendar.YEAR) &&
                Calendar.getInstance().get(Calendar.MONTH) == day.get(Calendar.MONTH) &&
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Generates the report objects and adds them to the recycler view adapter.
     * The following reports will be generated:
     * * ActivitySummary
     * * ActivityChart
     * If one of these reports does not exist it will be created and added at the end of view.
     *
     * @param updated determines if the method is called because of an update of current steps.
     *                If set to true and another day than today is shown the call will be ignored.
     */
    private void generateReports(boolean updated) {
        Log.i(LOG_TAG, "Generating reports");
        if (!this.isTodayShown() && updated || isDetached() || getContext() == null || generatingReports) {
            // the day shown is not today or is detached
            return;
        }
        generatingReports = true;
        // Get all step counts for this day.
        final Context context = getActivity().getApplicationContext();
        final Locale locale = context.getResources().getConfiguration().locale;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        List<StepCount> stepCounts = StepCountPersistenceHelper.getStepCountsForDay(day, context);
        int stepCount = 0;
        double distance = 0;
        int calories = 0;
        if (this.isTodayShown() && myBinder != null) {
            // Today is shown. Add the steps which are not in database.
            StepCount s = new StepCount();
            if (stepCounts.size() > 0) {
                s.setStartTime(stepCounts.get(stepCounts.size() - 1).getEndTime());
            } else {
                s.setStartTime(day.getTimeInMillis());
            }
            s.setEndTime(Calendar.getInstance().getTimeInMillis()); // now
            s.setStepCount(myBinder.stepsSinceLastSave());
            s.setWalkingMode(WalkingModePersistenceHelper.getActiveMode(context)); // add current walking mode
            stepCounts.add(s);
        }
        Map<String, ActivityChartDataSet> stepData = new LinkedHashMap<>();
        Map<String, ActivityChartDataSet> distanceData = new LinkedHashMap<>();
        Map<String, ActivityChartDataSet> caloriesData = new LinkedHashMap<>();
        WalkingMode wm = null;
        int hour = -1;

        // fill hours without info
        int e;
        if (stepCounts.size() > 0) {
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(stepCounts.get(0).getEndTime());
            e = end.get(Calendar.HOUR_OF_DAY);
        } else {
            e = 24;
        }

        for (int h = 0; h < e; h++) {
            StepCount s = new StepCount();
            Calendar m = day;
            m.set(Calendar.HOUR_OF_DAY, h);
            m.set(Calendar.MINUTE, 0);
            m.set(Calendar.SECOND, 0);
            s.setStartTime(m.getTimeInMillis());
            s.setEndTime(m.getTimeInMillis());
            stepCounts.add(h, s);
        }
        // Create report data
        SimpleDateFormat formatHourMinute = new SimpleDateFormat("HH:mm", locale);
        for (StepCount s : stepCounts) {
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(s.getEndTime());

            stepCount += s.getStepCount();
            distance += s.getDistance();
            calories += s.getCalories(context);

            if (s.getWalkingMode() == null && wm != null || s.getWalkingMode() != null && wm == null ||
                    s.getWalkingMode() != null && wm != null && s.getWalkingMode().getId() != wm.getId() ||
                    end.get(Calendar.HOUR_OF_DAY) != hour || stepCounts.indexOf(s) == stepCounts.size() - 1) {
                // create new field
                wm = s.getWalkingMode();
                hour = end.get(Calendar.HOUR_OF_DAY);
                stepData.put(formatHourMinute.format(end.getTime()), new ActivityChartDataSet(stepCount, s));
                distanceData.put(formatHourMinute.format(end.getTime()), new ActivityChartDataSet(distance, s));
                caloriesData.put(formatHourMinute.format(end.getTime()), new ActivityChartDataSet(calories, s));
            }
        }

        // fill hours without info
        if (stepCounts.size() > 0) {
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(stepCounts.get(stepCounts.size() - 1).getEndTime());
            e = end.get(Calendar.HOUR_OF_DAY);
            for (int h = e + 1; h < 24; h++) {
                stepData.put(h + ":00", null);
                distanceData.put(h + ":00", null);
                caloriesData.put(h + ":00", null);
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MMMM", locale);

        // create view models
        if (activitySummary == null) {
            activitySummary = new ActivitySummary(stepCount, distance, calories, simpleDateFormat.format(day.getTime()));
            reports.add(activitySummary);
        } else {
            activitySummary.setSteps(stepCount);
            activitySummary.setDistance(distance);
            activitySummary.setCalories(calories);
            activitySummary.setTitle(simpleDateFormat.format(day.getTime()));
        }

        if (activityChart == null) {
            activityChart = new ActivityDayChart(stepData, distanceData, caloriesData, simpleDateFormat.format(day.getTime()));
            activityChart.setDisplayedDataType(ActivityDayChart.DataType.STEPS);
            reports.add(activityChart);
        } else {
            activityChart.setSteps(stepData);
            activityChart.setDistance(distanceData);
            activityChart.setCalories(caloriesData);
            activityChart.setTitle(simpleDateFormat.format(day.getTime()));
        }
        String d = sharedPref.getString(context.getString(R.string.pref_daily_step_goal), "10000");
        activityChart.setGoal(Integer.valueOf(d));


        // notify ui
        if (mAdapter != null && mRecyclerView != null && !mRecyclerView.isComputingLayout()) {
            mAdapter.notifyItemChanged(reports.indexOf(activitySummary));

            mAdapter.notifyItemChanged(reports.indexOf(activityChart));
            mAdapter.notifyDataSetChanged();
        } else {
            Log.w(LOG_TAG, "Cannot inform adapter for changes.");
        }
        generatingReports = false;
    }

    @Override
    public void onActivityChartDataTypeClicked(ActivityDayChart.DataType newDataType) {
        Log.i(LOG_TAG, "Changing  displayed data type to " + newDataType.toString());
        if (this.activityChart == null) {
            return;
        }
        if (this.activityChart.getDisplayedDataType() == newDataType) {
            return;
        }
        this.activityChart.setDisplayedDataType(newDataType);
        if (this.mAdapter != null) {
            this.mAdapter.notifyItemChanged(this.reports.indexOf(this.activityChart));
        }
    }

    @Override
    public void setActivityChartDataTypeChecked(Menu menu) {
        if (this.activityChart == null) {
            return;
        }
        if (this.activityChart.getDisplayedDataType() == null) {
            menu.findItem(R.id.menu_steps).setChecked(true);
        }
        switch (this.activityChart.getDisplayedDataType()) {
            case DISTANCE:
                menu.findItem(R.id.menu_distance).setChecked(true);
                break;
            case CALORIES:
                menu.findItem(R.id.menu_calories).setChecked(true);
                break;
            case STEPS:
            default:
                menu.findItem(R.id.menu_steps).setChecked(true);
        }
    }

    @Override
    public void onPrevClicked() {
        this.day.add(Calendar.DAY_OF_MONTH, -1);
        this.generateReports(false);
        if (isTodayShown() && StepDetectionServiceHelper.isStepDetectionEnabled(getContext())) {
            bindService();
        }
    }

    @Override
    public void onNextClicked() {
        this.day.add(Calendar.DAY_OF_MONTH, 1);
        this.generateReports(false);
        if (isTodayShown() && StepDetectionServiceHelper.isStepDetectionEnabled(getContext())) {
            bindService();
        }
    }

    @Override
    public void onTitleClicked() {
        int year = this.day.get(Calendar.YEAR);
        int month = this.day.get(Calendar.MONTH);
        int day = this.day.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.AppTheme_DatePickerDialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DailyReportFragment.this.day.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DailyReportFragment.this.day.set(Calendar.MONTH, monthOfYear);
                DailyReportFragment.this.day.set(Calendar.YEAR, year);
                DailyReportFragment.this.generateReports(false);
                if (isTodayShown() && StepDetectionServiceHelper.isStepDetectionEnabled(getContext())) {
                    bindService();
                }
            }
        }, year, month, day);
        dialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // Currently doing nothing here.
    }

    public class BroadcastReceiver extends android.content.BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.w(LOG_TAG, "Received intent which is null.");
                return;
            }
            switch (intent.getAction()) {
                case AbstractStepDetectorService.BROADCAST_ACTION_STEPS_DETECTED:
                case StepCountPersistenceHelper.BROADCAST_ACTION_STEPS_SAVED:
                case WalkingModePersistenceHelper.BROADCAST_ACTION_WALKING_MODE_CHANGED:
                    // Steps were saved, reload step count from database
                    generateReports(true);
                    break;
                default:
            }
        }
    }
}
