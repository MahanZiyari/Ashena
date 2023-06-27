package ziyari.mahan.ashena.ui.addcontacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding
import ziyari.mahan.ashena.utils.checkForEmptyString
import ziyari.mahan.ashena.utils.generateRandomColor
import ziyari.mahan.ashena.utils.setUpListWithAdapter
import ziyari.mahan.ashena.viewmodel.AddContactViewModel
import javax.inject.Inject


@AndroidEntryPoint
class AddContactsFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding

    //Other
    private val viewModel: AddContactViewModel by viewModels()
    private var groups = mutableListOf<String>()
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
            // Groups Spinner
            viewModel.loadGroups()
            viewModel.groups.observe(viewLifecycleOwner) {
                groups.addAll(it)
                groupsSpinner.setUpListWithAdapter(groups) { selectedItem ->
                    contactEntity.group = selectedItem
                }
            }
            // Save button
            saveContact.setOnClickListener {
                contactEntity.id = 0
                val firstName = firstNameEdt.text.toString()
                val lastName = lastNameEdt.text.toString()
                val number = phoneNumber.text.toString()
                contactEntity.firstName = firstName
                contactEntity.lastName = lastName
                contactEntity.number = number
//                contactEntity.profilePicture = context?.getString(R.string.avatar_api, firstName)!!
                contactEntity.profilePicture = "https://api.dicebear.com/6.x/initials/png?seed=$firstName&backgroundColor=${generateRandomColor()}"
                if (checkForEmptyString(firstName, lastName, number)) {
                    viewModel.addContactToDatabase(contactEntity)
                    dismiss()
                } else {
                    Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}