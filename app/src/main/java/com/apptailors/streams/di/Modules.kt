package com.apptailors.streams.di

import android.content.Context
import android.net.ConnectivityManager
import com.apptailors.streams.BuildConfig
import com.apptailors.streams.data.network.ApptailorsApi
import com.apptailors.streams.data.network.ConnectionManagerImpl
import com.apptailors.streams.data.network.ResponseConverterFactory
import com.apptailors.streams.data.repository.Repository
import com.apptailors.streams.ui.stream.StreamViewModel
import com.apptailors.streams.ui.streamList.StreamListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppModules : ModuleLoader() {
    override val modules: List<Module> =
        listOf(
            viewModelModule,
            repositoryModule,
            networkModule
        )
}

private val viewModelModule = module {
    viewModel { StreamViewModel(get()) }
    viewModel { StreamListViewModel(get()) }
}

private val repositoryModule = module {
    single { Repository(get()) }
}

private val networkModule = module {
    single { Retrofit.Builder()
        .addConverterFactory(ResponseConverterFactory)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(get())
        .build()
        .create(ApptailorsApi::class.java)
    }

    single { OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()
    }

    single { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    single { ConnectionManagerImpl(get()) }
}
