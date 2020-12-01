package com.hkproductions.listme.guest.editview

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
import com.hkproductions.listme.databinding.GuestFragmentEditViewBinding
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import com.hkproductions.listme.guest.database.GuestDatabase

class EditViewFragment : Fragment() {

    private lateinit var binding: GuestFragmentEditViewBinding

    private lateinit var viewModel: EditViewViewModel

    private lateinit var datasource: GuestDataDao

    private var dataId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.guest_fragment_edit_view,
            container,
            false
        )

        //init var
        val application = requireNotNull(this.activity).application
        datasource = GuestDatabase.getInstance(application).guestDataDao
        dataId = EditViewFragmentArgs.fromBundle(requireArguments()).dataId
        viewModel = initViewModel()


        //give confirm Button a clickListener
        binding.editViewConfirmButton.setOnClickListener { confirmButtonClicked() }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    /**
     * Method that call if the confirmButton is clicked
     * check InputFields and insert or update the data in the database
     */
    private fun confirmButtonClicked() {
        val data = checkInputFields()
        //if data != null -> Inputs are correct
        //else -> Inputs are incorrect
        if (data != null) {
            /*
            if id==-1 -> create
            else -> edit
             */
            if (dataId == -1L) {
                viewModel.insertData(data)
            } else {
                viewModel.updateData(dataId, data)
            }
            Toast.makeText(
                this.context,
                resources.getString(R.string.data_safed),
                Toast.LENGTH_SHORT
            ).show()
        }
        this.findNavController()
            .navigate(EditViewFragmentDirections.actionEditViewFragmentToStartViewFragment())
    }

    /**
     * check the input fields for blank and postal for 5 numbers
     * if all correct -> input or update database
     * else -> error under affected fields
     */
    private fun checkInputFields(): GuestData? {
        var data: GuestData? = GuestData()

        //check firstName not blank
        val firstName = binding.textInputLayoutFirstName.editText?.text.toString()
        if (firstName.isBlank()) {
            binding.textInputLayoutFirstName.error = resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutFirstName.error = ""
            data?.firstName = firstName
        }

        //check lastName not blank
        val lastName = binding.textInputLayoutLastName.editText?.text.toString()
        if (lastName.isBlank()) {
            binding.textInputLayoutLastName.error = resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutLastName.error = ""
            data?.lastName = lastName
        }

        //check postal have 5 numbers and only numbers
        val postal = binding.textInputLayoutPostal.editText?.text.toString()
        if (postal.length != 5 || !postal.matches("[0-9]+".toRegex())) {
            binding.textInputLayoutPostal.error = resources.getString(R.string.postal_error_text)
            data = null
        } else {
            binding.textInputLayoutPostal.error = ""
            data?.postalCode = postal
        }

        //check city not blank
        val city = binding.textInputLayoutCity.editText?.text.toString()
        if (city.isBlank()) {
            binding.textInputLayoutCity.error = resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutCity.error = ""
            data?.city = city
        }

        //check street not blank
        val street = binding.textInputLayoutStreet.editText?.text.toString()
        if (street.isBlank()) {
            binding.textInputLayoutStreet.error = resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutStreet.error = ""
            data?.street = street
        }

        //check houseNumber not blank
        val houseNumber = binding.textInputLayoutHouseNumber.editText?.text.toString()
        if (houseNumber.isBlank()) {
            binding.textInputLayoutHouseNumber.error =
                resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutHouseNumber.error = ""
            data?.houseNumber = houseNumber
        }

        //check phoneNumber not blank
        val phoneNumber = binding.textInputLayoutPhoneNumber.editText?.text.toString()
        if (phoneNumber.isBlank()) {
            binding.textInputLayoutPhoneNumber.error =
                resources.getString(R.string.blank_error_text)
            data = null
        } else {
            binding.textInputLayoutPhoneNumber.error = ""
            data?.phoneNumber = phoneNumber
        }
        return data
    }

    private fun initViewModel(): EditViewViewModel {
        val viewModelFactory = EditViewViewModelFactory(datasource, dataId)
        return ViewModelProvider(this, viewModelFactory).get(EditViewViewModel::class.java)
    }

}
