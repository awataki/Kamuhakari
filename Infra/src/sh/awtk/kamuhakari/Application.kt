package sh.awtk.kamuhakari

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import sh.awtk.kamuhakari.exposed.DatabaseFactory
import sh.awtk.kamuhakari.interfaces.IRoomService
import sh.awtk.kamuhakari.interfaces.IURLAuthenticateService
import sh.awtk.kamuhakari.jwt.JWTFactory
import sh.awtk.kamuhakari.modules.KoinModules
import sh.awtk.kamuhakari.principal.LoginUser
import sh.awtk.kamuhakari.viewmodel.JoinResponse
import sh.awtk.kamuhakari.viewmodel.RoomRequest
import sh.awtk.kamuhakari.viewmodel.RoomResponse

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.kamuhakari() {
    setupDB()
    setupJWT()
    install(Locations)
    install(ContentNegotiation) {
        gson {}
    }
    installAuthentication()
    installCORS()
    installKoin()

    routing {
        val roomService: IRoomService by inject()
        val urlService: IURLAuthenticateService by inject()

        // ワンタイムURLの認証+トークン付与
        @Location("/join/{key}")
        data class JoinLocation(val key: String)
        get<JoinLocation> { query ->
            call.respond(
                JoinResponse(urlService.authURL(key = query.key).access_token)
            )
        }

        // ルームの作成
        post("/room") {
            val users = roomService.create(call.receive<RoomRequest>().toDto())
            val urls = users.map { "/join/" + it.oneTimeURL.value }
            call.respond(
                RoomResponse(
                    invite_links = urls,
                    access_token = users[0].roomId.value
                )
            )
        }
    }
}

@KtorExperimentalAPI
private fun Application.setupDB() {
    DatabaseFactory.also {
        it.dbUrl = requireNotNull(environment.config.property("kamuhakari.db.jdbcUrl").getString())
        it.dbUser = requireNotNull(environment.config.property("kamuhakari.db.username").getString())
        it.dbPassword = requireNotNull(environment.config.property("kamuhakari.db.password").getString())
        it.dbDriver = requireNotNull(environment.config.property("kamuhakari.db.driverClassName").getString())
        it.maxPoolSize =
            requireNotNull(environment.config.property("kamuhakari.db.maximumPoolSize").getString().toInt())
        it.isAutoCommit =
            requireNotNull(environment.config.property("kamuhakari.db.isAutoCommit").getString().toBoolean())
        it.transactionIsolation =
            requireNotNull(environment.config.property("kamuhakari.db.transactionIsolation").getString())
    }
    DatabaseFactory.init()
}

@KtorExperimentalAPI
private fun Application.setupJWT() {
    JWTFactory.also {
        it.algorithm =
            Algorithm.HMAC512(requireNotNull(environment.config.property("kamuhakari.jwt.secret").getString()))
        it.issuer = requireNotNull(environment.config.property("kamuhakari.jwt.issuer").getString())
        it.audience = requireNotNull(environment.config.property("kamuhakari.jwt.audience").getString())
    }

    JWTFactory.init()
}

@KtorExperimentalAPI
private fun Application.installAuthentication() {
    setupJWT()
    val jwtRealm = requireNotNull(environment.config.property("kamuhakari.jwt.realm").getString())

    install(Authentication) {
        jwt {
            realm = jwtRealm
            verifier(JWTFactory.verifyer)
            validate {
                val userId = it.payload.getClaim("user_id").asLong()
                val roomId = it.payload.getClaim("room_id").asString()
                val expiredAt = it.payload.expiresAt
                LoginUser(userId, roomId, expiredAt)
            }
        }
    }
}


private fun Application.installKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            KoinModules.modules
        )
    }
}

private fun Application.installCORS() {
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Options)
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
}