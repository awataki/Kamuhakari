package sh.awtk.kamuhakari.viewmodel

data class RoomResponse(
    val invite_links: List<String>,
    val access_token: String
)