package sh.awtk.kamuhakari.interfaces

import sh.awtk.kamuhakari.dto.UserDto
import sh.awtk.kamuhakari.vo.OneTimeURL
import sh.awtk.kamuhakari.vo.RoomId
import sh.awtk.kamuhakari.vo.UserId

interface IUserRepository {
    fun create(user: UserDto): UserDto
    fun findBy(userId: UserId): UserDto
    fun findBy(roomId: RoomId): UserDto
    fun findBy(url: OneTimeURL): UserDto
    fun update(user: UserDto): UserDto
    fun disable(userId: UserId)
}