package internaldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.Wall;
import model.Exams;
import model.Privilege;
import model.Questions;
import model.StudentWall;
import model.User;
import utils.Constants;

/**
 * Created by Pavan on 4/15/15.
 */
public class SmartCampusDB extends SQLiteOpenHelper {

    public SmartCampusDB(Context context){
        super(context, "SVECW_DB.db", null, 1);
    }

    public SmartCampusDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create a user table for storing user details while registering
        db.execSQL("CREATE TABLE "+Constants.userTable+" (id INTEGER PRIMARY KEY, campusId TEXT, userObjectId TEXT, username TEXT, email TEXT, phoneNumber TEXT, collegeId TEXT, year INTEGER, branch TEXT, semester INTEGER, role TEXT, mediaCount INTEGER, media TEXT)");//, messages INTEGER, knowledgeWall INTEGER, collegeWall INTEGER, studentWall INTEGER, moderating INTEGER, collegeDirectory INTEGER, cseDirectory INTEGER, eceDirectory INTEGER, eeeDirectory INTEGER, itDirectory INTEGER, mechDirectory INTEGER, civilDirectory INTEGER, mbaDirectory INTEGER, mcaDirectory INTEGER, basicScienceDirectory INTEGER, complaintOrFeedback INTEGER)");

        // create a table to store privileges of user
        db.execSQL("CREATE TABLE "+Constants.privilegeTable+" (id TEXT PRIMARY KEY, privilegeId TEXT, userObjectId TEXT, moderating TEXT, directory TEXT, createdAt TEXT, updatedAt TEXT)");
        
        // create a table to store the object ids of posts that are liked by current user
        db.execSQL("CREATE TABLE " + Constants.userLikesTable + " (id INTEGER PRIMARY KEY, wallId TEXT)");

        // create a table to store the object ids of posts that are commented by current user
        db.execSQL("CREATE TABLE " + Constants.commentsTable + " (id INTEGER PRIMARY KEY, objectId TEXT)");

        // create a table to store the exams
        db.execSQL("CREATE TABLE " + Constants.examsTable + " (id INTEGER PRIMARY KEY, objectId TEXT, name TEXT, expiryDate TEXT)");

        // create a table to store questions
        db.execSQL("CREATE TABLE " + Constants.questionsTable + " (id INTEGER PRIMARY KEY, examId TEXT, objectId TEXT, question TEXT, answer1 TEXT, answer2 TEXT, answer3 TEXT, answer4 TEXT, correctAnswer TEXT, explanation TEXT)");

        // create a table for Student wall posts
        db.execSQL("CREATE TABLE " + Constants.studentWall + " (id INTEGER PRIMARY KEY, objectId TEXT, postDescription TEXT, createdAt Date, updatedAt Date, likes INTEGER, dislikes INTEGER, comments INTEGER, userObjectId TEXT, username TEXT, userImage blob, mediaFile blob)");

        // create a table for College wall posts
        db.execSQL("CREATE TABLE " + Constants.collegeWall + " (id INTEGER PRIMARY KEY, objectId TEXT, postDescription TEXT, createdAt Date, updatedAt Date, likes INTEGER, dislikes INTEGER, comments INTEGER, userObjectId TEXT, username TEXT, userImage blob, mediaFile blob)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop the table and create it again
        db.execSQL("DROP TABLE IF EXISTS "+Constants.userTable);
        //db.execSQL("DROP TABLE IF EXISTS "+Constants.privilegeTable);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.userLikesTable);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.commentsTable);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.examsTable);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.questionsTable);
        onCreate(db);
    }

    /**
     * This method will insert user details into database
     * while user is registering for first time, the email verification
     * is set to false
     *
     * @param user
     */
    public void insertUser(User user){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.campusId, user.getCampusId());
        contentValues.put(Constants.userObjectId, user.getObjectId());
        contentValues.put(Constants.userName, user.getUserName());
        contentValues.put(Constants.email, user.getEmail());
        contentValues.put(Constants.phoneNumber, user.getPhoneNumber());
        contentValues.put(Constants.collegeId, user.getCollegeId());
        contentValues.put(Constants.year, user.getYear());
        contentValues.put(Constants.branch, user.getBranch());
        contentValues.put(Constants.semester, user.getSemester());
        contentValues.put(Constants.role, user.getRole());

        contentValues.put(Constants.mediaCount, user.getMediaCount());
        contentValues.put(Constants.media, user.getMedia());

        /*contentValues.put(Constants.messages, user.getMessages());
        contentValues.put(Constants.knowledgeWall, user.getKnowledgeWall());
        contentValues.put(Constants.collegeWall, user.getCollegeWall());
        contentValues.put(Constants.studentWall, user.getStudentWall());
        contentValues.put(Constants.moderating, user.getModerating());
        contentValues.put(Constants.collegeDirectory, user.getCollegeDirectory());
        contentValues.put(Constants.cseDirectory, user.getCseDirectory());
        contentValues.put(Constants.eceDirectory, user.getEceDirectory());
        contentValues.put(Constants.eeeDirectory, user.getEeeDirectory());
        contentValues.put(Constants.itDirectory, user.getItDirectory());
        contentValues.put(Constants.mechDirectory, user.getMechDirectory());
        contentValues.put(Constants.civilDirectory, user.getCivilDirectory());
        contentValues.put(Constants.mbaDirectory, user.getMbaDirectory());
        contentValues.put(Constants.mcaDirectory, user.getMcaDirectory());
        contentValues.put(Constants.basicScienceDirectory, user.getBasicScienceDirectory());
        contentValues.put(Constants.complaintOrFeedback, user.getComplaintOrFeedback());
*/

        // insert to database and close
        db.insert(Constants.userTable, null, contentValues);
        db.close();
    }

    /**
     * This will insert privileges assigned for the current user
     *
     * @param privilege
     */
    public void insertPrivilege(Privilege privilege){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.privilegeId, privilege.getPrivilegeId());
        contentValues.put(Constants.userObjectId, privilege.getUserObjectId());
        contentValues.put(Constants.moderating, privilege.getModerating());
        contentValues.put(Constants.directory, privilege.getDirectory());
        contentValues.put(Constants.createdAt, privilege.getCreatedAt());
        contentValues.put(Constants.updatedAt, privilege.getUpdatedAt());

        // insert to database and close
        db.insert(Constants.privilegeTable, null, contentValues);
        db.close();
    }

    /**
     * This will insert the object id of the post
     * that is liked/ disliked by current user
     *
     * @param wallId
     */
    public void insertUserLikesAndDisLikes(String wallId){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.wallId, wallId);

        // insert to database and close
        db.insert(Constants.userLikes, null, contentValues);
        db.close();
    }

    /**
     * This will insert the object id of the post
     * that is commented by current user
     *
     * @param objectId
     */
    public void insertUserComment(String objectId){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.objectId, objectId);

        // insert to database and close
        db.insert(Constants.commentsTable, null, contentValues);
        db.close();
    }

    /**
     * This method will insert exam details into database
     *
     * @param exams
     */
    public void insertExam(Exams exams){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.objectId, exams.getObjectId());
        contentValues.put(Constants.name, exams.getName());
        contentValues.put(Constants.expiryDate, exams.getExpiryDate());

        // insert to database and close
        db.insert(Constants.examsTable, null, contentValues);
        System.out.println("Inserted exam");
        db.close();
    }

    /**
     * This method will insert question details into database
     *
     * @param questions
     */
    public void insertQuestions(ArrayList<Questions> questions){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i<questions.size(); i++) {

            // map the column value pair
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.examId, questions.get(i).getExamId());
            contentValues.put(Constants.objectId, questions.get(i).getQuestionId());
            contentValues.put(Constants.question, questions.get(i).getQuestion());
            contentValues.put(Constants.answer1, questions.get(i).getAnswer1());
            contentValues.put(Constants.answer2, questions.get(i).getAnswer2());
            contentValues.put(Constants.answer3, questions.get(i).getAnswer3());
            contentValues.put(Constants.answer4, questions.get(i).getAnswer4());
            contentValues.put(Constants.correctAnswer, questions.get(i).getCorrectAnswer());
            contentValues.put(Constants.explanation, questions.get(i).getExplanation());

            // insert to database and close
            db.insert(Constants.questionsTable, null, contentValues);
        }
        System.out.println("Inserted question");
        db.close();
    }

    /**
     * This method will insert studentWall posts into database
     *
     * @param studentWallPosts
     */
    public void insertStudentWallPosts(List<StudentWall> studentWallPosts){

        // date format for displaying created date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy, MMM dd, HH:mm:ss");
        simpleDateFormat.format(new Date());

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        /*ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.objectId, studentWallPosts.getObjectId());
            contentValues.put(Constants.postDescription, studentWallPosts.getPostDescription());
            contentValues.put(Constants.createdAt, simpleDateFormat.format(studentWallPosts.getCreatedAt()));
            contentValues.put(Constants.updatedAt, simpleDateFormat.format(studentWallPosts.getUpdatedAt()));
            contentValues.put(Constants.likes, studentWallPosts.getLikes());
            contentValues.put(Constants.dislikes, studentWallPosts.getDislikes());
            contentValues.put(Constants.comments, studentWallPosts.getComments());
            contentValues.put(Constants.userObjectId, studentWallPosts.getUserObjectId());
            contentValues.put(Constants.userName, studentWallPosts.getUserName());
            contentValues.put(Constants.userImage, studentWallPosts.getUserImage());
            contentValues.put(Constants.mediaFile, studentWallPosts.getMediaFile());*/

        for(int i=0; i<studentWallPosts.size(); i++) {

            // map the column value pair
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.objectId, studentWallPosts.get(i).getObjectId());
            contentValues.put(Constants.postDescription, studentWallPosts.get(i).getPostDescription());
            contentValues.put(Constants.createdAt, simpleDateFormat.format(studentWallPosts.get(i).getCreatedAt()));
            contentValues.put(Constants.updatedAt, simpleDateFormat.format(studentWallPosts.get(i).getUpdatedAt()));
            contentValues.put(Constants.likes, studentWallPosts.get(i).getLikes());
            contentValues.put(Constants.dislikes, studentWallPosts.get(i).getDislikes());
            contentValues.put(Constants.comments, studentWallPosts.get(i).getComments());
            contentValues.put(Constants.userObjectId, studentWallPosts.get(i).getUserObjectId());
            contentValues.put(Constants.userName, studentWallPosts.get(i).getUserName());
            contentValues.put(Constants.userImage, studentWallPosts.get(i).getUserImage());
            contentValues.put(Constants.mediaFile, studentWallPosts.get(i).getMediaFile());

            // insert to database and close
            db.insert(Constants.studentWall, null, contentValues);

        }

        Log.v(Constants.appName, "Inserted db");
        db.close();
    }

    /**
     * This method will insert collegeWall posts into database
     *
     * @param studentWallPost
     */
    public void insertCollegeWallPosts(StudentWall studentWallPost){

        // get the writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // map the column value pair
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.objectId, studentWallPost.getObjectId());
        contentValues.put(Constants.postDescription, studentWallPost.getPostDescription());
        contentValues.put(Constants.createdAt, studentWallPost.getCreatedAt().toString());
        contentValues.put(Constants.updatedAt, studentWallPost.getUpdatedAt().toString());
        contentValues.put(Constants.likes, studentWallPost.getLikes());
        contentValues.put(Constants.dislikes, studentWallPost.getDislikes());
        contentValues.put(Constants.comments, studentWallPost.getComments());
        contentValues.put(Constants.userObjectId, studentWallPost.getUserObjectId());
        contentValues.put(Constants.userName, studentWallPost.getUserName());
        contentValues.put(Constants.userImage, studentWallPost.getUserImage());
        contentValues.put(Constants.mediaFile, studentWallPost.getMediaFile());

        // insert to database and close
        db.insert(Constants.collegeWall, null, contentValues);
        db.close();
    }

    /**
     *  This method will update userName column of user table
     */
    public void updateStudentWallImage(String objectId, byte[] mediaFile){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.mediaFile, mediaFile);
        // update the userName column
        db.update(Constants.studentWall, contentValues, "objectId = "+objectId, null);
        db.close();
    }


    /**
     * This method will fetch the user details from
     * database
     *
     * @return
     */
    public HashMap<Object, Object> getUser(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // create hashmap for storing user details
        HashMap<Object, Object> userMap = new HashMap<Object, Object>();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.userTable, null);

        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{
                userMap.put(Constants.campusId, cursor.getString(1));
                userMap.put(Constants.userObjectId, cursor.getString(2));
                userMap.put(Constants.userName, cursor.getString(3));
                userMap.put(Constants.email, cursor.getString(4));
                userMap.put(Constants.phoneNumber, cursor.getString(5));
                userMap.put(Constants.collegeId, cursor.getString(6));
                userMap.put(Constants.year, cursor.getInt(7));
                userMap.put(Constants.branch, cursor.getString(8));
                userMap.put(Constants.semester, cursor.getInt(9));
                userMap.put(Constants.role, cursor.getString(10));
                userMap.put(Constants.mediaCount, cursor.getString(11));
                userMap.put(Constants.media, cursor.getString(12));
/*                userMap.put(Constants.messages, cursor.getInt(10));
                userMap.put(Constants.knowledgeWall, cursor.getInt(11));
                userMap.put(Constants.collegeWall, cursor.getInt(12));
                userMap.put(Constants.studentWall, cursor.getInt(13));
                userMap.put(Constants.moderating, cursor.getInt(14));
                userMap.put(Constants.collegeDirectory, cursor.getInt(15));
                userMap.put(Constants.cseDirectory, cursor.getInt(16));
                userMap.put(Constants.eceDirectory, cursor.getInt(17));
                userMap.put(Constants.eeeDirectory, cursor.getInt(18));
                userMap.put(Constants.itDirectory, cursor.getInt(19));
                userMap.put(Constants.mechDirectory, cursor.getInt(20));
                userMap.put(Constants.civilDirectory, cursor.getInt(21));
                userMap.put(Constants.mbaDirectory, cursor.getInt(22));
                userMap.put(Constants.mcaDirectory, cursor.getInt(23));
                userMap.put(Constants.basicScienceDirectory, cursor.getInt(24));
                userMap.put(Constants.complaintOrFeedback, cursor.getInt(25));*/
            }
            while(cursor.moveToNext());
        }

        db.close();

        return userMap;

    }

    /**
     * This method will check whether user has
     * any privileges assigned
     *
     * @return
     */
    public Boolean isUserPrivileged(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.privilegeTable, null);

        // navigate through cursor and fetch values
        if(cursor.getCount() > 0){

            return true;
        }

        db.close();

        return false;
    }


    /**
     * This method will fetch the user privileges
     *
     * @return
     */
    public Privilege getUserPrivileges(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.privilegeTable, null);

        Privilege privilege = new Privilege();

        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{
                privilege.setPrivilegeId(cursor.getString(1));
                privilege.setUserObjectId(cursor.getString(2));
                privilege.setModerating(cursor.getInt(3));
                privilege.setDirectory(cursor.getString(4));
                privilege.setCreatedAt(cursor.getString(5));
                privilege.setUpdatedAt(cursor.getString(6));

            }
            while(cursor.moveToNext());
        }

        db.close();

        return privilege;

    }

    /**
     * This method will fetch the questions from
     * database for given examID
     *
     * @return
     */
    public ArrayList<Questions> getQuestions(String examId){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // list of questions
        ArrayList<Questions> questionsList = new ArrayList<Questions>();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.questionsTable+" WHERE examId="+examId, null);

        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{

                Questions questions = new Questions();

                questions.setExamId(cursor.getString(1));
                questions.setQuestionId(cursor.getString(2));
                questions.setQuestion(cursor.getString(3));
                questions.setAnswer1(cursor.getString(4));
                questions.setAnswer2(cursor.getString(5));
                questions.setAnswer3(cursor.getString(6));
                questions.setAnswer4(cursor.getString(7));
                questions.setCorrectAnswer(cursor.getString(8));
                questions.setExplanation(cursor.getString(9));

                questionsList.add(questions);
            }
            while(cursor.moveToNext());
        }

        db.close();

        return questionsList;

    }

    /**
     *
     * This method will verify if given examId is present in db
     *
     * @param examId
     * @return
     */
    public boolean verifyExamId(String examId){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.examsTable+" WHERE userObjectId = '"+examId+"'", null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();


        return true;
    }


    /**
     * This method will fetch the studentWall posts from
     * database
     *
     * @return
     */
    public List<StudentWall> getStudentWallPosts(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // list of studentWall post
        List<StudentWall> studentWalls = new ArrayList<StudentWall>();

        // get the query output to cursor
        //Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.studentWall, null);
        Cursor cursor = db.query(Constants.studentWall, null, null, null, null, null, "createdAt ASC");
        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{
                StudentWall studentWall = new StudentWall();

                studentWall.setObjectId(cursor.getString(1));
                studentWall.setPostDescription(cursor.getString(2));
                studentWall.setCreatedAt(new Date(cursor.getString(3)));
                studentWall.setUpdatedAt(new Date(cursor.getString(4)));
                studentWall.setLikes(cursor.getInt(5));
                studentWall.setDislikes(cursor.getInt(6));
                studentWall.setComments(cursor.getInt(7));
                studentWall.setUserObjectId(cursor.getString(8));
                studentWall.setUserImage(cursor.getBlob(8));
                studentWall.setMediaFile(cursor.getBlob(9));

                studentWalls.add(studentWall);

            }
            while(cursor.moveToNext());
        }

        db.close();

        return studentWalls;

    }

    /**
     * This method will fetch the collegeWall posts from
     * database
     *
     * @return
     */
    public List<Wall> getCollegeWallPosts(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // list of studentWall post
        List<Wall> collegeWalls = new ArrayList<Wall>();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.collegeWall, null);

        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{
                Wall collegeWall = new Wall();

               // collegeWall.setObjectId(cursor.getString(1));
                collegeWall.setPostDescription(cursor.getString(2));
                collegeWall.setCreatedAt(cursor.getString(3));
                collegeWall.setUpdatedAt(cursor.getString(4));
                collegeWall.setLikes(cursor.getInt(5));
                collegeWall.setDislikes(cursor.getInt(6));
                collegeWall.setComments(cursor.getInt(7));
                collegeWall.setUserObjectId(cursor.getString(8));
               // collegeWall.setUserImage(cursor.getBlob(8));
               // collegeWall.setMediaFile(cursor.getBlob(9));

                collegeWalls.add(collegeWall);

            }
            while(cursor.moveToNext());
        }

        db.close();

        return collegeWalls;

    }

/*
    *//**
     * This method will return a value accordingly
     * if email of the user is verified or not
     *
     * @return
     *//*
    public boolean isUserVerified(){

        int isVerified = 0;

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        if (cursor.moveToFirst()) {
            do {
                isVerified =  cursor.getInt(4);

            } while (cursor.moveToNext());
        }

        db.close();

        if(isVerified == 1)
            return true;

        return false;
    }

    */


    /**
     *  This method will update privilege column of user table
     */
    public void updatePrivilege(Privilege privilege){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // check if record exists in db for given userObjectId
        // if so update the existing record
        // else insert new record

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.privilegeTable, null);
        if(cursor.getCount()>0) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.userObjectId, privilege.getUserObjectId());
            contentValues.put(Constants.moderating, privilege.getModerating());
            contentValues.put(Constants.directory, privilege.getDirectory());
            contentValues.put(Constants.updatedAt, privilege.getUpdatedAt());
            // update the specified privilege column

            db.update(Constants.privilegeTable, contentValues, "userObjectId = '"+ privilege.getUserObjectId() +"'", null);
        }
        else{

            // map the column value pair
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.privilegeId, privilege.getPrivilegeId());
            contentValues.put(Constants.userObjectId, privilege.getUserObjectId());
            contentValues.put(Constants.moderating, privilege.getModerating());
            contentValues.put(Constants.directory, privilege.getDirectory());
            contentValues.put(Constants.createdAt, privilege.getCreatedAt());
            contentValues.put(Constants.updatedAt, privilege.getUpdatedAt());

            // insert to database and close
            db.insert(Constants.privilegeTable, null, contentValues);
        }
        db.close();
    }

    /**
     *  This method will update userName column of user table
     */
    public void updateUserName(String userName){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.userName, userName);
        // update the userName column
        db.update("user", contentValues, "objectId = "+1, null);
        db.close();
    }

    /**
     *  This method will update email and phoneNumber column of user table
     */
    public void updateEmailAndPhone(String email, String phoneNumber, String objectId){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.email, email);
        contentValues.put(Constants.phoneNumber, phoneNumber);
        // update the userName column
        db.update(Constants.userTable, contentValues, "userObjectId = '"+objectId+"'", null);
        db.close();
    }

    /**
     *  This method will update branch column of user table
     */
    public void updateUserDetails(User user){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.campusId, user.getCampusId());
        contentValues.put(Constants.userObjectId, user.getObjectId());
        contentValues.put(Constants.userName, user.getUserName());
        contentValues.put(Constants.email, user.getEmail());
        contentValues.put(Constants.phoneNumber, user.getPhoneNumber());
        contentValues.put(Constants.collegeId, user.getCollegeId());
        contentValues.put(Constants.year, user.getYear());
        contentValues.put(Constants.branch, user.getBranch());
        contentValues.put(Constants.semester, user.getSemester());
        contentValues.put(Constants.role, user.getRole());

/*
        contentValues.put(Constants.messages, user.getMessages());
        contentValues.put(Constants.collegeWall, user.getCollegeWall());
        contentValues.put(Constants.knowledgeWall, user.getKnowledgeWall());
        contentValues.put(Constants.studentWall, user.getStudentWall());
        contentValues.put(Constants.moderating, user.getModerating());

        contentValues.put(Constants.collegeDirectory, user.getCollegeDirectory());
        contentValues.put(Constants.cseDirectory, user.getCseDirectory());
        contentValues.put(Constants.eceDirectory, user.getEceDirectory());
        contentValues.put(Constants.eeeDirectory, user.getEeeDirectory());
        contentValues.put(Constants.itDirectory, user.getItDirectory());
        contentValues.put(Constants.mechDirectory, user.getMechDirectory());
        contentValues.put(Constants.civilDirectory, user.getCivilDirectory());
        contentValues.put(Constants.basicScienceDirectory, user.getBasicScienceDirectory());
        contentValues.put(Constants.mbaDirectory, user.getMbaDirectory());
        contentValues.put(Constants.mcaDirectory, user.getMcaDirectory());
        contentValues.put(Constants.complaintOrFeedback, user.getComplaintOrFeedback());
*/

        // update the branch column
        db.update(Constants.userTable, contentValues, "userObjectId = '"+ user.getObjectId() +"'", null);
        db.close();
    }

    /**
     *  This method will update about column of user table
    public void updateAbout(String about){

        // get instance of writable database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.about, about);
        // update the about column
        db.update("user", contentValues, "emailVerified = "+1, null);
        db.close();
    }*/


    /**
     * This method will return the user role
     * @return
     */
    public String getUserRole(){

        String category = null;

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Constants.userTable, null);
        if (cursor.moveToFirst()) {
            do {
                category =  cursor.getString(cursor.getColumnIndex(Constants.role));

            } while (cursor.moveToNext());
        }

        db.close();

        return category;
    }

    /**
     * This method will return the list of object ids
     * of posts that are liked/ disliked by current user
     * in the past
     * @return
     */
    public HashMap<String, String> getUserLikesAndDisLikes(){

        // get the readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // create array list for storing object ids of likes
        ArrayList<String> userLikes = new ArrayList<String>();

        HashMap<String, String> userLikesMap = new HashMap<String, String>();

        // get the query output to cursor
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.userLikes, null);

        // navigate through cursor and fetch values
        if(cursor.moveToFirst()){

            do{
                userLikesMap.put(cursor.getString(1), cursor.getString(1));
                userLikes.add(cursor.getString(1));
            }
            while(cursor.moveToNext());
        }

        db.close();

        return userLikesMap ;

    }

}
