package ziyari.mahan.ashena.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ziyari.mahan.ashena.utils.CONTACTS_TABLE_NAME

@Entity(tableName = CONTACTS_TABLE_NAME)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var lookupKey: String? = null,
    var firstName: String = "",
    var lastName: String = "",
    var number: String = "",
    var email: String = "",
    var favorites: Boolean = false,
    var profilePicture: String = "",
    var isFromPhone: Boolean = false
)

enum class Group {
    WORK,
    FAMILY,
    FRIENDS,
    COSTUMERS
}
