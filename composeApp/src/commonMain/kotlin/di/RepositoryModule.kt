package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.profile.ProfileViewModel
import features.cashier_role.history.data.HistoryRepository
import features.cashier_role.history.data.HistoryRepositoryImpl
import features.cashier_role.history.presentation.HistoryViewModel
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.HomeRepositoryImpl
import features.cashier_role.home.presentation.HomeViewModel
import features.cashier_role.sales.SalesViewModel
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.data.SalesRepositoryImpl
import features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel
import features.cashier_role.sales.presentation.invoice.InvoiceViewModel
import features.cashier_role.sales.presentation.payment.PaymentViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val provideAuthRepositoryModule = module {
    single<AuthRepositoryImpl> {
        AuthRepositoryImpl(
            requestHandler = get(),
            sessionHandler = get()
        )
    }.bind<AuthRepository>()
    viewModel { LoginViewModel(sessionHandler = get(), authRepository = get()) }
    viewModel { ProfileViewModel(authRepository = get()) }
}

val provideHomeRepositoryModule = module {
    single<HomeRepositoryImpl> {
        HomeRepositoryImpl(
            requestHandler = get(),
            productDao = get()
        )
    }.bind<HomeRepository>()
    viewModel { HomeViewModel(homeRepository = get()) }
}

val provideSalesRepositoryModule = module {
    single<SalesRepositoryImpl> {
        SalesRepositoryImpl(
            productDao = get(),
            productTransDraftDao = get(),
            requestHandler = get()
        )
    }.bind<SalesRepository>()
    viewModel { EntrySalesViewModel(salesRepository = get(), authRepository = get()) }
    viewModel { PaymentViewModel(salesRepository = get()) }
    viewModel { InvoiceViewModel(sessionHandler = get(), salesRepository = get()) }
    viewModel { SalesViewModel(salesRepository = get()) }
}

val provideHistoryRepositoryModule = module {
    single<HistoryRepositoryImpl> {
        HistoryRepositoryImpl(
            requestHandler = get()
        )
    }.bind<HistoryRepository>()

    viewModel { HistoryViewModel(historyRepository = get()) }
}