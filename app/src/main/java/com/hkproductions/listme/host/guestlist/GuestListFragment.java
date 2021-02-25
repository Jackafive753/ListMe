package com.hkproductions.listme.host.guestlist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentGuestListBinding;
import com.hkproductions.listme.host.database.HostDataDao;
import com.hkproductions.listme.host.database.HostDatabase;

import java.util.Calendar;

public class GuestListFragment extends Fragment {

    private HostFragmentGuestListBinding binding;
    private GuestListViewModel viewModel;
    private Calendar c;
    private boolean expandedSearchfield = true;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Hide Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        // initialize starting values
        initDateAndTime();
        // initialize onClickListeners
        setOnClickListeners();

        Observer<Long> observer = aLong -> viewModel.alterList();
        GuestAdapter guestAdapter = new GuestAdapter();
        binding.recyclerViewGuests.setAdapter(guestAdapter);
        viewModel.liveName.observe(getViewLifecycleOwner(), s -> viewModel.alterList());
        viewModel.liveDate.observe(getViewLifecycleOwner(), observer);
        viewModel.liveStartTime.observe(getViewLifecycleOwner(), observer);
        viewModel.liveEndTime.observe(getViewLifecycleOwner(), observer);
        viewModel.data.observe(getViewLifecycleOwner(), guestAdapter::submitList);

        //bind layout with data
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.host_fragment_guest_list, container, false);

        //initialize datasource
        HostDataDao datasource = HostDatabase.Companion.getInstance(getContext()).getHostDataDao();

        //initialize viewModel
        GuestListViewModelFactory guestListViewModelFactory = new GuestListViewModelFactory(datasource);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, guestListViewModelFactory);
        viewModel = viewModelProvider.get(GuestListViewModel.class);

        return binding.getRoot();
    }

    /**
     * Utility Method alter Time
     * <p>
     * Uses android.app.TimePickerDialog
     * Displays the picked time in the TimepickerDialog on the textView given as param
     * Updates the liveData liveStartTime or liveEndTime depending on which textView was given as param
     * with the chosen Time
     *
     * @param textView
     */
    private void alterTime(TextView textView) {
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String hourString = "";
            String minString = "";
            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            } else {
                hourString = String.valueOf(hourOfDay);
            }
            if (minute < 10) {
                minString = "0" + minute;
            } else {
                minString = String.valueOf(minute);
            }
            textView.setText(hourString + ":" + minString);
            if (textView.getId() == R.id.TextInputEditTextStartTime) {
                viewModel.liveStartTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            } else {
                viewModel.liveEndTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    /**
     * Utility Method set Date
     * <p>
     * Uses android.app.DatePickerDialog
     * Displays the picked date in the DatePickerDialog on the editTextDate textView
     * Updates the liveData liveDate with chosen date
     */
    private void setDate() {
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            binding.editTextDate.setText(dayOfMonth + "." + month + 1 + "." + year);
            c.set(year, month, dayOfMonth);
            viewModel.liveDate.setValue(c.getTimeInMillis());
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /**
     * Initializing Method initDateAndTime
     * <p>
     * Initializes the Date TextView with the current date, sets liveDate to current Date
     * Initializes the StartTime TextView with 00:00, sets liveStartDate to 00:00
     * Initializes the EndTime TextView with 23:59, sets liveEndDate to 23:59
     */
    private void initDateAndTime() {
        // initialize date to current
        c = Calendar.getInstance();
        binding.editTextDate.setText(c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + (c.get(Calendar.YEAR)));
        viewModel.liveDate.setValue(c.getTimeInMillis());

        //initialize timepicker startTime
        Calendar cT = Calendar.getInstance();
        cT.set(Calendar.HOUR_OF_DAY, 0);
        cT.set(Calendar.MINUTE, 0);
        binding.TextInputEditTextStartTime.setText(cT.get(Calendar.HOUR_OF_DAY) + "0:0" + cT.get(Calendar.MINUTE));
        viewModel.liveStartTime.setValue(cT.getTimeInMillis());

        //initialize timepicker endTime
        Calendar cTEnd = Calendar.getInstance();
        cTEnd.set(Calendar.HOUR_OF_DAY, 23);
        cTEnd.set(Calendar.MINUTE, 59);
        binding.TextInputEditTextEndTime.setText(cTEnd.get(Calendar.HOUR_OF_DAY) + ":" + cTEnd.get(Calendar.MINUTE));
        viewModel.liveEndTime.setValue(cTEnd.getTimeInMillis());
    }

    /**
     * ExpandCollapseSearch Method
     * Expands or collapses the cardview to achieve more viewing space of the guestlist for the host
     *
     * @param expandedSearchfield determines wether the cardview is expanded or not
     */
    private void expandCollapseSearch(boolean expandedSearchfield) {
        if (expandedSearchfield) {


            binding.cardView.setVisibility(View.GONE);

            binding.imageViewArrowStart.animate().rotation(180).setDuration(300);
            binding.imageViewArrowEnd.animate().rotation(180).setDuration(300);
            binding.textViewExpandCollapseSearch.setText(R.string.expand_search_fields_text);

            this.expandedSearchfield = false;
        } else {

            binding.cardView.setVisibility(View.VISIBLE);
            binding.imageViewArrowStart.animate().rotation(0).setDuration(300);
            binding.imageViewArrowEnd.animate().rotation(0).setDuration(300);
            binding.textViewExpandCollapseSearch.setText(R.string.collapse_search_fields_text);
            this.expandedSearchfield = true;
        }
    }

    /**
     * Utility method setOnClickListeners
     * Sets all onClickListeners for the fragment
     */
    private void setOnClickListeners() {
        // set on clickListener on Calendar icon
        binding.imageButtonDatePicker.setOnClickListener(l -> {
            // method for picking date
            setDate();
        });
        // set OnClickListener on Date Textfield
        binding.editTextDate.setOnClickListener(l -> {
            // method for picking date
            setDate();
        });
        //set OnClickListener on left clock icon (startTime)
        binding.imageButtonClockStart.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextStartTime);
        });

        //set OnClickListener on left textfield displaying time
        binding.TextInputEditTextStartTime.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextStartTime);
        });
        //set OnClickListener on right clock icon (endtime)
        binding.imageButtonClockEnd.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextEndTime);
            viewModel.alterList();
        });
        //set OnClickListener on right textfield displaying time
        binding.TextInputEditTextEndTime.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextEndTime);
        });

        //set OnClickListener on imageViewArrowStart
        binding.imageViewArrowStart.setOnClickListener(l -> {
            expandCollapseSearch(expandedSearchfield);
        });
        //set OnClickListener on imageViewArrowEnd
        binding.imageViewArrowEnd.setOnClickListener(l -> {
            expandCollapseSearch(expandedSearchfield);
        });

        //set OnClickListener on textViewExpandCollapseSearch
        binding.textViewExpandCollapseSearch.setOnClickListener(l -> {
            expandCollapseSearch(expandedSearchfield);
        });
    }
}
