package com.hkproductions.listme.host.startview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Hide Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        viewModel = new ViewModelProvider(
                this,
                new HostStartViewModelFactory(
                        HostDatabase.Companion.getInstance(
                                requireActivity().getApplication()).getHostDataDao()
                )).get(HostStartViewModel.class
        );

        HostStartAdapter adapter = new HostStartAdapter(new CheckoutListener(
                (hostDataids) -> {
                    viewModel.checkout(hostDataids);
                    return null;
                }));
        binding.recyclerViewHostStartViewAreaList.setAdapter(adapter);

        viewModel.getOpenEntries().observe(getViewLifecycleOwner(),
                hostData -> viewModel.actualizeMap());

        viewModel.getCheckedInAreas().observe(getViewLifecycleOwner(), adapter::submitMap);

        binding.buttonHostStartViewEinauschecken.setOnClickListener(event -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX, IntentIntegrator.QR_CODE);
            integrator.setPrompt(getResources().getString(R.string.host_scan_header));
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });

        binding.buttonHostStartViewGaesteliste.setOnClickListener(event -> {
            Navigation.findNavController(getView()).navigate(HostStartViewFragmentDirections.actionShowGuestList());
        });

        viewModel.getNavigateToScanResult().observe(getViewLifecycleOwner(), longs -> {
            //TODO if Max pushed his navigation and the scanresultFragment has an argument then uncomment next line
//            Navigation.findNavController(getView()).navigate(HostStartViewFragmentDirections.actionShowScanResult(longs));
        });

        //To Delete all HostDatas that older than dataLifeSpan
        viewModel.refreshDatabase();

        //DEVELOPERMODE
        binding.buttonHostStartClearHostData.setOnClickListener(event -> {
            viewModel.clearHostData();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                viewModel.scannedCode(result.getContents(), getContext());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
