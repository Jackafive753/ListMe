package com.hkproductions.listme.host.guestlist;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentGuestListBinding;
import com.hkproductions.listme.host.database.HostData;
import com.hkproductions.listme.host.database.HostDataDao;
import com.hkproductions.listme.host.database.HostDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuestListFragment extends Fragment {

    private HostFragmentGuestListBinding binding;
    private GuestListViewModel viewModel;
    private HostDataDao datasource;

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

        //initialize datepicker
        binding.imageButtonDatePicker.setOnClickListener(l -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                binding.editTextDate.setText(dayOfMonth + "-" + month + "-" + year);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                viewModel.liveDate.setValue(calendar.getTimeInMillis());
            }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });
        //initialize timepicker startTime
        binding.editTextTimeStart.setOnClickListener(l -> {
            alterTime(binding.editTextTimeStart);
        });


        //initialize timepicker endTime
        binding.editTextTimeEnd.setOnClickListener(l -> {
            alterTime(binding.editTextTimeEnd);
        });
        //TODO:: MAKE IMAGEBUTTON, TEXTVIEWS as big as TEXTINPUTLAYOUT with DIMENSIONS hardcoded

        viewModel.liveName.observe(getViewLifecycleOwner(), s -> viewModel.alterList());
        viewModel.liveDate.observe(getViewLifecycleOwner(), observer);
        viewModel.liveStartTime.observe(getViewLifecycleOwner(), observer);
        viewModel.liveEndTime.observe(getViewLifecycleOwner(), observer);


        viewModel.data.observe(getViewLifecycleOwner(), hostData -> guestAdapter.submitList(hostData));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Utility Method alter Time
     *
     * @param textView Used for altering the textViews displaying the Time
     *                 Uses TimepickerDialog
     */
    private void alterTime(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            textView.setText(hourOfDay + ":" + minute);
            if (textView.getId() == R.id.editTextTimeStart) {
                viewModel.liveStartTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            } else {
                viewModel.liveEndTime.setValue((((long) hourOfDay) * 3600000 + ((long) minute) * 6000));
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }


}
