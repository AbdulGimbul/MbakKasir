package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.LoginViewModel
import network.RequestHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val provideRepositoryModule = module {
    single { RequestHandler(get()) }
    single<AuthRepositoryImpl> { AuthRepositoryImpl(get()) }.bind<AuthRepository>()
    factory { LoginViewModel(sessionHandler = get(), authRepository = get()) }
}