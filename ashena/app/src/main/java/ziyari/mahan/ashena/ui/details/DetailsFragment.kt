package ziyari.mahan.ashena.ui.details

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.models.Group
import ziyari.mahan.ashena.databinding.FragmentDetailsBinding
import ziyari.mahan.ashena.utils.PermissionsManager
import ziyari.mahan.ashena.utils.generateRandomColor
import ziyari.mahan.ashena.utils.setUpListWithAdapter
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.viewmodel.DetailsViewModel
import ziyari.mahan.ashena.viewmodel.SharedViewModel
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    // Binding
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding

    // ViewModel
    private val viewModel: DetailsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Other
    @Inject
    lateinit var permissionsManager: PermissionsManager
    private val args: DetailsFragmentArgs by navArgs()
    private var contact: ContactEntity = ContactEntity()




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
            requestNeededPermissions()
            val passedId = args.contactId
            val isContactFromPhone = args.isContactFromPhone
            if (isContactFromPhone) {
                viewModel.getContactFromPhone(passedId)
            } else {
                viewModel.getContactFromDatabaseWith(passedId)
            }

            viewModel.contact.observe(viewLifecycleOwner) {
                contact = it ?: ContactEntity()
                showDebugLog("Email: ${contact.email}")
                initializeFavoritesStatus()
                fillFieldsWithContactInfo()
            }

            //Inflating Menu
            handleToolbar()
            // Stating Favorites icon

            phoneCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                val numberToPassByIntent = Uri.parse("tel:${contact.number}")
                intent.setData(numberToPassByIntent)
                startActivity(intent)
            }


            message.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setData(Uri.parse("smsto:${contact.number}"))
                startActivity(intent)
            }




        }
    }

    private fun FragmentDetailsBinding.initializeFavoritesStatus() {
        showDebugLog("inside fav icon: ${contact.favorites}")
        if (contact.favorites) {
            favContact.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_24))
            favContact.imageTintList =
                ColorStateList.valueOf(resources.getColor(R.color.yellow))
        } else {
            favContact.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_outline_24))
            favContact.imageTintList =
                ColorStateList.valueOf(resources.getColor(R.color.deepKoamaru))
        }

        favContact.setOnClickListener {
            contact.favorites = !contact.favorites
            if (contact.favorites) {
                favContact.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_24))
                favContact.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow))
            } else {
                favContact.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_outline_24))
                favContact.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.deepKoamaru))
            }
        }
    }

    private fun FragmentDetailsBinding.handleToolbar() {
        detailsToolbar.apply {
            inflateMenu(R.menu.details_toolbar_menu)
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.save_icon -> {
                        val newFirstName = firstnameTextField.text.toString()
                        val newLastName = lastnameTextField.text.toString()
                        val newPhoneNumber = phoneNumberTextField.text.toString()
                        val newPicture = resources.getString(
                            R.string.avatar_api,
                            newFirstName,
                            generateRandomColor()
                        )
                        contact.firstName = newFirstName
                        contact.lastName = newLastName
                        contact.number = newPhoneNumber
                        contact.profilePicture = newPicture

                        var updateResult: Boolean
                        if (contact.isFromPhone) {
                            // Save to Phone
                            updateResult = viewModel.updatePhoneContact(contact)
                        } else {
                            // Save to DB
                            viewModel.updateDatabaseContact(contact)
                            updateResult = true
                        }

                        if (updateResult)
                            Snackbar.make(requireView(), "Contact Updated Successfully", Snackbar.LENGTH_SHORT).show()
                        else
                            Snackbar.make(requireView(), "Ooops something happened", Snackbar.LENGTH_SHORT).show()
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
            emailTextField.setText(contact.email)
        }
    }


    private fun showDeleteConfirmationWarning() {
        var deleteResult = false
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Moving to Trash?")
            .setMessage(resources.getString(R.string.delete_warning))
            .setNegativeButton("Cancel") { dialog, which ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton("Move to Trash") { dialog, which ->
                // Respond to positive button press
                if (contact.isFromPhone) {
                    deleteResult = viewModel.removeContactFromDevice(contact.id.toLong())
                } else {
                    viewModel.removeContact(contact)
                }
                if (deleteResult) {
                    dialog.dismiss()
                    sharedViewModel.showSnackbar("${contact.firstName} Deleted Successfully")
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "Error: ", Toast.LENGTH_SHORT).show()
                }
            }.show()
    }

    private fun requestNeededPermissions() {
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_CONTACTS)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(
                        requireContext(),
                        "All permissions are granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}