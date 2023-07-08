package ziyari.mahan.ashena.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FavoriteContactItemBinding
import ziyari.mahan.ashena.databinding.FragmentContactsBinding
import ziyari.mahan.ashena.databinding.FragmentFavoritesBinding
import ziyari.mahan.ashena.ui.contacts.ContactsFragmentDirections
import ziyari.mahan.ashena.utils.Adapters.FavoritesContactAdapter
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.viewmodel.FavoritesViewModel
import ziyari.mahan.ashena.viewmodel.SharedViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    val binding get() = _binding

    //ViewModels
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: FavoritesViewModel by viewModels()
    //Adapter
    @Inject
    lateinit var favoritesContactAdapter: FavoritesContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            viewModel.getAllFavoritesContacts()
            viewModel.favContacts.observe(viewLifecycleOwner) {
                showEmptyLayout(it.isEmpty)
                favoritesContactAdapter.setData(it.data!!)
                favContacts.apply {
                    layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
                    adapter = favoritesContactAdapter
                }
            }

            favoritesContactAdapter.setOnItemClickListener { contactEntity ->
                val direction = ContactsFragmentDirections.actionToDetails(contactEntity.id, contactEntity.isFromPhone)
                findNavController().navigate(direction)
            }
        }
    }

    private fun FragmentFavoritesBinding.showEmptyLayout(isEmpty: Boolean) {
        if (isEmpty) {
            favContacts.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            favContacts.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
}