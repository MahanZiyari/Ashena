package ziyari.mahan.ashena.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ziyari.mahan.ashena.utils.CONTACTS_TABLE_NAME

@Entity(tableName = CONTACTS_TABLE_NAME)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val number: String = "",
    val group: Group = Group.FAMILY,
    val favorites: Boolean = false
)

enum class Group {
    WORK,
    FAMILY,
    FRIENDS,
    COSTUMERS
}
