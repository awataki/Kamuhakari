package sh.awtk.kamuhakari.service

import org.joda.time.DateTime
import sh.awtk.kamuhakari.dto.TokenPair
import sh.awtk.kamuhakari.exception.AuthenticationException
import sh.awtk.kamuhakari.interfaces.ITransaction
import sh.awtk.kamuhakari.interfaces.IURLAuthenticateService
import sh.awtk.kamuhakari.interfaces.IUserRepository
import sh.awtk.kamuhakari.jwt.JWTFactory
import sh.awtk.kamuhakari.vo.OneTimeURL

class URLAuthenticateService(
    private val userRepository: IUserRepository,
    private val transaction: ITransaction
) : IURLAuthenticateService {
    override suspend fun authURL(key: String): TokenPair {
        return transaction.run {
            val user = userRepository.findBy(OneTimeURL(key))
            if (!user.isActiveURL || DateTime.now() > user.expireAt) throw AuthenticationException(
                "out dated url",
                "無効なURLです"
            )
            user.isActiveURL = false
            userRepository.update(user)
            return@run TokenPair(
                user.roomId.value,
                "not implemented"
            )
        }
    }

    override suspend fun disableURL(key: String) {
        transaction.run {
            val user = userRepository.findBy(OneTimeURL(key)).also { it.isActiveURL = false }
            userRepository.update(user)
        }
    }
}