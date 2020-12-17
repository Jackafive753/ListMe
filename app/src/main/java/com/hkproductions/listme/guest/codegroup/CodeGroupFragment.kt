package com.hkproductions.listme.guest.codegroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentGroupCodeViewBinding
import com.hkproductions.listme.guest.database.GuestDatabase

class CodeGroupFragment : Fragment() {

    private lateinit var viewModel: CodeGroupViewModel
    private lateinit var binding: GuestFragmentGroupCodeViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.guest_fragment_group_code_view,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val database = GuestDatabase.getInstance(application).guestDataDao

        viewModel = ViewModelProvider(
            this,
            CodeGroupViewModelFactory(
                database,
                CodeGroupFragmentArgs.fromBundle(requireArguments()).dataIds
            )
        ).get(CodeGroupViewModel::class.java)

        val adapter = GroupMemberAdapter()
        binding.recyclerviewGroupcodeGroupmember.adapter = adapter
        viewModel.groupMembers.observe(viewLifecycleOwner, {
            it?.let {
                adapter.data = it
            }
        })

        viewModel.code.observe(viewLifecycleOwner, {
            it.let {
                binding.imageviewGroupcodeCode.setImageBitmap(it)
            }
        })

        return binding.root
    }

}