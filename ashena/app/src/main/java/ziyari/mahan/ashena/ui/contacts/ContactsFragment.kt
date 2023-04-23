package ziyari.mahan.ashena.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding
import ziyari.mahan.ashena.databinding.FragmentContactsBinding
import ziyari.mahan.ashena.ui.addcontacts.AddContactsFragment


class ContactsFragment : Fragment() {

    // Binding
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding

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
            // Fab
            addContactFab.setOnClickListener {
                AddContactsFragment().show(parentFragmentManager, AddContactsFragment().tag)
            }
        }
    }
}