package com.hkproductions.listme.host.guestlist;

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
import java.util.TimeZone;

public class GuestListFragment extends Fragment {

    private HostFragmentGuestListBinding binding;
    private GuestListViewModel viewModel;
    private HostDataDao datasource;
    private Calendar c;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Hide Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Observer<Long> observer = aLong -> viewModel.alterList();

        binding = DataBindingUtil.inflate(inflater, R.layout.host_fragment_guest_list, container, false);

        //initialize datasource
        datasource = HostDatabase.Companion.getInstance(getContext()).getHostDataDao();

        //initialize viewModel
        GuestListViewModelFactory guestListViewModelFactory = new GuestListViewModelFactory(datasource);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, guestListViewModelFactory);
        viewModel = viewModelProvider.get(GuestListViewModel.class);
        GuestAdapter guestAdapter = new GuestAdapter();
        binding.recyclerViewGuests.setAdapter(guestAdapter);


        // initialize date to current
        c = Calendar.getInstance();
        binding.editTextDate.setText(c.get(Calendar.DAY_OF_MONTH)+"."+(c.get(Calendar.MONTH)+1)+"."+(c.get(Calendar.YEAR)));
        viewModel.liveDate.setValue(c.getTimeInMillis());

        // set on clickListener on Calendar icon
        binding.imageButtonDatePicker.setOnClickListener(l -> {
            // method for picking date
            setDate();
        });
        // set OnClickListener on Date Textfield
        binding.editTextDate.setOnClickListener(l ->{
            // method for picking date
            setDate();
        });
        //initialize timepicker startTime
        Calendar cT = Calendar.getInstance();
        cT.set(Calendar.HOUR_OF_DAY,0);
        cT.set(Calendar.MINUTE,0);
        binding.TextInputEditTextStartTime.setText(cT.get(Calendar.HOUR_OF_DAY)+"0:0"+cT.get(Calendar.MINUTE));
        viewModel.liveStartTime.setValue(cT.getTimeInMillis());

        //set OnClickListener on left clock icon (startTime)
        binding.imageButtonClockStart.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextStartTime);
        });

        //set OnClickListener on left textfield displaying time
        binding.TextInputEditTextStartTime.setOnClickListener(l ->{
            alterTime(binding.TextInputEditTextStartTime);
        });

        //initialize timepicker endTime
        Calendar cTEnd = Calendar.getInstance();
        cTEnd.set(Calendar.HOUR_OF_DAY,23);
        cTEnd.set(Calendar.MINUTE,59);
        binding.TextInputEditTextEndTime.setText(cTEnd.get(Calendar.HOUR_OF_DAY)+":"+cTEnd.get(Calendar.MINUTE));
        viewModel.liveEndTime.setValue(cTEnd.getTimeInMillis());

        //set OnClickListener on right clock icon (endtime)
        binding.imageButtonClockEnd.setOnClickListener(l -> {
            alterTime(binding.TextInputEditTextEndTime);
            viewModel.alterList();
        });

        //set OnClickListener on right textfield displaying time
        binding.TextInputEditTextEndTime.setOnClickListener(l ->{
            alterTime(binding.TextInputEditTextEndTime);
        });
        //TODO:: MAKE IMAGEBUTTON, TEXTVIEWS as big as TEXTINPUTLAYOUT with DIMENSIONS hardcoded

        viewModel.liveName.observe(getViewLifecycleOwner(), s -> viewModel.alterList());
        viewModel.liveDate.observe(getViewLifecycleOwner(), observer);
        viewModel.liveStartTime.observe(getViewLifecycleOwner(), observer);
        viewModel.liveEndTime.observe(getViewLifecycleOwner(), observer);


        viewModel.data.observe(getViewLifecycleOwner(), guestAdapter::submitList);
        return binding.getRoot();
    }

    /**
     * Utility Method alter Time
     *
     * @param textView Used for altering the textViews displaying the Time
     *                 Uses TimepickerDialog
     * TODO: alter TimePicker to last value
     */
    private void alterTime(TextView textView) {
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String hourString="";
            String minString="";
            if(hourOfDay < 10){ hourString = "0"+String.valueOf(hourOfDay);}
            else{hourString = String.valueOf(hourOfDay);}
            if(minute < 10){minString = "0"+String.valueOf(minute);}
            else{minString = String.valueOf(minute);};
            textView.setText(hourString+":"+minString);
            if (textView.getId() == R.id.TextInputEditTextStartTime ) {
                viewModel.liveStartTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            } else {
                viewModel.liveEndTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void setDate(){
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            binding.editTextDate.setText(dayOfMonth + "." + month+1 + "." + year);
            c.set(year,month,dayOfMonth);
            viewModel.liveDate.setValue(c.getTimeInMillis());
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
