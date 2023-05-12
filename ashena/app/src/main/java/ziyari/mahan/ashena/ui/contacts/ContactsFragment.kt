package ziyari.mahan.ashena.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding
import ziyari.mahan.ashena.databinding.FragmentContactsBinding
import ziyari.mahan.ashena.ui.addcontacts.AddContactsFragment
import ziyari.mahan.ashena.utils.Adapters.ContactAdapter
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
    private val viewModel: ContactHomeScreenViewModel by viewModels()

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
            // Getting Contacts in Db
            viewModel.getAllContactsFromDb()
            viewModel.allContacts.observe(viewLifecycleOwner) {
                contactAdapter.setData(it.data!!)
                contacts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = contactAdapter
                }
            }
            // Fab
            addContactFab.setOnClickListener {
                AddContactsFragment().show(parentFragmentManager, AddContactsFragment().tag)
            }
        }
    }
}