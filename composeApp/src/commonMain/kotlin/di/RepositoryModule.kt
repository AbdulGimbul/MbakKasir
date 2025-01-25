package di

import features.auth.data.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.profile.ProfileViewModel
import features.cashier_role.sales.presentation.history.HistoryViewModel
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.HomeRepositoryImpl
import features.cashier_role.home.presentation.HomeViewModel
import features.cashier_role.product.data.ProductRepository
import features.cashier_role.product.data.ProductRepositoryImpl
import features.cashier_role.product.presentation.ProductViewModel
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
        HomeRepositoryImpl(productDao = get())
    }.bind<HomeRepository>()
    viewModel { HomeViewModel(homeRepository = get(), productRepository = get()) }
}

val provideProductRepositoryModule = module {
    single<ProductRepositoryImpl> {
        ProductRepositoryImpl(requestHandler = get(), productDao = get())
    }.bind<ProductRepository>()
    viewModel { ProductViewModel(productRepository = get()) }
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