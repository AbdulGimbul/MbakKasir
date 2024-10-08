package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            provideHttpClientModule,
            platformModule,
            provideSessionHandlerModule,
            provideAuthRepositoryModule,
            provideHomeRepositoryModule,
            provideSalesRepositoryModule,
            provideHistoryRepositoryModule
        )
    }
}