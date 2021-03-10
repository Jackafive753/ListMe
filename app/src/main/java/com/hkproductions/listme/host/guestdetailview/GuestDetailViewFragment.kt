package com.hkproductions.listme.host.guestdetailview

import android.annotation.SuppressLint
import android.database.Observable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostFragmentGuestDetailViewBinding
import com.hkproductions.listme.guest.detailview.GuestDetailFragmentArgs
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.host.database.HostDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat

class GuestDetailViewFragment : Fragment() {
    private lateinit var binding: HostFragmentGuestDetailViewBinding
    private lateinit var datasource: HostDataDao
    private var dataId: Long = -1L
    private lateinit var viewModel: GuestDetailViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.host_fragment_guest_detail_view,
            container, false
        )
        val application = requireNotNull(this.activity).application
        dataId = GuestDetailViewFragmentArgs.fromBundle(requireArguments()).dataId
        datasource = HostDatabase.getInstance(application).hostDataDao

        viewModel = initViewModel()










        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.liveData.observe(viewLifecycleOwner, {
            val df = SimpleDateFormat("dd//MM//yy").format(it.startTimeMilli)
            binding.textViewDate.text = df
            val dfStart = SimpleDateFormat("HH:mm").format(it.startTimeMilli)
            val dfEnd = SimpleDateFormat("HH:mm").format(it.endTimeMilli)
            binding.textViewTimeFrame.text = "$dfStart - $dfEnd"
            binding.textViewArea.text = if (it.areaName.isNullOrBlank()) {
                resources.getString(R.string.scannedEntrys_noArea)
            } else {
                it.areaName
            }
        })
    }

    private fun initViewModel(): GuestDetailViewViewModel {
        val factory = GuestDetailViewViewModelFactory(datasource, dataId)
        return ViewModelProvider(this, factory).get(GuestDetailViewViewModel::class.java)
    }
}