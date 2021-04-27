package sh.awtk.kamuhakari.interfaces

import sh.awtk.kamuhakari.dto.RoomDto
import sh.awtk.kamuhakari.vo.RoomId
import sh.awtk.kamuhakari.vo.UserId

interface IRoomRepository {
    fun create(room: RoomDto): RoomDto
    fun findBy(roomId: RoomId): RoomDto
    fun update(room: RoomDto): RoomDto
    fun close(room: RoomDto): RoomId
}