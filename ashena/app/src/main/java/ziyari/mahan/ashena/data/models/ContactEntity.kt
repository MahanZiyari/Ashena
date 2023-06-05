package ziyari.mahan.ashena.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ziyari.mahan.ashena.utils.CONTACTS_TABLE_NAME

@Entity(tableName = CONTACTS_TABLE_NAME)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var firstName: String = "",
    var lastName: String = "",
    var number: String = "",
    var group: String = Group.FAMILY.name,
    var favorites: Boolean = false,
    var profilePicture: String = ""
)

enum class Group {
    WORK,
    FAMILY,
    FRIENDS,
    COSTUMERS
}