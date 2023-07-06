package ziyari.mahan.ashena.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.CONTACTS_TABLE_NAME

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactEntity: ContactEntity)

    @Delete
    suspend fun removeContact(contactEntity: ContactEntity)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContact(contactEntity: ContactEntity)

    @Query("Select * From $CONTACTS_TABLE_NAME")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("Select * From $CONTACTS_TABLE_NAME Where id= :id")
    fun getSingleContact(id: Int): Flow<ContactEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM $CONTACTS_TABLE_NAME WHERE id = :contactID)")
    suspend fun existsContact(contactID: Int): Boolean

    @Query("SELECT * FROM $CONTACTS_TABLE_NAME WHERE firstName LIKE '%' || :searchQuery || '%'  OR lastName LIKE '%' || :searchQuery || '%'")
    fun seaarchContacts(searchQuery: String): Flow<MutableList<ContactEntity>>
}