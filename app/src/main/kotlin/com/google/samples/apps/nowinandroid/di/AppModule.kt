/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.di

import com.google.samples.apps.nowinandroid.core.analytics.analyticsKoinModule
import com.google.samples.apps.nowinandroid.core.data.di.dataKoinModule
import com.google.samples.apps.nowinandroid.core.network.di.coroutineScopesKoinModule
import com.google.samples.apps.nowinandroid.feature.bookmarks.bookmarksModule
import com.google.samples.apps.nowinandroid.feature.foryou.forYouModule
import com.google.samples.apps.nowinandroid.feature.interests.interestModule
import com.google.samples.apps.nowinandroid.feature.search.searchModule
import com.google.samples.apps.nowinandroid.feature.topic.topicModule
import com.google.samples.apps.nowinandroid.sync.di.syncKoinModule
import org.koin.dsl.module

val appModule = module {
    includes(dataKoinModule, analyticsKoinModule, jankStatsKoinModule, syncKoinModule, forYouModule, interestModule, topicModule, bookmarksModule, searchModule)
}