package ziyari.mahan.ashena.ui.contacts

import android.Manifest
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FragmentContactsBinding
import ziyari.mahan.ashena.ui.addcontacts.AddContactsFragment
import ziyari.mahan.ashena.utils.Adapters.ContactAdapter
import ziyari.mahan.ashena.utils.PermissionsManager
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.viewmodel.ContactHomeScreenViewModel
import ziyari.mahan.ashena.viewmodel.SharedViewModel
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
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requestToAccessContacts()
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
            viewModel.allContacts.observe(viewLifecycleOwner) {
                contactAdapter.setData(it.data!!)
                contacts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = contactAdapter
                }
            }



            sharedViewModel.snackbarMessage.observe(viewLifecycleOwner) {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_toolbar_menu, menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchForContacts(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }


    private fun requestToAccessContacts() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
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
                    showDebugLog("All Permission are Already Granted")
                    viewModel.getAllContacts()
                } else {
                    viewModel.getAllContactsFromDb()
                    showDebugLog("These permissions are denied: $deniedList")
                    showDebugLog("These permissions are granted: $grantedList")
                }
            }
    }

}