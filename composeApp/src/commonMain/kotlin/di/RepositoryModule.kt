package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

//val provideRepositoryModule = module {
//    single<AuthRepositoryImpl> { AuthRepositoryImpl(get()) }.bind<AuthRepository>()
//}