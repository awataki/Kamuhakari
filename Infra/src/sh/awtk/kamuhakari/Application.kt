package sh.awtk.kamuhakari

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
import sh.awtk.kamuhakari.exposed.DatabaseFactory
import sh.awtk.kamuhakari.jwt.JWTFactory
import sh.awtk.kamuhakari.principal.LoginUser

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.kamuhakari() {

    install(Locations)
    val client = HttpClient(OkHttp) {
        install(WebSockets)

    }
    installAuthentication()

    routing {

        // ワンタイムURLの認証+トークン付与
        @Location("/join/{room}/{key}")
        data class JoinLocation(val room: String, val key: String)
        get<JoinLocation> { query ->
            call.respond(query.key)
        }

        // ルームの作成
        post("/room") {

        }
        authenticate{
            // リバースプロキシの実装
            route("/chat") {

            }
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
        it.maxPoolSize = requireNotNull(environment.config.property("kamuhakari.db.maximumPoolSize").getString().toInt())
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
        it.algorithm = Algorithm.HMAC512(requireNotNull(environment.config.property("kamuhakari.jwt.secret").getString()))
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
                val expiredAt = it.payload.expiresAt
                LoginUser(userId, expiredAt)
            }
        }
    }
}
