package com.hkproductions.listme.guest.creategroupview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentCreateGroupViewBinding
import com.hkproductions.listme.guest.database.GuestDataDao
import com.hkproductions.listme.guest.database.GuestDatabase

class CreateGroupFragment : Fragment() {

    private lateinit var binding: GuestFragmentCreateGroupViewBinding

    private lateinit var adapter: CheckContactAdapter
    private lateinit var database: GuestDataDao
    private lateinit var viewModel: CreateGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.guest_fragment_create_group_view,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        database = GuestDatabase.getInstance(application).guestDataDao

        //init ViewModel
        val viewModelFactory = CreateGroupViewModelFactory(database)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CreateGroupViewModel::class.java)

        //init adapter and fill RecyclerView
        adapter = CheckContactAdapter()
        binding.recyclerviewGroupContacts.adapter = adapter
        viewModel.contacts.observe(viewLifecycleOwner, {
            it?.let {
                adapter.data = it
            }
        })

        //Set text of the first checkbox, which contains phoneOwner
        viewModel.phoneOwner.observe(viewLifecycleOwner, {
            viewModel.phoneOwner.value?.apply {
                binding.checkboxGroupPhoneOwner.text =
                    resources.getString(R.string.full_name, firstName, lastName)
            }
        })

        //add ClickListener to the confirm Button
        //navigate to show group code with ids of the selected contacts
        binding.buttonGroupConfirm.setOnClickListener {
            val list = mutableListOf<Long>()
            //add phoneOwner id if selected
            if (binding.checkboxGroupPhoneOwner.isChecked) {
                viewModel.phoneOwner.value?.guestDataId?.let { it1 -> list.add(it1) }
            }
            //collect ids of the selected contacts
            viewModel.contacts.value?.apply {
                for ((index, value) in withIndex()) {
                    if (adapter.checkedState.get(index)) {
                        list.add(value.guestDataId)
                    }
                }
            }
            this.findNavController()
                .navigate(CreateGroupFragmentDirections.actionShowGroupCode(list.toLongArray()))
        }

        return binding.root
    }

}