package utils;

import android.widget.Toast;

/**
 * Created by Pavan on 4/14/15.
 */
public class Constants {

    // Parse credentials
    public static final String parseApplicationId = "7AkT2DE1Xmw3HFSbNN2SG5cLvDt7QHPZREQE99NM";
    public static final String parseClientKey = "qoP8MaOPCMZ6jXHJ93NkvoVwjrFXYBUTmf3JLuxa";
    public static final String key = "smartcampus_key";

    public static final String appName = "SVECW";
    public static final String app_SharedPreferences = "Smart_SharedPreference";

    public static final String fontName = "fonts/roboto.ttf";

    public static final String role = "role";
    public static final String student = "Student";
    public static final String faculty = "Faculty";
    public static final String admin = "Admin";
    public static final String moderator = "Moderator";

    public static final String faculty_secure_code = "fac015svecwPV";
    public static final String admin_secure_code = "admin015svecwPV";

    public static final String loginUserName = "username";
    public static final String loginPassword = "password";

    public static final String charset = "UTF-8";
    public static final String KEY = "key";
    public static final String userId = "userId";
    public static final String campusId = "campusId";
    public static final String sessionId = "sessionId";
    public static final String userName = "username";
    public static final String email = "email";
    public static final String emailVerified = "emailVerified";
    public static final String collegeId = "collegeId";
    public static final String gcm_regId = "gcm_regId";
    public static final String branch = "branch";
    public static final String semester = "semester";
    public static final String year = "year";
    public static final String department = "department";
    public static final String phoneNumber = "phoneNumber";
    public static final String id = "id";
    public static final String to = "to";
    public static final String isGroup = "isGroup";
    public static final String Broadcast = "Broadcast";
    public static final String Group = "Group";
    public static final String Direct = "Direct";
    public static final String groupId = "groupId";
    public static final String groupName = "groupName";
    public static final String messageId = "messageId";
    public static final String message = "message";
    public static final String messageType = "messageType";
    public static final String createdAt = "createdAt";
    public static final String updatedAt = "updatedAt";
    public static final String userImage = "userImage";
    public static final String mediaId = "mediaId";
    public static final String privilegeId = "privilegeId";
    public static final String privilege = "privilege";
    public static final String Complaint = "Complaint";
    public static final String Feedback = "Feedback";

    public static final String status = "status";
    public static final String details = "details";
    public static final String post = "post";
    public static final String privileges = "privileges";
    public static final String departments = "departments";
    public static final String sub_departments = "sub_departments";

    // intetnal DB tables
    public static final String userTable = "user";
    public static final String privilegeTable = "privilege";
    public static final String userLikesTable = "userLikes";
    public static final String commentsTable = "comments";
    public static final String examsTable = "exams";
    public static final String questionsTable = "questions";

    // extenal DB tables
    public static final String users = "users";
    public static final String Session = "Session";
    public static final String mediaTable = "Media";
    public static final String mediaCount = "mediaCount";
    public static final String media = "media";
    public static final String linkUrl = "linkUrl";
    public static final String linkTitle = "linkTitle";
    public static final String linkCaption = "linkCaption";
    public static final String location = "location";
    public static final String feeling = "feeling";
    public static final String isActive = "isActive";
    public static final String collegeWallTable = "CollegeWall";
    public static final String studentWallTable = "StudentWall";
    public static final String knowledgeWallHeading = "Knowledge wall";
    public static final String globalInfoTable = "GlobalInfo";
    public static final String collegeWallLikesTable = "CollegeWallLikes";
    public static final String studentWallLikesTable = "StudentWallLikes";
    public static final String messagesTable = "Messages";
    public static final String StudentWallComments = "StudentWallComments";
    public static final String CollegeWallComments = "CollegeWallComments";
    public static final String Complaint_FeedbackTable = "Complaint_Feedback";
    public static final String Exams_Table = "Exams";
    public static final String Exam_Questions = "Exam_Questions";
    public static final String Learn = "Learn";
    public static final String Academics = "Academics";
    public static final String Placements = "Placements";
    public static final String MessageGroupsUserMapping = "MessageGroupsUserMapping";

    public static final String userObjectId = "userObjectId";
    public static final String fromUserObjectId = "fromUserObjectId";
    public static final String toUserObjectId = "toUserObjectId";
    public static final String feedbackId = "feedbackId";
    public static final String reportId = "reportId";
    public static final String objectId = "objectId";
    public static final String fromuser = "fromuser";
    public static final String user = "user";
    public static final String participant = "Participant";
    public static final String userLikes = "userLikes";
    public static final String description = "description";
    public static final String link = "link";
    public static final String postDescription = "postDescription";
    public static final String mediaFile = "mediaFile";
    public static final String relativeId = "relativeId";
    public static final String alumni = "alumni";
    public static final String postId = "postId";
    public static final String like = "like";
    public static final String fullImage = "fullImage";
    public static final String wallId = "wallId";
    public static final String wallType = "wallType";
    public static final String infoId = "infoId";
    public static final String title = "title";
    public static final String likes = "likes";
    public static final String dislikes = "dislikes";
    public static final String position = "position";
    public static final String commentId = "commentId";
    public static final String comment = "comment";
    public static final String comments = "comments";
    public static final String commentCount = "commentCount";
    public static final String alumniPost = "alumniPost";
    public static final String studentYear = "studentYear";
    public static final String privilegeNumber = "privilegeNumber";
    public static final String type = "type";
    public static final String name = "name";
    public static final String expiryDate = "expiryDate";
    public static final String numberOfQuestions = "numberOfQuestions";
    public static final String examId = "examId";
    public static final String questionId = "questionId";
    public static final String question = "question";
    public static final String questions = "questions";
    public static final String answer1 = "answer1";
    public static final String answer2 = "answer2";
    public static final String answer3 = "answer3";
    public static final String answer4 = "answer4";
    public static final String explanation = "explanation";
    public static final String correctAnswer = "correctAnswer";
    public static final String questionsCount = "questionCount";
    public static final String correctAnswers = "correctAnswers";
    public static final String wrongAnswers = "wrongAnswers";
    public static final String topic = "topic";
    public static final String answer = "answer";
    public static final String timetable = "Time table";
    public static final String syllabus = "Syllabus";
    public static final String classnotes = "Class notes";
    public static final String module = "module";
    public static final String company = "company";
    public static final String count = "count";
    public static final String alphabet = "alphabet";
    public static final String error = "error";
    public static final String success = "success";
    public static final String valueBack = "valueBack";
    public static final String groupDetails = "groupDetails";
    public static final String searchType = "searchType";
    public static final String searchTerm = "searchTerm";
    public static final String fromBranch = "fromBranch";
    public static final String toBranch = "toBranch";
    public static final String fromYear = "fromYear";
    public static final String toYear = "toYear";
    public static final String fromSemester = "fromSemester";
    public static final String toSemester = "toSemester";
    public static final String limit = "limit";
    public static final String reactionId = "reactionId";


    public static final String msgType = "msgType";
    public static final String msgTypeGateway = "gateWay";
    public static final String msgTypePhone = "phone";


    public static final String C = "C";
    public static final String CPlusPlus = "C++";
    public static final String Java = "JAVA";
    public static final String HTML = "HTML";
    public static final String JavaScript = "JAVASCRIPT";
    public static final String Android = "ANDROID";
    public static final String dbms = "DBMS";
    public static final String os = "OS";
    public static final String unix = "UNIX";


    public static final String category = "category";

    // comment table
    public static final String commentText = "commentText";
    public static final String postObjectId = "postObjectId";

    public static final String STUDENT = "Student";
    public static final String COLLEGE = "College";
    public static final String KNOWLEDGE = "Knowledge";
    public static final String ALUMNI = "Alumni";

    public static final int IMG_PICK = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100; // activity request codes
    public static final int EDIT_PROFILE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;

    public static final String null_indicator = "-";

    public static final String FONTNAME = "fonts/sans.ttf";

    public static final String ALL = "All";
    public static final String MANAGEMENT = "MANAGEMENT";
    public static final String BASICSCIENCE = "BASIC SCIENCE";
    public static final String CIVIL = "CIVIL";
    public static final String CSE = "CSE";
    public static final String ECE = "ECE";
    public static final String EEE = "EEE";
    public static final String IT = "IT";
    public static final String MECHANICAL = "MECHANICAL";
    public static final String MBA = "MBA";
    public static final String MCA = "MCA";

    public static final String MESSAGES = "P1";
    public static final String KNOWLEDGE_WALL = "P2";
    public static final String COLLGEE_WALL = "P3";
    public static final String STUDENT_WALL = "P4";
    public static final String MODERATING = "P5";
    public static final String COLLEGE_DIRECTORY = "P6";
    public static final String CSE_DIRECTORY = "P7";
    public static final String ECE_DIRECTORY = "P8";
    public static final String EEE_DIRECTORY = "P9";
    public static final String IT_DIRECTORY = "P10";
    public static final String MECHANICAL_DIRECTORY = "P11";
    public static final String CIVIL_DIRECTORY = "P12";
    public static final String MBA_DIRECTORY = "P13";
    public static final String MCA_DIRECTORY = "P14";
    public static final String MANAGEMENT_DIRECTORY = "P15";
    public static final String BASIC_SCIENCE_DIRECTORY = "P16";

    public static final String messages = "messages";
    public static final String knowledgeWall = "knowledgeWall";
    public static final String collegeWall = "collegeWall";
    public static final String studentWall = "studentWall";
    public static final String moderating = "moderating";
    public static final String directory = "directory";
    public static final String studentDirectory = "studentDirectory";
    public static final String collegeDirectory = "collegeDirectory";
    public static final String cseDirectory = "cseDirectory";
    public static final String eceDirectory = "eceDirectory";
    public static final String eeeDirectory = "eeeDirectory";
    public static final String itDirectory = "itDirectory";
    public static final String mechDirectory = "mechDirectory";
    public static final String civilDirectory = "civilDirectory";
    public static final String mbaDirectory = "mbaDirectory";
    public static final String mcaDirectory = "mcaDirectory";
    public static final String basicScienceDirectory = "basicScienceDirectory";
    public static final String complaintOrFeedback = "complaintOrFeedback";

    public static final String guest = "guest";
    public static final String founder = "founder";
    public static final String chairman = "chairman";
    public static final String viceChairman = "viceChairman";
    public static final String secretary = "secretary";
    public static final String jointSecretary = "jointSecretary";
    public static final String principal = "principal";
    public static final String vicePrincipal = "vicePrincipal";

    public static final String awards = "awards";
    public static final String vishuRD = "vishuRD";
    public static final String missionRD = "missionRD";
    public static final String jkc = "jkc";
    public static final String atl = "atl";
    public static final String ibm = "ibm";
    public static final String texas = "texas";
    public static final String talentSprint = "talentSprint";
    public static final String kCenter = "kCenter";
    public static final String radio = "radio";
    public static final String foreign = "foreign";
    public static final String womenTech = "womenTech";
    public static final String iucee = "iucee";
    public static final String wifi = "wifi";
    public static final String counseling = "counseling";
    public static final String teqip = "teqip";
    public static final String mou = "mou";
    public static final String club = "club";
    public static final String qeee = "qeee";


    public static final String technology = "Technology";
    public static final String electronics = "Electronics";
    public static final String business = "Business";
    public static final String startups = "Startups";
    public static final String news = "News";
    public static final String events = "Events";
    public static final String newsandevents = "News and Events";

    public static final String driveInfo = "driveInfo";
    public static final String uploads = "My uploads";
    public static final String shared = "Shared";


}
