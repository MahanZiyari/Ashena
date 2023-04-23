package ziyari.mahan.ashena.ui.addcontacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.databinding.FragmentAddContactsBinding


class AddContactsFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding : FragmentAddContactsBinding? = null
    private val binding get() = _binding

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
        }
    }


}