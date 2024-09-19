package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.LoginViewModel
import features.auth.presentation.ProfileViewModel
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.HomeRepositoryImpl
import features.cashier_role.home.presentation.HomeViewModel
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.data.SalesRepositoryImpl
import features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel
import features.cashier_role.sales.presentation.payment.PaymentViewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import storage.MongoDB

val provideAuthRepositoryModule = module {
    single<AuthRepositoryImpl> {
        AuthRepositoryImpl(
            requestHandler = get(),
            sessionHandler = get()
        )
    }.bind<AuthRepository>()
    factory { LoginViewModel(sessionHandler = get(), authRepository = get()) }
    factory { ProfileViewModel(authRepository = get()) }
}

val provideHomeRepositoryModule = module {
    single { MongoDB() }
    single<HomeRepositoryImpl> { HomeRepositoryImpl(get()) }.bind<HomeRepository>()
    factory { HomeViewModel(homeRepository = get(), mongoDB = get()) }
}

val provideSalesRepositoryModule = module {
    single<SalesRepositoryImpl> {
        SalesRepositoryImpl(
            mongoDB = get(),
            requestHandler = get()
        )
    }.bind<SalesRepository>()
    factory { EntrySalesViewModel(salesRepository = get()) }
    factory { PaymentViewModel(sessionHandler = get(), salesRepository = get()) }
}