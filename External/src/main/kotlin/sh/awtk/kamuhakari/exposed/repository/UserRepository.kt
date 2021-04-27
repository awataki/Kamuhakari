package sh.awtk.kamuhakari.exposed.repository

import sh.awtk.kamuhakari.dto.UserDto
import sh.awtk.kamuhakari.exception.ObjectNotFoundException
import sh.awtk.kamuhakari.exposed.table.RoomEntity
import sh.awtk.kamuhakari.exposed.table.RoomTable
import sh.awtk.kamuhakari.exposed.table.UserEntity
import sh.awtk.kamuhakari.exposed.table.UserTable
import sh.awtk.kamuhakari.interfaces.IUserRepository
import sh.awtk.kamuhakari.vo.OneTimeURL
import sh.awtk.kamuhakari.vo.RoomId
import sh.awtk.kamuhakari.vo.UserId

class UserRepository : IUserRepository {
    override fun create(user: UserDto): UserDto = UserEntity.new {
        roomId =
            RoomEntity.find { RoomTable.rawId eq user.roomId.value }.firstOrNull() ?: throw ObjectNotFoundException("")
        oneTimeURL = user.oneTimeURL.value
        isActiveURL = user.isActiveURL
        expiredAt = user.expireAt
    }.toDto()

    override fun findBy(userId: UserId): UserDto =
        UserEntity.find { UserTable.id eq userId.value }.firstOrNull()?.toDto()
            ?: throw ObjectNotFoundException("user not found")

    override fun findBy(roomId: RoomId): UserDto {
        TODO("Not yet implemented")
    }

    override fun findBy(url: OneTimeURL): UserDto =
        UserEntity.find { UserTable.oneTimeURL eq url.value }.firstOrNull()?.toDto()
            ?: throw ObjectNotFoundException("user not found")


    override fun update(user: UserDto): UserDto = UserEntity.findById(user.id.value)?.also {
        it.roomId = RoomEntity.find {
            RoomTable.rawId eq user.roomId.value
        }.firstOrNull() ?: throw ObjectNotFoundException("")
        it.oneTimeURL = user.oneTimeURL.value
        it.isActiveURL = user.isActiveURL
        it.expiredAt = user.expireAt
    }?.toDto() ?: throw ObjectNotFoundException("")

    override fun disable(userId: UserId) {
        TODO("Not yet implemented")
    }

}