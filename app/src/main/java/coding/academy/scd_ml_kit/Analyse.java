package coding.academy.scd_ml_kit;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Analyse {
    private static final String TAG = "Analyse";

    private Context context;


    public Analyse(Context context) {
        this.context = context;
    }

    private String result = "";


    public static <T> List<T> convertArrayToList(T array[]) {

        // Create an empty List
        List<T> list = new ArrayList<>();

        // Iterate through the array
        for (T t : array) {
            // Add each element into the list
            list.add(t);
        }

        // Return the converted List
        return list;
    }

    private static final String ARG_CODE = "CODE";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    MutableLiveData<String> CodeLiveData = new MutableLiveData<>();


    public MutableLiveData<String> analysCode(final String code) {
        getKeyWord(code);
        return CodeLiveData;
    }

    private void toLines(String line) {
        // line = line.replace("\n"," ");

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '{') {
                line = line.substring(i + 1);
            }
        }


        Log.d(TAG, "toLines: " + line);

        List<String> lines = new ArrayList<>();
        StringBuilder wordStringBuilder = new StringBuilder();

        for (char c : line.toCharArray()) {

            if (c == '\n' || c == ';') {

                if (c == ';') {
                    wordStringBuilder.append(c);
                }

                lines.add(wordStringBuilder.toString());
                wordStringBuilder = new StringBuilder();


            } else if (c == '}') {
                break;
            } else {
                wordStringBuilder.append(c);
            }

        }

        for (String s : lines) {
            getKeyWord(s);
        }

    }

    private boolean isBlock(String line) {
        boolean isBlock = false;

        if (line.startsWith("if") || line.startsWith("for") || line.startsWith("while") || line.startsWith("do")
                || line.startsWith("private void") || line.startsWith("public void") || line.startsWith("protect void") || line.startsWith("public static void")
                || line.startsWith("private static void") || line.startsWith("protect static void") || line.startsWith("public static ")
                || line.startsWith("private static ") || line.startsWith("protect static ") || line.startsWith("public final ") || line.startsWith("private final ")
                || line.startsWith("protect final ") || line.startsWith("protect final void") || line.startsWith("private final void") || line.startsWith("public final void")) {
            isBlock = true;
        }


        return isBlock;
    }

    private void getKeyWord(String line) {
        String keyWord = "";
        line = line.trim();

        if (isBlock(line)) {
            if (line.startsWith("if")) {
                keyWord = "if";
                /////////////////////////////
            } else if (line.startsWith("for")) {
                keyWord = "for";
            } else if (line.startsWith("while")) {
                keyWord = "while";
            } else if (line.startsWith("do")) {
                keyWord = "dowhile";
                /////////////////////////////////
            } else if (line.startsWith("public int") || line.startsWith("private int") || line.startsWith("protect int")
                    || line.startsWith("static int") || line.startsWith("final int") ||
                    line.startsWith("public float") || line.startsWith("private float") || line.startsWith("protect float")
                    || line.startsWith("static float") || line.startsWith("final float") ||
                    line.startsWith("public double") || line.startsWith("private double ") || line.startsWith("protect double")
                    || line.startsWith("static double") || line.startsWith("final double") ||
                    line.startsWith("public short") || line.startsWith("private short") || line.startsWith("protect short")
                    || line.startsWith("static short") || line.startsWith("final short") ||
                    line.startsWith("public byte") || line.startsWith("private byte") || line.startsWith("protect byte")
                    || line.startsWith("static byte") || line.startsWith("final byte") ||
                    line.startsWith("public long") || line.startsWith("private long") || line.startsWith("protect long")
                    || line.startsWith("static long") || line.startsWith("final long") ||
                    line.startsWith("public String") || line.startsWith("private String") || line.startsWith("protect String")
                    || line.startsWith("static String") || line.startsWith("final String") ||
                    line.startsWith("public char") || line.startsWith("private char") || line.startsWith("protect char")
                    || line.startsWith("static char") || line.startsWith("final char") ||
                    line.startsWith("public boolean") || line.startsWith("private boolean") || line.startsWith("protect boolean")
                    || line.startsWith("static boolean") || line.startsWith("final boolean")) {
                keyWord = "modifier";

            } else if (line.startsWith("private void") || line.startsWith("public void") || line.startsWith("protect void") || line.startsWith("public static void")
                    || line.startsWith("private  static void") || line.startsWith("protect static void") || line.startsWith("public static ")
                    || line.startsWith("private static ") || line.startsWith("protect static ") || line.startsWith("public final ") || line.startsWith("private final ")
                    || line.startsWith("protect final ") || line.startsWith("protect final void") || line.startsWith("private final void") || line.startsWith("poblic final void")) {
                keyWord = "function";
            }


            Log.d("TAG", "Function --------------------------------- : " + keyWord);


            toFirebase(keyWord, line);

            toLines(line);

        } else {

            if (line.startsWith("int[]") || line.startsWith("float[]")
                    || line.startsWith("String[]") || line.startsWith("char[]")
                    || line.startsWith("double[]")) {
                keyWord = "Array";
                ////////////////////////////////////////
            } else if (line.startsWith("System.out.print") || line.startsWith("System.out.println")) {
                keyWord = "prints";
                ///////////////////////////////////////////////
            /*} else if (line.startsWith("switch")) {
                keyWord = "switch";  */
           /* } else if (line.startsWith("return")) {
                keyWord = "return";*/
                //else if (line.contains("system.out.print")){
                //  keyWord = "prints";
                /////////////////////////////////
            } else if (line.startsWith("int") || line.startsWith("  " + "int")
                    || line.startsWith(" " + "int") || line.startsWith("   " + "int")) {
                keyWord = "int";
            } else if (line.startsWith("long")) {
                keyWord = "long";
            } else if (line.startsWith("short")) {
                keyWord = "short";
            } else if (line.startsWith("byte")) {
                keyWord = "byte";
            } else if (line.startsWith("float")) {
                keyWord = "float";
            } else if (line.startsWith("double")) {
                keyWord = "double";
            } else if (line.startsWith("String") || line.startsWith("string")) {
                keyWord = "String";
            } else if (line.startsWith("char")) {
                keyWord = "char";
            } else if (line.startsWith("boolean")) {
                keyWord = "boolean";
                //////////////////////////////////
            }

            Log.d("TAG", "Is Key Word ++++: " + keyWord);

            toFirebase(keyWord, line);
        }

    }

    private void toFirebase(String keyWord, final String line) {

        firebaseFirestore
                .collection("regex").whereEqualTo("regex_name", keyWord)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<String> regexList = new ArrayList<>();
                boolean islineCorrect = false;
                List<String> suggestion = new ArrayList<>();

                for (DocumentSnapshot d : queryDocumentSnapshots) {

                    try {
                        regexList = (List<String>) d.get("regex");

                        if (d.get("suggestion") != null) {
                            suggestion = (List<String>) d.get("suggestion");

                            for (String s : suggestion) {
                                Log.d(TAG, "suggestion: " + s);
                            }
                        } else {
                            Log.d(TAG, "suggestion: NULL");
                        }

                    } catch (Exception x) {
                        Log.d(TAG, "suggestion : " + x.getMessage());
                    }

                }

                if (regexList != null || !regexList.isEmpty()) {


                    islineCorrect = checkErrors(regexList, line);

                    if (!islineCorrect) {
                        result += "xxx" + line + "xxx" + "\n" + getSuggestion(suggestion) + "\n";
                        Log.d("TAG", "Wrong | " + line);

                    } else {
                        result += line + "\n";
                        Log.d("TAG", "Correct | " + line);

                    }

                    CodeLiveData.setValue(result);


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });


    }


    public void analyseNormalText(String text, final TextView errorTextView, final TextView suggestionTextView) {
        errorTextView.setText("");
        suggestionTextView.setText("");

        getKeyWord(text);


        /*
        //final Intent intent = new Intent(context, CodeViewActivity.class);
        result = "";


        String keyWord = "";

        final List<String> lines = convertArrayToList(text.split("\n"));


        Log.e("camera", "size = " + lines.size() + "  " + lines.toString());

        // الدوران على السطور لتحليل النص داخلهم

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            final int j = i;
            Log.e("camera", "line = " + line);
            // فحص الكلمة الاولى لمعرفة نوع البيانات

            if (line.startsWith("if")) {
                keyWord = "if";
                toLines(line);
                /////////////////////////////
            } else if (line.startsWith("for")) {
                keyWord = "for";
            } else if (line.startsWith("while")) {
                keyWord = "while";
            } else if (line.startsWith("do")) {
                keyWord = "dowhile";
                /////////////////////////////////
            } else if (line.startsWith("int") || line.startsWith("float")
                    || line.startsWith("String") || line.startsWith("char")
                    || line.startsWith("double")) {
                keyWord = "Array";
                ////////////////////////////////////////
            } else if (line.startsWith("class")) {
                keyWord = "class";
                /////////////////////////////////////////////
            } else if (line.startsWith("system.out.print") || line.startsWith("system.out.println")) {
                keyWord = "prints";
                ///////////////////////////////////////////////
            /*} else if (line.startsWith("switch")) {
                keyWord = "switch";  */
        /* } else if (line.startsWith("return")) {
                keyWord = "return";
                //else if (line.contains("system.out.print")){
                //  keyWord = "prints";
                /////////////////////////////////
            } else if (line.startsWith("public int") || line.startsWith("private int") || line.startsWith("protect int")
                    || line.startsWith("static int") || line.startsWith("final int") ||
                    line.startsWith("public float") || line.startsWith("private float") || line.startsWith("protect float")
                    || line.startsWith("static float") || line.startsWith("final float") ||
                    line.startsWith("public double") || line.startsWith("private double ") || line.startsWith("protect double")
                    || line.startsWith("static double") || line.startsWith("final double") ||
                    line.startsWith("public short") || line.startsWith("private short") || line.startsWith("protect short")
                    || line.startsWith("static short") || line.startsWith("final short") ||
                    line.startsWith("public byte") || line.startsWith("private byte") || line.startsWith("protect byte")
                    || line.startsWith("static byte") || line.startsWith("final byte") ||
                    line.startsWith("public long") || line.startsWith("private long") || line.startsWith("protect long")
                    || line.startsWith("static long") || line.startsWith("final long") ||
                    line.startsWith("public String") || line.startsWith("private String") || line.startsWith("protect String")
                    || line.startsWith("static String") || line.startsWith("final String") ||
                    line.startsWith("public char") || line.startsWith("private char") || line.startsWith("protect char")
                    || line.startsWith("static char") || line.startsWith("final char") ||
                    line.startsWith("public boolean") || line.startsWith("private boolean") || line.startsWith("protect boolean")
                    || line.startsWith("static boolean") || line.startsWith("final boolean")) {
                keyWord = "modifier";
///////////////////////////////////////////////////////////////

            } else if (line.startsWith("int") || line.startsWith("  " + "int")
                    || line.startsWith(" " + "int") || line.startsWith("   " + "int")) {
                keyWord = "int";

            } else if (line.startsWith("long")) {
                keyWord = "long";
            } else if (line.startsWith("short")) {
                keyWord = "short";
            } else if (line.startsWith("byte")) {
                keyWord = "byte";
            } else if (line.startsWith("float")) {
                keyWord = "float";
            } else if (line.startsWith("double")) {
                keyWord = "double";
            } else if (line.startsWith("String")) {
                keyWord = "String";
            } else if (line.startsWith("char")) {
                keyWord = "char";
            } else if (line.startsWith("boolean")) {
                keyWord = "boolean";
                //////////////////////////////////
            } else if (line.startsWith("private void") || line.startsWith("public void") || line.startsWith("protect void") || line.startsWith("public static void")
                    || line.startsWith("private  static void") || line.startsWith("protect static void") || line.startsWith("public static ")
                    || line.startsWith("private static ") || line.startsWith("protect static ") || line.startsWith("public final ") || line.startsWith("private final ")
                    || line.startsWith("protect final ") || line.startsWith("protect final void") || line.startsWith("private final void") || line.startsWith("poblic final void")) {
                keyWord = "function";
            }
///////////////////////////////////////////////////////

            else {
                keyWord = "";
            }

            final String finalKeyWord = keyWord;
            Log.e("camera", "finalKeyWord = " + finalKeyWord);
            firebaseFirestore
                    .collection("regex").whereEqualTo("regex_name", keyWord)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    List<String> regexList = new ArrayList<>();
                    boolean islineCorrect = false;


                    for (DocumentSnapshot d : queryDocumentSnapshots) {

                        try {
                            regexList = (List<String>) d.get("regex");

                        } catch (Exception x) {
                            Log.d(TAG, "Exception : " + x.getMessage());
                        }
                        Log.d(TAG, "النوع : " + d.getString("regex_name") + " | " + d.getString("item_name"));

                    }

                    if (regexList != null || !regexList.isEmpty()) {

                        islineCorrect = checkErrors(regexList, line);

                        if (!islineCorrect) {
                            // result += line + "\n" +  getSuggestion(finalKeyWord) ;
                            result += line + "\n";

                        } else {
                            result += line + "\n";
                        }

                        //  result += " -> " + line + " "+ islineCorrect + "\n";
                        // if(j==lines.size()-1){


                        Bundle bundle = new Bundle();

                        bundle.putString(ARG_CODE, result);
                        //intent.putExtras(bundle);
                        //context.startActivity(intent);
                        //  }


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }

        */

    }

    private boolean checkErrors(List<String> regexList, String text) {

        text = text.trim();
        boolean correct = false;
        try {

            for (String regex : regexList) {
                Pattern pt = Pattern.compile(regex);
                Matcher mt = pt.matcher(text);
                if (mt.matches()) {
                    correct = true;
                    break;
                }
            }
        } catch (Exception x) {
            Log.d(TAG, "خطأ عند معالجة الريجكس: " + x.getMessage());
        }


        return correct;
    }


    String suggestions;

    //"suggestion"
    private String getSuggestion(List<String> suggestionsList) {

        String text = "/* Suggestion \n ";
        for (String s : suggestionsList) {
            text += s + "\n";
        }
        suggestions = text + "\n */";

        return suggestions;
    }
}
