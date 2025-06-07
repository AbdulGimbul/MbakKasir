package dev.mbakasir.com.di

import com.plusmobileapps.konnectivity.Konnectivity
import dev.mbakasir.com.features.admin_role.stock_opname.data.StockOpnameRepository
import dev.mbakasir.com.features.admin_role.stock_opname.data.StockOpnameRepositoryImpl
import dev.mbakasir.com.features.admin_role.stock_opname.presentation.StockOpnameViewModel
import dev.mbakasir.com.features.admin_role.stock_opname.presentation.entry_stock_opname.EntryStockOpnameViewModel
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.auth.data.AuthRepositoryImpl
import dev.mbakasir.com.features.auth.presentation.login.LoginViewModel
import dev.mbakasir.com.features.auth.presentation.profile.ProfileViewModel
import dev.mbakasir.com.features.cashier_role.home.data.HomeRepository
import dev.mbakasir.com.features.cashier_role.home.data.HomeRepositoryImpl
import dev.mbakasir.com.features.cashier_role.home.presentation.HomeViewModel
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepositoryImpl
import dev.mbakasir.com.features.cashier_role.product.presentation.ProductViewModel
import dev.mbakasir.com.features.cashier_role.sales.SalesViewModel
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepositoryImpl
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.invoice.InvoiceViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentViewModel
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
    single { Konnectivity() }
    viewModel {
        LoginViewModel(
            sessionHandler = get(),
            authRepository = get(),
            konnectivity = get()
        )
    }
    viewModel { ProfileViewModel(authRepository = get()) }
}

val provideHomeRepositoryModule = module {
    single<HomeRepositoryImpl> {
        HomeRepositoryImpl(productDao = get(), reqHandler = get())
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
    viewModel { HistoryViewModel(salesRepository = get()) }
}

val provideStockOpnameRepositoryModule = module {
    single<StockOpnameRepositoryImpl> {
        StockOpnameRepositoryImpl(productDao = get(), requestHandler = get())
    }.bind<StockOpnameRepository>()
    viewModel { StockOpnameViewModel(stockOpnameRepository = get()) }
    viewModel { EntryStockOpnameViewModel(stockOpnameRepository = get(), authRepository = get()) }
}