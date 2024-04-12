package com.example.contenotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.internal.synchronizedImpl
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


// SELF NOTE:
// Database file can be found in the 'device file explorer':
//  /data/data/com.example.contenotes/databases
class SQLDatabaseActivity(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CASE_ENTRIES) // Create 'cases' table
        db.execSQL(SQL_CREATE_NOTE_ENTRIES) // Create 'notes' table
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_CASE_ENTRIES) // Delete 'cases' table
        db.execSQL(SQL_DELETE_NOTE_ENTRIES) // Delete 'notes' table
        onCreate(db)
    }

    // Functions / Statements for creating & deleting tables
    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "UserData.db"

        // Cases
        private const val SQL_CREATE_CASE_ENTRIES = // Create
            "CREATE TABLE IF NOT EXISTS cases (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "colour TEXT," + // Displayed colour
                    "lastAccessed TEXT)" // Timestamp
        private const val SQL_DELETE_CASE_ENTRIES = // Delete
            "DROP TABLE IF EXISTS cases"

        // Notes
        private const val SQL_CREATE_NOTE_ENTRIES = // Create
            "CREATE TABLE IF NOT EXISTS notes (" +
                    "note_id INTEGER PRIMARY KEY AUTOINCREMENT," + // Global note id
                    "case_id INTEGER," + // Associated case
                    "local_note_id INTEGER," + // Local note id for the individual case
                    "title TEXT," +
                    "content TEXT," +
                    "hashvalue TEXT," + // Hash of the note
                    "created TEXT, "+ // Time of creation
                    "FOREIGN KEY(case_id) REFERENCES cases(id))"
        private const val SQL_DELETE_NOTE_ENTRIES = // Delete
            "DROP TABLE IF EXISTS notes"
    }



    // ------ Case Management Functions
    // Create
    fun newCase(name: String, description: String, colour: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentTime = LocalDateTime.now().format(formatter)

        val db = writableDatabase
        val values = ContentValues().apply{
            put("name", name)
            put("description", description)
            put("colour", colour)
            put("lastAccessed", currentTime)
        }
        return db.insert("cases", null, values)
    }
    // Update existing
    fun updateCase(id: Int, name: String, description: String, colour: String){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentTime = LocalDateTime.now().format(formatter)

        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("colour", colour)
            put("lastAccessed", currentTime)
        }
        db.update("cases", values, "id = ?", arrayOf(id.toString()))
    }
    // Delete case
    fun deleteCase(id: Int){
        val db = writableDatabase
        db.delete("cases", "id = ?", arrayOf(id.toString()))
    }
    // Fetch all cases
    fun fetchAllCases(): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM cases", null)
    }



    // ------ Note Management Functions
    // Get # of notes in a case
    fun getNotesCount(caseId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM notes WHERE case_id = ?", arrayOf(caseId.toString()))
        var count = 0
        if (cursor.moveToFirst()){
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    // Create
    fun createNote(caseId: Int, title: String, content: String, hashvalue: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentTime = LocalDateTime.now().format(formatter)
        val existingNotes = getNotesCount(caseId)
        println(existingNotes)
        println(existingNotes+1)

        val db = writableDatabase
        val values = ContentValues().apply{
            put("case_id", caseId)
            put("local_note_id", existingNotes + 1)
            put("title", title)
            put("content", content)
            put("hashvalue", hashvalue)
            put("created", currentTime)
        }
        return db.insert("notes", null, values)
    }
    // Update function for notes is not necessary as it goes against the purpose of the application
    // Delete
    fun deleteNote(id: Int){
        val db = writableDatabase
        db.delete("notes", "note_id = ?", arrayOf(id.toString()))
    }
    // Fetch all
    fun fetchNotes(caseId: Int): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM notes WHERE case_id = ?", arrayOf(caseId.toString()))
    }
    // Fetch one
    fun fetchSingleNote(noteId: Int): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM notes WHERE note_id = ? LIMIT 1 ", arrayOf(noteId.toString()))
    }
}

