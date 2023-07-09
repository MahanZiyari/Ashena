package ziyari.mahan.ashena.ui.addcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding
import ziyari.mahan.ashena.utils.PermissionsManager
import ziyari.mahan.ashena.utils.checkForEmptyString
import ziyari.mahan.ashena.viewmodel.AddContactViewModel
import javax.inject.Inject


@AndroidEntryPoint
class AddContactsFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var permissionsManager: PermissionsManager

    //Binding
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding

    //Other
    private val viewModel: AddContactViewModel by viewModels()
    @Inject lateinit var contactEntity: ContactEntity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactsBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            closeImg.setOnClickListener {
                dismiss()
            }

            // Save button
            saveContact.setOnClickListener {
                /*if (permissionsManager.hasReadContactsPermissionGranted()) {
                    // showing dialogs and asking for destination
                    showDialogsToChooseSaveLocation()
                } else {
                    saveToDatabase()
                }*/
                saveToDatabase()
            }
        }
    }

    private fun saveToDatabase() {
        if (fillContactProperties()) {
            viewModel.addContactToDatabase(contactEntity)
            dismiss()
        } else {
            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillContactProperties(): Boolean {
        binding?.apply {
            contactEntity.id = 0
            contactEntity.isFromPhone = false
            val firstName = firstnameTextField.text.toString()
            val lastName = lastnameTextField.text.toString()
            val number = phoneNumberTextField.text.toString()
            val email = emailTextField.text.toString()
            contactEntity.firstName = firstName
            contactEntity.lastName = lastName
            contactEntity.number = number
            contactEntity.email = email
            return checkForEmptyString(firstName, lastName, number)
        }
        return false
    }

    private fun FragmentAddContactsBinding.showDialogsToChooseSaveLocation() {
        val singleItems = arrayOf("Application Storage", "Device")
        var checkedItem = 1
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose where to save contact.")
            .setNeutralButton("Never mind") { dialog, which ->
                // Respond to neutral button press
                dialog.dismiss()
            }
            .setPositiveButton("Done") { dialog, which ->
                // Respond to positive button press
                when(checkedItem) {
                    0 -> {
                        saveToDatabase()
                        dialog.dismiss()
                        dismiss()
                    }
                    1 -> {
                        if (fillContactProperties()) {
                            viewModel.insertContactToDevice(contactEntity)
                            dialog.dismiss()
                            dismiss()
                        } else {
                            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                // Respond to item chosen
                checkedItem = which
            }
            .show()
    }


}