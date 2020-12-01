package com.hkproductions.listme.guest.detailview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentDetailViewBinding
import com.hkproductions.listme.guest.database.GuestDataDao
import com.hkproductions.listme.guest.database.GuestDatabase

class GuestDetailFragment : Fragment() {
    private lateinit var binding: GuestFragmentDetailViewBinding
    private lateinit var datasource: GuestDataDao
    private var dataId: Long = -1L
    private lateinit var viewModel: GuestDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.guest_fragment_detail_view,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        //Get Arguments
        dataId = GuestDetailFragmentArgs.fromBundle(requireArguments()).dataId
        //Init datasource
        datasource = GuestDatabase.getInstance(application).guestDataDao
        //Init ViewModel
        viewModel = initViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.editButton.setOnClickListener {
            this.findNavController().navigate(
                GuestDetailFragmentDirections.actionGuestDetailFragmentToEditViewFragment(dataId)
            )
        }

        return binding.root
    }

    private fun initViewModel(): GuestDetailViewModel {
        val factory = GuestDetailViewModelFactory(datasource, dataId)
        return ViewModelProvider(this, factory).get(GuestDetailViewModel::class.java)
    }
}