package cl.farmaonline.www.farmaonline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SpiderHacker on 8/11/2016.
 */
public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE user_farma (id_user INTEGER PRIMARY KEY AUTOINCREMENT, nom_user TEXT, ape_user TEXT, tel_user TEXTO)";

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int valAct, int valSig) {
        db.execSQL("DROP TABLE IF EXISTS user_farma");
        db.execSQL(sqlCreate);
    }
}
