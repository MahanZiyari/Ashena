package ziyari.mahan.ashena.utils.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.request.ErrorResult
import coil.request.ImageRequest
import io.getstream.avatarview.coil.loadImage
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.databinding.ContactItemBinding
import ziyari.mahan.ashena.utils.generateRandomBlue
import ziyari.mahan.ashena.utils.generateRandomColor
import javax.inject.Inject

class ContactAdapter @Inject constructor() : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private lateinit var binding: ContactItemBinding
    private lateinit var context: Context
    private var contactEntityList = emptyList<ContactEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactEntityList[position])
        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = contactEntityList.size

    private lateinit var onItemClickListener: (ContactEntity) -> Unit

    fun setOnItemClickListener(onItemClickListener: (ContactEntity) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ContactEntity) {
            binding.apply {
                contactPic.loadImage(
                    data = item.profilePicture.toUri(),
                    onError = {request: ImageRequest, result: ErrorResult ->
                        contactPic.avatarInitials = item.firstName + item.lastName
                    }
                )
                contactName.text = item.firstName + " " + item.lastName
                contactItemLayout.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }
    }

    fun setData(data: List<ContactEntity>) {
        val contactsDiffUtil = ContactsDiffUtils(contactEntityList, data)
        val diffUtils = DiffUtil.calculateDiff(contactsDiffUtil)
        contactEntityList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class ContactsDiffUtils(
        private val oldItem: List<ContactEntity>,
        private val newItem: List<ContactEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}