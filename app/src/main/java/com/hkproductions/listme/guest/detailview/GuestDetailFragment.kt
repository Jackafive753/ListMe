package com.hkproductions.listme.guest.detailview

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    ): View {
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

        viewModel.deleteButtonVisible.observe(viewLifecycleOwner, {
            if (it) {
                binding.deleteButton.visibility = View.GONE
            } else {
                binding.deleteButton.visibility = View.VISIBLE
            }
        })

        binding.showCodeButton.setOnClickListener {
            this.findNavController()
                .navigate(
                    GuestDetailFragmentDirections
                        .actionShowCode(viewModel.liveData.value!!.guestDataId)
                )
        }

        binding.deleteButton.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Datensatz wirklich löschen?")
            alertDialogBuilder.setTitle("Löschen von Daten")
            alertDialogBuilder.setPositiveButton("Löschen") { dialog, which ->
                Toast.makeText(context, "Daten gelöscht!", Toast.LENGTH_LONG).show()
                viewModel.deleteData()
            }
            alertDialogBuilder.setNegativeButton("Abbrechen") { dialog, which ->
                Toast.makeText(context, "Abgebrochen", Toast.LENGTH_LONG).show()
            }
            alertDialogBuilder.show()

            this.findNavController().navigate(GuestDetailFragmentDirections.actionAfterDelete())
        }

        return binding.root
    }

    private fun initViewModel(): GuestDetailViewModel {
        val factory = GuestDetailViewModelFactory(datasource, dataId)
        return ViewModelProvider(this, factory).get(GuestDetailViewModel::class.java)
    }
}