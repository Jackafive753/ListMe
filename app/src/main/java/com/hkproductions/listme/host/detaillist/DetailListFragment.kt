package com.hkproductions.listme.host.detaillist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostFragmentDetailListBinding
import com.hkproductions.listme.host.database.HostDatabase

class DetailListFragment : Fragment() {
    private lateinit var binding: HostFragmentDetailListBinding
    private lateinit var viewModel: DetailListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.host_fragment_detail_list,
            container,
            false
        )

        viewModel = ViewModelProvider(
            this, DetailListViewModelFactory(
                database = HostDatabase.getInstance(requireActivity().application).hostDataDao,
                hostDataId = DetailListFragmentArgs.fromBundle(requireArguments()).hostDataId
            )
        ).get(DetailListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DetailListAdapter(resources)
        binding.recyclerviewContactPersonOne.adapter = adapter

        viewModel.dataOne.observe(viewLifecycleOwner) {
            adapter.createList(it, viewModel.dataTwo.value)
        }
        viewModel.dataTwo.observe(viewLifecycleOwner) {
            adapter.createList(viewModel.dataOne.value, it)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.host_detaillist_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.detailListInfo) {
            AlertDialog.Builder(requireContext()).setTitle(R.string.detailList_dialog_title)
                .setMessage(R.string.detailList_dialog_message)
                .create().show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}