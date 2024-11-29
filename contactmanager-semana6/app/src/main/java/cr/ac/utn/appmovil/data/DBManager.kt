package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.appmovil.util.BitmapUtils


class DBManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), IDBManager {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_PHOTO = "photo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_PHONE INTEGER,
                $COLUMN_EMAIL TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_COUNTRY TEXT,
                $COLUMN_PHOTO BLOB
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    override fun add(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.Id)
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LAST_NAME, contact.LastName)
            put(COLUMN_PHONE, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_ADDRESS, contact.Address)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_PHOTO, BitmapUtils.bitmapToByteArray(contact.Photo))
        }
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    override fun update(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LAST_NAME, contact.LastName)
            put(COLUMN_PHONE, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_ADDRESS, contact.Address)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_PHOTO, BitmapUtils.bitmapToByteArray(contact.Photo))
        }
        db.update(TABLE_CONTACTS, values, "$COLUMN_ID = ?", arrayOf(contact.Id))
        db.close()
    }

    override fun remove(id: String) {
        val db = writableDatabase
        db.delete(TABLE_CONTACTS, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
    }

    override fun getAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                    photo = BitmapUtils.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow(
                        COLUMN_PHOTO
                    )))
                )
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTACTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )

        var contact: Contact? = null
        if (cursor.moveToFirst()) {
            contact = Contact(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                photo = BitmapUtils.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow(
                    COLUMN_PHOTO
                )))
            )
        }
        cursor.close()
        db.close()
        return contact
    }
}
