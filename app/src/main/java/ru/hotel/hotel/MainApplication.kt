package ru.hotel.hotel

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.hotel.hotel.ui.screens.authenticated.profile.ProfileScreenViewModel
import ru.hotel.hotel.ui.screens.authenticated.rent.RentViewModel
import ru.hotel.hotel.ui.screens.authenticated.rooms.RoomsViewModel
import ru.hotel.hotel.ui.screens.unauthenticated.login.LoginViewModel
import ru.hotel.hotel.ui.screens.unauthenticated.registration.RegistrationViewModel
import ru.ktor_koin.network.KtorClient
import ru.ktor_koin.network.KtorClientImpl
import ru.ktor_koin.repositories.LoginRepository
import ru.ktor_koin.repositories.LoginRepositoryImpl
import ru.ktor_koin.repositories.RoomsRepository
import ru.ktor_koin.repositories.RoomsRepositoryImpl

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val unAuthModule = module {

            single<KtorClient> { KtorClientImpl() }

            single<LoginRepository> { LoginRepositoryImpl(get()) }
            single<RoomsRepository> { RoomsRepositoryImpl(get()) }

            viewModel { LoginViewModel(get()) }
            viewModel { RegistrationViewModel(get()) }
            viewModel { RoomsViewModel(get()) }
            viewModel { RentViewModel(get()) }
            viewModel { ProfileScreenViewModel() }
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(unAuthModule)
        }
    }
}