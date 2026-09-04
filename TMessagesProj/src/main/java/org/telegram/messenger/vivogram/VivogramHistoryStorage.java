package org.telegram.messenger.vivogram;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

public class VivogramHistoryStorage {

    private static volatile VivogramHistoryStorage[] Instance = new VivogramHistoryStorage[org.telegram.messenger.UserConfig.MAX_ACCOUNT_COUNT];

    public static VivogramHistoryStorage getInstance(int num) {
        VivogramHistoryStorage localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (VivogramHistoryStorage.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    Instance[num] = localInstance = new VivogramHistoryStorage(num);
                }
            }
        }
        return localInstance;
    }

    private int currentAccount;
    private DatabaseHelper databaseHelper;

    public static class MessageEdit {
        public long dialogId;
        public int messageId;
        public int editDate;
        public String text;
        public ArrayList<TLRPC.MessageEntity> entities;

        public MessageEdit(long dialogId, int messageId, int editDate, String text, ArrayList<TLRPC.MessageEntity> entities) {
            this.dialogId = dialogId;
            this.messageId = messageId;
            this.editDate = editDate;
            this.text = text;
            this.entities = entities;
        }
    }

    public VivogramHistoryStorage(int account) {
        this.currentAccount = account;
        this.databaseHelper = new DatabaseHelper(account);
    }

    public void saveDeletedMessage(long dialogId, int messageId, int deleteDate) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("dialog_id", dialogId);
            cv.put("msg_id", messageId);
            cv.put("delete_date", deleteDate);
            db.insertWithOnConflict("deleted_messages", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void markMessageDeleted(long dialogId, int messageId, int deleteDate) {
        saveDeletedMessage(dialogId, messageId, deleteDate);
    }

    public boolean isMessageDeleted(long dialogId, int messageId) {
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT 1 FROM deleted_messages WHERE dialog_id = ? AND msg_id = ?", new String[]{String.valueOf(dialogId), String.valueOf(messageId)});
            boolean exists = cursor != null && cursor.moveToFirst();
            if (cursor != null) cursor.close();
            return exists;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public int getDeletedMessageDate(long dialogId, int messageId) {
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT delete_date FROM deleted_messages WHERE dialog_id = ? AND msg_id = ?", new String[]{String.valueOf(dialogId), String.valueOf(messageId)});
            int date = 0;
            if (cursor != null && cursor.moveToFirst()) {
                date = cursor.getInt(0);
            }
            if (cursor != null) cursor.close();
            return date;
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public void saveMessageEdit(long dialogId, int messageId, int editDate, String text, ArrayList<TLRPC.MessageEntity> entities) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            byte[] entityBytes = null;
            if (entities != null && !entities.isEmpty()) {
                NativeByteBuffer buffer = new NativeByteBuffer(true);
                buffer.writeInt32(entities.size());
                for (int a = 0; a < entities.size(); a++) {
                    entities.get(a).serializeToStream(buffer);
                }
                entityBytes = new byte[buffer.length()];
                buffer.position(0);
                buffer.readBytes(entityBytes, false);
                buffer.reuse();
            }

            ContentValues cv = new ContentValues();
            cv.put("dialog_id", dialogId);
            cv.put("msg_id", messageId);
            cv.put("edit_date", editDate);
            cv.put("text", text);
            if (entityBytes != null) {
                cv.put("entities", entityBytes);
            }
            db.insert("message_edits", null, cv);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public ArrayList<MessageEdit> getMessageEdits(long dialogId, int messageId) {
        ArrayList<MessageEdit> list = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT edit_date, text, entities FROM message_edits WHERE dialog_id = ? AND msg_id = ? ORDER BY edit_date ASC", new String[]{String.valueOf(dialogId), String.valueOf(messageId)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int editDate = cursor.getInt(0);
                    String text = cursor.getString(1);
                    byte[] entityBytes = cursor.getBlob(2);
                    ArrayList<TLRPC.MessageEntity> entities = null;
                    if (entityBytes != null && entityBytes.length > 0) {
                        try {
                            NativeByteBuffer buffer = new NativeByteBuffer(entityBytes.length);
                            buffer.writeBytes(entityBytes);
                            buffer.position(0);
                            int count = buffer.readInt32(false);
                            entities = new ArrayList<>(count);
                            for (int a = 0; a < count; a++) {
                                int constructor = buffer.readInt32(false);
                                TLRPC.MessageEntity entity = TLRPC.MessageEntity.TLdeserialize(buffer, constructor, false);
                                if (entity != null) {
                                    entities.add(entity);
                                }
                            }
                            buffer.reuse();
                        } catch (Exception err) {
                            FileLog.e(err);
                        }
                    }
                    list.add(new MessageEdit(dialogId, messageId, editDate, text, entities));
                }
                cursor.close();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return list;
    }

    public boolean hasMessageEdits(long dialogId, int messageId) {
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT 1 FROM message_edits WHERE dialog_id = ? AND msg_id = ? LIMIT 1", new String[]{String.valueOf(dialogId), String.valueOf(messageId)});
            boolean exists = cursor != null && cursor.moveToFirst();
            if (cursor != null) cursor.close();
            return exists;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME_PREFIX = "vivogram_history_";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(int account) {
            super(ApplicationLoader.applicationContext, DATABASE_NAME_PREFIX + account + ".db", null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS deleted_messages (dialog_id INTEGER, msg_id INTEGER, delete_date INTEGER, PRIMARY KEY (dialog_id, msg_id));");
            db.execSQL("CREATE TABLE IF NOT EXISTS message_edits (id INTEGER PRIMARY KEY AUTOINCREMENT, dialog_id INTEGER, msg_id INTEGER, edit_date INTEGER, text TEXT, entities BLOB);");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_message_edits_dialog_msg ON message_edits (dialog_id, msg_id);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
