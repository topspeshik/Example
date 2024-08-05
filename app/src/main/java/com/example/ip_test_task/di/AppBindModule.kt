package com.example.ip_test_task.di

import android.content.Context
import android.media.MediaPlayer
import com.example.ip_test_task.data.ItemRepositoryImpl
import com.example.ip_test_task.data.LocalDatabase
import com.example.ip_test_task.data.dao.ItemDao
import com.example.ip_test_task.domain.repositories.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppBindModule {


    @Binds
    fun bindItemRepository(authImpl: ItemRepositoryImpl) : ItemRepository

    companion object {



        @Provides
        fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
            return LocalDatabase.getInstance(context)
        }


        @Provides
        fun provideItemDao(database: LocalDatabase): ItemDao {
            return database.itemDao()
        }
    }
}