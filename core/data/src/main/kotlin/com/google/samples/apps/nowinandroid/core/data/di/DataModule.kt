/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.data.di

import androidx.datastore.core.DataStore
import com.google.samples.apps.nowinandroid.core.data.repository.DefaultRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.DefaultSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstNewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstTopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstUserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.ConnectivityManagerNetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneBroadcastMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.database.di.daosModule
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import com.google.samples.apps.nowinandroid.core.datastore.di.dataStoreModule
import com.google.samples.apps.nowinandroid.core.network.NiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.network.di.coroutineScopesKoinModule
import com.google.samples.apps.nowinandroid.core.notifications.Notifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.core.component.KoinComponent
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
object DataModuleBridgeDagger : KoinComponent {

    @Provides
    fun providesNetworkMonitor() : NetworkMonitor = getKoin().get()

    @Provides
    fun providesTimeZoneMonitor() : TimeZoneMonitor = getKoin().get()

}

//TODO Remove TestDataModule
@Module(includes = [DataModuleBridgeDagger::class])
@InstallIn(SingletonComponent::class)
abstract class DataModule {
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface DataModuleBridgeKoin {
    fun niaNetworkDataSource(): NiaNetworkDataSource
    fun notifier(): Notifier
}

val dataKoinModule = module {
    includes(userNewsResourceRepositoryKoinModule, coroutineScopesKoinModule, daosModule, dataStoreModule)
    singleOf(::ConnectivityManagerNetworkMonitor) bind NetworkMonitor::class
    singleOf(::TimeZoneBroadcastMonitor) bind TimeZoneMonitor::class
    singleOf(::DefaultSearchContentsRepository) bind SearchContentsRepository::class
    singleOf(::DefaultRecentSearchRepository) bind RecentSearchRepository::class
    singleOf(::OfflineFirstNewsRepository) bind NewsRepository::class
    singleOf(::OfflineFirstTopicsRepository) bind TopicsRepository::class
    singleOf(::OfflineFirstUserDataRepository) bind UserDataRepository::class

    single { daggerBridge<DataModuleBridgeKoin>().notifier() }
    single { daggerBridge<DataModuleBridgeKoin>().niaNetworkDataSource() }
}
