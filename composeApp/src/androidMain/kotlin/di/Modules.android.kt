package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import dependencies.DbClient
import dependencies.MyViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf

actual val platformModule = module {
    singleOf(::DbClient)
}