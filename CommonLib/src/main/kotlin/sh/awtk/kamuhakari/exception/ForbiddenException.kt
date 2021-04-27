package sh.awtk.kamuhakari.exception

class ForbiddenException(message: String, cMessage: String) : HttpException(403, message, cMessage)