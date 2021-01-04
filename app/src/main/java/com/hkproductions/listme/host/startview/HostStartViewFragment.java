package com.hkproductions.listme.host.startview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentStartviewBinding;
import com.hkproductions.listme.host.database.HostDatabase;

public class HostStartViewFragment extends Fragment {

    private HostFragmentStartviewBinding binding;
    private HostStartViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.host_fragment_startview,
                container,
                false
        );

        viewModel = new ViewModelProvider(
                this,
                new HostStartViewModelFactory(
                        HostDatabase.Companion.getInstance(
                                requireActivity().getApplication()).getHostDataDao()
                )).get(HostStartViewModel.class
        );

        HostStartAdapter adapter = new HostStartAdapter();
        binding.recyclerViewHostStartViewAreaList.setAdapter(adapter);

        viewModel.getOpenEntries().observe(getViewLifecycleOwner(),
                hostData -> viewModel.actualizeMap());

        viewModel.getCheckedInAreas().observe(getViewLifecycleOwner(), observable -> {
            adapter.submitMap(observable);
        });

        return binding.getRoot();
    }
}
