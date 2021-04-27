package sh.awtk.kamuhakari.exception

class ObjectNotFoundException(message: String, cMessage: String = "内部エラーが発生しました") :
    HttpException(404, message, cMessage)