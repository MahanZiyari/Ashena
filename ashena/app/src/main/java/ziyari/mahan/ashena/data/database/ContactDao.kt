package ziyari.mahan.ashena.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.CONTACTS_TABLE_NAME

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactEntity: ContactEntity)

    @Query("Select * From $CONTACTS_TABLE_NAME")
    fun getAllContacts(): Flow<List<ContactEntity>>
}