package cr.ac.utn.appmovil.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1


        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LASTNAME = "lastname"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_PHOTO = "photo"


        private const val TABLE_AUTHENTICATIONS = "authentications"
        private const val COLUMN_AUTH_ID = "auth_id"
        private const val COLUMN_AUTH_NAME = "name"
        private const val COLUMN_AUTH_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE_CONTACTS = ("CREATE TABLE $TABLE_CONTACTS (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LASTNAME TEXT," +
                "$COLUMN_PHONE INTEGER," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_ADDRESS TEXT," +
                "$COLUMN_COUNTRY TEXT," +
                "$COLUMN_PHOTO BLOB)")
        db?.execSQL(CREATE_TABLE_CONTACTS)


        val CREATE_TABLE_AUTHENTICATIONS = ("CREATE TABLE $TABLE_AUTHENTICATIONS (" +
                "$COLUMN_AUTH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_AUTH_NAME TEXT," +
                "$COLUMN_AUTH_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)")
        db?.execSQL(CREATE_TABLE_AUTHENTICATIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {

            val CREATE_TABLE_AUTHENTICATIONS = ("CREATE TABLE $TABLE_AUTHENTICATIONS (" +
                    "$COLUMN_AUTH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_AUTH_NAME TEXT," +
                    "$COLUMN_AUTH_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)")
            db?.execSQL(CREATE_TABLE_AUTHENTICATIONS)
        }
    }


    fun insertAuthentication(name: String) {
        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_AUTHENTICATIONS ($COLUMN_AUTH_NAME) VALUES (?)"
        val statement = db.compileStatement(query)
        statement.bindString(1, name)
        statement.executeInsert()
        db.close()
    }


    fun getAllAuthentications(): List<Pair<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_AUTH_NAME, $COLUMN_AUTH_TIMESTAMP FROM $TABLE_AUTHENTICATIONS"
        val cursor = db.rawQuery(query, null)

        val authentications = mutableListOf<Pair<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTH_NAME))
                val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTH_TIMESTAMP))
                authentications.add(Pair(name, timestamp))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return authentications
    }
}
