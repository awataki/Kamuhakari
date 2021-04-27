package sh.awtk.kamuhakari.service

import org.joda.time.DateTime
import sh.awtk.kamuhakari.dto.RoomDto
import sh.awtk.kamuhakari.dto.UserDto
import sh.awtk.kamuhakari.interfaces.IRoomRepository
import sh.awtk.kamuhakari.interfaces.IRoomService
import sh.awtk.kamuhakari.interfaces.ITransaction
import sh.awtk.kamuhakari.interfaces.IUserRepository
import sh.awtk.kamuhakari.vo.OneTimeURL
import sh.awtk.kamuhakari.vo.RoomId
import sh.awtk.kamuhakari.vo.UserId

class RoomService(
    private val userRepository: IUserRepository,
    private val roomRepository: IRoomRepository,
    private val transaction: ITransaction
) : IRoomService {

    val RANDOM_MAX_LENGTH = 100
    val RANDOM_MIN_LENGTH = 50

    override suspend fun create(roomDto: RoomDto): List<UserDto> {
        val id = getRandomString((RANDOM_MIN_LENGTH..RANDOM_MAX_LENGTH).random())
        roomDto.id = RoomId(id)
        val userList: MutableList<UserDto> = mutableListOf()
        transaction.run {
            roomRepository.create(roomDto)
            for (i in 0 until roomDto.numOfParticipants.value) {
                userList.add(
                    userRepository.create(
                        UserDto(
                            UserId(0),
                            RoomId(id),
                            OneTimeURL(getRandomString((RANDOM_MIN_LENGTH..RANDOM_MAX_LENGTH).random())),
                            true
                        )
                    )
                )
            }
        }
        return userList
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "-_!*'()"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}