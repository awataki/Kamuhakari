package sh.awtk.kamuhakari.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import sh.awtk.kamuhakari.exposed.table.RoomTable
import sh.awtk.kamuhakari.exposed.table.UserTable
import kotlin.properties.Delegates

object DatabaseFactory {

    lateinit var dbUrl: String
    lateinit var dbUser: String
    lateinit var dbPassword: String
    lateinit var dbDriver: String
    var maxPoolSize by Delegates.notNull<Int>()
    var isAutoCommit by Delegates.notNull<Boolean>()
    lateinit var transactionIsolation: String

    fun init() {
        Database.connect(hikariCP())
        transaction {
            SchemaUtils.create(UserTable, RoomTable)
        }
    }

    private fun hikariCP(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = dbDriver
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = maxPoolSize
        config.isAutoCommit = isAutoCommit
        config.transactionIsolation = transactionIsolation
        config.validate()
        return HikariDataSource(config)
    }
}