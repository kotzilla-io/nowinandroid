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

package com.google.samples.apps.nowinandroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.samples.apps.nowinandroid.core.data.model.Issue
import com.google.samples.apps.nowinandroid.core.data.model.Issues.blockForIssue
import com.google.samples.apps.nowinandroid.di.appModule
import com.google.samples.apps.nowinandroid.sync.initializers.Sync
import com.google.samples.apps.nowinandroid.util.ProfileVerifierLogger
import io.kotzilla.sdk.KotzillaSDK
import io.kotzilla.sdk.analytics.koin.analytics
import io.kotzilla.sdk.config.Environment.Staging
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import kotlin.random.Random

/**
 * [Application] class for NiA
 */
//@HiltAndroidApp
@OptIn(KoinExperimentalAPI::class)
class NiaApplication : Application(), ImageLoaderFactory {


    val imageLoader: ImageLoader by inject()
    val profileVerifierLogger: ProfileVerifierLogger by inject()

    override fun onCreate() {
        super.onCreate()

        // Android 2 steps setup with analyticsLogger()
//        KotzillaSDK.setup(this){
//            setEnvironment(Staging)
//            onConfig {
//                refreshRate = 15_000L
//                useDebugLogs = true
//            }
//        }

//        // KMP Setup 2 steps setup with analyticsLogger()
//        KotzillaCoreSDK
//            .setup("ktz-sdk-oIcm7l3wm76xdFcjJxaFOLKL9FothX-ml2KF4cdEpBA","1.0-KMP")
//            .setEnvironment(Staging)
//            .onConfig {
//                refreshRate = 15_000L
//                useDebugLogs = true
//            }
//            .attachKoin()
//            .connect()

//        // SDK Setup - need analyticsLogger(sdkInstance = sdk)
//        val sdk = KotzillaCoreSDK
//            .setup(apiKey(),getVersionName()) // apiKey(),getVersionName() in Android else manual for now
//            .setEnvironment(Staging)
//            .onConfig {
//                refreshRate = 15_000L
//                useDebugLogs = true
//            }
//            .connect()

        startKoin {
            androidContext(this@NiaApplication)

            // one-line setup Android
//            analytics()

            // one-line setup KMP
//            analytics {
//                // in KMP
//                setApiKey()
//                setVersion()
//            }

            // internal dev
            analytics {
                setEnvironment(Staging)
                onConfig {
                    refreshRate = 15_000L
                    useDebugLogs = true
                }
            }
            modules(appModule)
            workManagerFactory()
        }

        // Initialize Sync; the system responsible for keeping data in the app up to date.
//        KotzillaSDK.log("let's fire an issue")

        // Create business marker
//        KotzillaSDK.createIssue("Custom Business Issue","Issue detected at start!")

        KotzillaSDK.log("event: null trnsition from alert_analysis to alert_analysis")

        Sync.initialize(context = this)
        profileVerifierLogger()

        blockForIssue(Issue.STARTUP_TIME)

        // Random crash
        runBlocking {
            val randomCrash = Random.nextInt(3)
            val hasCrash = randomCrash == 1
            val randomDelay = Random.nextLong(500)
            KotzillaSDK.setProperties(
                "has_crash" to hasCrash,
                "crash_delay" to randomDelay
            )

            if (hasCrash) {
                KotzillaSDK.log("let's crash ...")
                delay(randomDelay)
                error("LET'S CRASH!")
            }
        }
    }

    override fun newImageLoader(): ImageLoader = imageLoader
}
