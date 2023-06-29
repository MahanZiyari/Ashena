package ziyari.mahan.ashena.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ziyari.mahan.ashena.data.models.ContactEntity

@Database(entities = [ContactEntity::class], version = 4, exportSchema = false)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}