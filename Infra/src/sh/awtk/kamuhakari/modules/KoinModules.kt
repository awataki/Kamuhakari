package sh.awtk.kamuhakari.modules

import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy
import sh.awtk.kamuhakari.exposed.TransactionImpl
import sh.awtk.kamuhakari.exposed.repository.RoomRepository
import sh.awtk.kamuhakari.exposed.repository.UserRepository
import sh.awtk.kamuhakari.interfaces.IRoomRepository
import sh.awtk.kamuhakari.interfaces.IRoomService
import sh.awtk.kamuhakari.interfaces.ITransaction
import sh.awtk.kamuhakari.interfaces.IURLAuthenticateService
import sh.awtk.kamuhakari.interfaces.IUserRepository
import sh.awtk.kamuhakari.service.RoomService
import sh.awtk.kamuhakari.service.URLAuthenticateService

class KoinModules {
    companion object {
        val modules = module {
            // Service
            singleBy<IURLAuthenticateService, URLAuthenticateService>()
            singleBy<IRoomService, RoomService>()
            //Repository
            singleBy<ITransaction, TransactionImpl>()
            singleBy<IRoomRepository, RoomRepository>()
            singleBy<IUserRepository, UserRepository>()
        }
    }
}