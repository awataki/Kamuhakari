package sh.awtk.kamuhakari.interfaces

import sh.awtk.kamuhakari.dto.RoomDto
import sh.awtk.kamuhakari.dto.UserDto

interface IRoomService {
    suspend fun create(roomDto: RoomDto): List<UserDto>
}