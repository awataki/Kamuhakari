package sh.awtk.kamuhakari.principal

import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.authentication
import java.util.*

data class LoginUser(val id: Long, val roomId: String, val expired: Date) : Principal

val ApplicationCall.LoginUser get() = authentication.principal<LoginUser>()