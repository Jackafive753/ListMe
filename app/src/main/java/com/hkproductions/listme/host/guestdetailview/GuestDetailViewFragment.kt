package com.hkproductions.listme.host.guestdetailview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostFragmentGuestDetailViewBinding
import com.hkproductions.listme.guest.detailview.GuestDetailFragmentArgs
import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.host.database.HostDatabase

class GuestDetailViewFragment : Fragment() {
    private lateinit var binding: HostFragmentGuestDetailViewBinding
    private lateinit var datasource : HostDataDao
    private var dataId : Long = -1L
    private lateinit var viewModel : GuestDetailViewViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState :Bundle?
    ) : View {
        binding= DataBindingUtil.inflate(
            layoutInflater,
            R.layout.host_fragment_guest_detail_view,
            container,false
        )
        val application = requireNotNull(this.activity).application
        dataId = GuestDetailViewFragmentArgs.fromBundle(requireArguments()).dataId
        datasource = HostDatabase.getInstance(application).hostDataDao

        viewModel = initViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }
    private fun initViewModel() : GuestDetailViewViewModel{
        val factory = GuestDetailViewViewModelFactory(datasource,dataId)
        return ViewModelProvider(this,factory).get(GuestDetailViewViewModel::class.java)
    }
}