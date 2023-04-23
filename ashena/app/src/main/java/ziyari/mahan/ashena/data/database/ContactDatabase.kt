package ziyari.mahan.ashena.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ziyari.mahan.ashena.data.models.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}