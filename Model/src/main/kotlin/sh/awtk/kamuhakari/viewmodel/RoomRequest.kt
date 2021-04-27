package sh.awtk.kamuhakari.viewmodel

import sh.awtk.kamuhakari.dto.RoomDto
import sh.awtk.kamuhakari.vo.MailList
import sh.awtk.kamuhakari.vo.NumOfParticipants
import sh.awtk.kamuhakari.vo.RoomId

data class RoomRequest(
    val num_of_participants: Int,
    val mail_list: List<String>
) {
    fun toDto(): RoomDto {
        return RoomDto(
            id = RoomId(""),
            numOfParticipants = NumOfParticipants(this.num_of_participants),
            mailList = MailList(this.mail_list)
        )
    }
}