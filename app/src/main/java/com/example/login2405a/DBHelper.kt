package com.example.login2405a

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "Login.db"
        const val DB_VERSION = 1
        const val TB_NAME = "user"
        const val ID = "id"
        const val PW = "pw"
        const val NICK = "nick"
    }
    private val writableDB by lazy { this.writableDatabase }
    private val readableDB by lazy { this.readableDatabase }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table if not exists $TB_NAME($ID text primary key, $PW text, $NICK text)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val sql = "drop table if exists $TB_NAME"
        db?.execSQL(sql)
    }

    fun insert(id: String?, pw: String?, nick: String?): Boolean {
        val contentValues = ContentValues().apply {
            put(ID, id)
            put(PW, pw)
            put(NICK, nick)
        }
        val user = writableDB.insert(TB_NAME, null, contentValues)
        writableDB.close()
        return user != -1L // 삽입 성공 시 true, 실패 시 false
    }

    fun getNickById(id: String) : String? {
        val cursor = readableDB.rawQuery("select $NICK from $TB_NAME where $ID = ?", arrayOf(id))
        var nick: String? = null
        if(cursor.moveToFirst()) {
            nick = cursor.getString(cursor.getColumnIndexOrThrow(NICK))
        }
        cursor.close()
        return nick
    }

    fun checkId(id: String?): Boolean {
        var user = false
        val cursor = readableDB.rawQuery("select $ID from $TB_NAME where id =?", arrayOf(id))
        if (cursor.count > 0) user = true
        cursor.close()
        return user
    }

    // id와 pw가 일치하는 사용자가 있는지 확인
    fun checkPw(id: String, pw: String) : Boolean {
        var user = false
        val cursor = readableDB.rawQuery("select $ID, $PW from $TB_NAME where id = ? and pw = ?", arrayOf(id, pw))
        if (cursor.count > 0) user = true
        cursor.close()
        return user
    }

    fun checkNick(nick: String?): Boolean {
        var user = false
        val cursor = readableDB.rawQuery("select $NICK from $TB_NAME where nick =?", arrayOf(nick))
        if (cursor.count > 0) user = true
        cursor.close()
        return user
    }
}