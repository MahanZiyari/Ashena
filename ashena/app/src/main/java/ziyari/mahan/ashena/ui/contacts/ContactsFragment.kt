package ziyari.mahan.ashena.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding
import ziyari.mahan.ashena.databinding.FragmentContactsBinding
import ziyari.mahan.ashena.ui.addcontacts.AddContactsFragment
import ziyari.mahan.ashena.utils.Adapters.ContactAdapter
import ziyari.mahan.ashena.utils.DEBUG_TAG
import ziyari.mahan.ashena.utils.PermissionsManager
import ziyari.mahan.ashena.viewmodel.ContactHomeScreenViewModel
import javax.inject.Inject


@AndroidEntryPoint
class ContactsFragment : Fragment() {

    // Binding
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding

    //Other
    @Inject
    lateinit var contactAdapter: ContactAdapter

    @Inject
    lateinit var permissionsManager: PermissionsManager
    private val viewModel: ContactHomeScreenViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.i(DEBUG_TAG, "permission status: $isGranted")
        Toast.makeText(requireContext(), "Permission Status: $isGranted", Toast.LENGTH_SHORT).show()
        if (isGranted) viewModel.getAllContacts() else viewModel.getAllContactsFromDb()
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            requestPermission()

            viewModel.allContacts.observe(viewLifecycleOwner) {
                contactAdapter.setData(it.data!!)
                contacts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = contactAdapter
                }
            }
            contactAdapter.setOnItemClickListener { contactEntity ->
                val direction = ContactsFragmentDirections.actionToDetails(contactEntity.id, contactEntity.isFromPhone)
                findNavController().navigate(direction)
            }
            // Fab
            addContactFab.setOnClickListener {
                AddContactsFragment().show(parentFragmentManager, AddContactsFragment().tag)
            }
        }
    }

    private fun requestPermission() {
        when {
            permissionsManager.hasReadContactsPermission() -> {
                Log.i(DEBUG_TAG, "permission is already granted")
                viewModel.getAllContacts()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_CONTACTS
            ) -> {
                // Additional rationale should be displayed
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Why we need access to your contacts?")
                        .setMessage("Grant that fucking access you piss of shit!!!")
                        .setNeutralButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()

                requestPermissionLauncher
                        .launch(Manifest.permission.READ_CONTACTS)
            }

            else -> {
                //requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                //permission has not been asked yet
            }
        }
    }

}