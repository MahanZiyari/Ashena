package ziyari.mahan.ashena.utils.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ziyari.mahan.ashena.data.database.ContactDatabase
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.CONTACTS_DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideContact(): ContactEntity = ContactEntity()


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = ContactDatabase::class.java,
            name = CONTACTS_DB_NAME
        ).fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideContactDao(database: ContactDatabase) = database.contactDao()
}