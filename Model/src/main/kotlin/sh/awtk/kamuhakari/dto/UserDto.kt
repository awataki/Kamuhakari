package sh.awtk.kamuhakari.dto

import org.joda.time.DateTime
import sh.awtk.kamuhakari.viewmodel.RoomResponse
import sh.awtk.kamuhakari.vo.OneTimeURL
import sh.awtk.kamuhakari.vo.RoomId
import sh.awtk.kamuhakari.vo.UserId

data class UserDto(
    val id: UserId,
    val roomId: RoomId,
    val oneTimeURL: OneTimeURL,
    var isActiveURL: Boolean = false,
    var expireAt: DateTime = DateTime.now().plusDays(7)
)