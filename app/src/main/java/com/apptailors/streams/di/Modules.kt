package com.apptailors.streams.di

import com.apptailors.streams.data.repository.Repository
import com.apptailors.streams.ui.stream.StreamViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules : ModuleLoader() {
    override val modules: List<Module> =
        listOf(
            viewModelModule,
            repositoryModule
        )
}

private val viewModelModule = module {
    viewModel { StreamViewModel(get()) }
}

private val repositoryModule = module {
    single { Repository() }
}
