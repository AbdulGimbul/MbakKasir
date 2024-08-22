package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.LoginViewModel
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.HomeRepositoryImpl
import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.presentation.HomeViewModel
import network.RequestHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val provideAuthRepositoryModule = module {
    single { RequestHandler(get()) }
    single<AuthRepositoryImpl> { AuthRepositoryImpl(get()) }.bind<AuthRepository>()
    factory { LoginViewModel(sessionHandler = get(), authRepository = get()) }
}

val provideHomeRepositoryModule = module {
    single { MongoDB() }
    single<HomeRepositoryImpl> { HomeRepositoryImpl(get()) }.bind<HomeRepository>()
    factory { HomeViewModel(homeRepository = get(), mongoDB = get()) }
}