package cr.ac.utn.appmovil.data


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ContactDatabaseHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "authentications.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_AUTHENTICATIONS = "authentications"
        private const val COLUMN_AUTH_ID = "auth_id"
        private const val COLUMN_USER = "user"
        private const val COLUMN_TIMESTAMP = "timestamp"

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAuthTableQuery = """
            CREATE TABLE $TABLE_AUTHENTICATIONS (
                $COLUMN_AUTH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER TEXT,
                $COLUMN_TIMESTAMP TEXT
            )
        """.trimIndent()
        db.execSQL(createAuthTableQuery)

        val createUserTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()
        db.execSQL(createUserTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHENTICATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addAuthentication(user: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER, user)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis().toString())
        }
        db.insert(TABLE_AUTHENTICATIONS, null, values)
        db.close()
    }

    fun getAllAuthentications(): List<Pair<String, String>> {
        val authentications = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_AUTHENTICATIONS, null, null, null, null, null, "$COLUMN_TIMESTAMP DESC")
        if (cursor.moveToFirst()) {
            do {
                val user = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER))
                val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                authentications.add(Pair(user, timestamp))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return authentications
    }

    fun removeAuthentication(authId: Int) {
        val db = writableDatabase
        db.delete(TABLE_AUTHENTICATIONS, "$COLUMN_AUTH_ID = ?", arrayOf(authId.toString()))
        db.close()
    }

    fun clearAuthentications() {
        val db = writableDatabase
        db.delete(TABLE_AUTHENTICATIONS, null, null)
        db.close()
    }

    fun addUser(username: String, password: String): Boolean {
        return try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, username)
                put(COLUMN_PASSWORD, password)
            }
            db.insert(TABLE_USERS, null, values)
            db.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?", arrayOf(username))

        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun clearUsers() {
        val db = writableDatabase
        db.delete(TABLE_USERS, null, null)
        db.close()
    }

    fun saveAuthentication(username: String, timestamp: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("timestamp", timestamp)
        db.insert("auth_sessions", null, values)
        db.close()
    }

}

data class User(
    val id: Int,
    val username: String,
    val password: String
)

