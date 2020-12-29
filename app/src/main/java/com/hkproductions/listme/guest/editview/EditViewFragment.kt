package com.hkproductions.listme.guest.editview

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestFragmentEditViewBinding
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
    ): View {

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

        //start scan to preload data
        binding.buttonEditGuestScanData.setOnClickListener {
            requestCamera()
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.apply {
                setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX, IntentIntegrator.QR_CODE)
                setPrompt(resources.getString(R.string.scan_guest_header))
                setOrientationLocked(true)
                initiateScan()
            }
        }

        //bind variable
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //request Camera permission
//        requestCamera()

        return binding.root
    }

    /**
     * Request activation of the Camera Permission
     * permission is given -> nothing happens
     * permission is not given -> ask user
     */
    private fun requestCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    0
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    0
                )
            }
        }
    }

    /**
     * Handle the result of the permisson question
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.size == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //Permission is denied
                //TODO Message that camera is necessary
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        /**
         * result result of the code scan
         * contents hold the content of the code
         */
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.scannedCode(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Method that call if the confirmButton is clicked
     * check InputFields and insert or update the data in the database
     */
    private fun confirmButtonClicked() {
        //true -> all entries are correct and not blank
        //false -> some or all entries are incorrect ore blank
        if (checkInputFields()) {
            //dataId -1 -> create
            //else -> edit
            if (dataId == -1L) {
                viewModel.insertData()
            } else {
                viewModel.updateData()
            }

            //Confirmation Message
            Toast.makeText(
                this.context,
                resources.getString(R.string.data_safed),
                Toast.LENGTH_SHORT
            ).show()

            //Hide Keyboard
            val inputMethodManager =
                context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            //Navigate back to start
            this.findNavController()
                .navigate(EditViewFragmentDirections.actionEditViewFragmentToStartViewFragment())
        }
    }

    /**
     * check the input fields for blank and postal for 5 numbers
     * if all correct -> input or update database
     * else -> error under affected fields
     */
    private fun checkInputFields(): Boolean {
        var success = true
        viewModel.liveData.value?.apply {
            //check fistname is not blank
            if (firstName.isBlank()) {
                binding.textInputLayoutFirstName.error =
                    resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutFirstName.error = ""

            //check lastname is not blank
            if (lastName.isBlank()) {
                binding.textInputLayoutLastName.error =
                    resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutLastName.error = ""

            //check postal have 5 numbers
            if (postalCode.length != 5 || !postalCode.matches("[0-9]+".toRegex())) {
                binding.textInputLayoutPostal.error =
                    resources.getString(R.string.postal_error_text)
                success = false
            } else binding.textInputLayoutPostal.error = ""

            //check city is not blank
            if (city.isBlank()) {
                binding.textInputLayoutCity.error = resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutCity.error = ""

            //check street is not blank
            if (street.isBlank()) {
                binding.textInputLayoutStreet.error = resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutStreet.error = ""

            //check houseNumber is not blank
            if (houseNumber.isBlank()) {
                binding.textInputLayoutHouseNumber.error =
                    resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutHouseNumber.error = ""

            //check phoneNumber is not blank
            if (phoneNumber.isBlank()) {
                binding.textInputLayoutPhoneNumber.error =
                    resources.getString(R.string.blank_error_text)
                success = false
            } else binding.textInputLayoutPhoneNumber.error = ""
        }
        return success
    }

    private fun initViewModel(): EditViewViewModel {
        val viewModelFactory = EditViewViewModelFactory(datasource, dataId)
        return ViewModelProvider(this, viewModelFactory).get(EditViewViewModel::class.java)
    }

}
