package sh.awtk.kamuhakari.exception

class BadRequestException(message: String, cMessage: String) : HttpException(400, message, cMessage)