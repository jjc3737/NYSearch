package com.codepath.nysearch.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.codepath.nysearch.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends DialogFragment {

    @Bind(R.id.dpBeignDatePicker) DatePicker mdatePicker;
    @Bind(R.id.spSortOrder) Spinner mspinner;
    @Bind(R.id.cbArts) CheckBox mArtsCheckBox;
    @Bind(R.id.cbFashion) CheckBox mFashionCheckBox;
    @Bind(R.id.cbSports) CheckBox mSportsCheckBox;

    private SharedPreferences setting;

    public static final String beginDatePrefs = "DATE_PREFS";
    public static final String sortOrder = "SORT_PREFS";
    public static final String artsNewsDesk = "ARTS_NEWS_DESK_PREFS";
    public static final String fashionNewsDesk = "FASHION_NEWs_DESK_PREFS";
    public static final String sportsNewsDesk = "SPORTS_NEWS_DESK_PREFS";
    public static final String prefName = "MY_SHARED_PREFS";

    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        return view;
    }

    public void getSharedPreferences() {
        Long beginDate = setting.getLong(beginDatePrefs, 0);

        if (beginDate != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(beginDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            mdatePicker.updateDate(year, month, day);
        }

        int order = setting.getInt(sortOrder, 0);
        mspinner.setSelection(order);

        Boolean arts = setting.getBoolean(artsNewsDesk, false);
        mArtsCheckBox.setChecked(arts);

        Boolean fashion = setting.getBoolean(fashionNewsDesk, false);
        mFashionCheckBox.setChecked(fashion);

        Boolean sports = setting.getBoolean(sportsNewsDesk, false);
        mSportsCheckBox.setChecked(sports);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setting = getActivity().getApplicationContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);

        Button btnSave = (Button) view.findViewById(R.id.btSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClicked();
            }
        });
        getSharedPreferences();

    }

    public void saveClicked() {
        int year = mdatePicker.getYear();
        int month = mdatePicker.getMonth();
        int day = mdatePicker.getDayOfMonth();

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Long date = cal.getTimeInMillis();

        SharedPreferences.Editor et = setting.edit();
        et.putLong(beginDatePrefs, date);

        int sort = mspinner.getSelectedItemPosition();
        et.putInt(sortOrder, sort);

        Boolean arts = mArtsCheckBox.isChecked();
        et.putBoolean(artsNewsDesk, arts);

        Boolean fashion = mFashionCheckBox.isChecked();
        et.putBoolean(fashionNewsDesk, fashion);

        Boolean sports = mSportsCheckBox.isChecked();
        et.putBoolean(sportsNewsDesk, sports);

        et.apply();
        //Change filter in activity?
        this.dismiss();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
