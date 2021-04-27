package sh.awtk.kamuhakari.exception

open class HttpException(val code: Int, val errMessage: String, val clientMessage: String) :
    RuntimeException(errMessage)
