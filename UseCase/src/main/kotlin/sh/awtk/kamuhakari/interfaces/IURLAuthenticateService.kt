package sh.awtk.kamuhakari.interfaces

import sh.awtk.kamuhakari.dto.TokenPair

interface IURLAuthenticateService {
    suspend fun authURL(key: String): TokenPair
    suspend fun disableURL(key: String)
}