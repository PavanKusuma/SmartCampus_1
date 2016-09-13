package svecw.smartcampus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by Pavan on 6/19/15.
 */
public class RealPathUtil {

    public String result = null;

    // sdk >= 19
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){

        String filePath = "";

        // get the id of document
        String completeId = DocumentsContract.getDocumentId(uri);

        // split the complete id at colon and use the second item in the array
        String id = completeId.split(":")[1];

        String[] column = {MediaStore.Files.FileColumns.DATA};

        // query for getting id
        String query = MediaStore.MediaColumns._ID + "=?";

        Cursor cursor = context.getContentResolver().query(uri, column, query, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    //sdk >= 11 && < 19
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {

        // get mediaTable columns
        String[] proj = { MediaStore.Files.FileColumns.DATA };
        String result = null;

        // query and fetch the cursor
        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    // sdk < 11
    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){

        // get mediaTable columns
        String[] proj = { MediaStore.Files.FileColumns.DATA};

        // query and fetch the cursor
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

        // get the column index
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
