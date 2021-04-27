package sh.awtk.kamuhakari.exception

class AuthenticationException(message: String, cMessage: String) : HttpException(401, message, cMessage)