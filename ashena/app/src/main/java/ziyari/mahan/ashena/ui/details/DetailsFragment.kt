package ziyari.mahan.ashena.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.models.Group
import ziyari.mahan.ashena.databinding.FragmentDetailsBinding
import ziyari.mahan.ashena.utils.setUpListWithAdapter
import ziyari.mahan.ashena.viewmodel.DetailsViewModel


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    // Binding
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    // ViewModel
    private val viewModel: DetailsViewModel by viewModels()
    // Other
    private val args: DetailsFragmentArgs by navArgs()
    private  var contact: ContactEntity = ContactEntity()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            // SettingUp Spinner
            val passedId = args.contactId ?: 0
            viewModel.getContactFromDatabaseWith(passedId)
            viewModel.contact.observe(viewLifecycleOwner) {
                contact = it ?: ContactEntity()
                fillFieldsWithContactInfo()
            }

            //Inflating Menu
            HandleToolbar()

        }
    }

    private fun FragmentDetailsBinding.HandleToolbar() {
        detailsToolbar.apply {
            inflateMenu(R.menu.details_toolbar_menu)
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.fav_icon -> {
                        TODO("making Contact Fav and change fav icon appearance")
                        true
                    }

                    R.id.save_icon -> {
                        //TODO("Save Contact and return to main screen")
                        //TODO show a snackbar for successfull operation
                        true
                    }

                    R.id.trash_icon -> {
                        showDeleteConfirmationWarning()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun fillFieldsWithContactInfo() {
        binding?.apply {
            firstnameTextField.setText(contact.firstName)
            lastnameTextField.setText(contact.lastName)
            contactsPictureProfile.load(contact.profilePicture)
            phoneNumberTextField.setText(contact.number)
            val groups = mutableListOf(Group.FAMILY.name, Group.WORK.name, Group.COSTUMERS.name, Group.FRIENDS.name)
            val pos = groups.indexOf(contact.group)
            detailsGroupsSpinner.setUpListWithAdapter(groups) {
                contact.group = it
            }
            detailsGroupsSpinner.setSelection(pos)

        }
    }


    private fun showDeleteConfirmationWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Moving to Trash?")
            .setMessage(resources.getString(R.string.delete_warning))
            .setNegativeButton("Cancel") { dialog, which ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton("Move to Trash") { dialog, which ->
                // Respond to positive button press
                viewModel.removeContact(contact)
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }
}