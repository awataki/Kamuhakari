package sh.awtk.kamuhakari.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.joda.time.DateTime

object JWTFactory {
    lateinit var algorithm: Algorithm
    lateinit var issuer: String
    lateinit var audience: String
    lateinit var verifyer: JWTVerifier

    fun init() {
        this.verifyer = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build()
    }

    fun newToken(userId: Long, roomId: String, expires: DateTime): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("user_id", userId)
            .withClaim("room_id", roomId)
            .withExpiresAt(expires.toDate())
            .sign(algorithm)
    }
}