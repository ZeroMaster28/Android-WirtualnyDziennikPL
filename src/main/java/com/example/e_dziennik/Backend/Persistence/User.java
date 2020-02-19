package com.example.e_dziennik.Backend.Persistence;

/**Obiekty odpowiadające wpisom w bazie dla użytkowników*/
public class User {
    public static final String TABLE_NAME = "USERS";

    public static final String COL_ID = "ID";
    private int id;

    public static final String COL_NAME = "NAME";
    private String name;

    public static final String COL_ROLE = "ROLE";
    private String role;

    public static final String COL_SEC_NAME = "SECOND_NAME";
    private String secondName;

    public static final String COL_USER_ID = "CODE";
    private String code;

    public static final String COL_DESC = "DESCRIPTION";
    private String description;

    public static final String[] ROLES = {"admin","nauczyciel","uczen"};

    //kod odpowiadający zalogowanemu właśnie userowi
    private static String currentUserCode = "";

    public User() {
    }

    public User(int id, String name, String role, String secondName, String description) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.secondName = secondName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role!=null?role.toLowerCase():null;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String generateCode()
    {
        long time = (long) (System.currentTimeMillis()/1000.0);
        StringBuilder builder = new StringBuilder();
        long temp;
        while(time>0)
        {
            temp = time%100;
            if(temp<=62) builder.append((char) (temp+60));
            else builder.append(temp);
            time = (time - temp)/100L;
        }
        return builder.toString();
    }

    public static boolean validateRole(String role)
    {
        String simpleRole = role.toLowerCase();
        for(String availableRole: ROLES)
            if(simpleRole.equals(availableRole)) return true;
        return false;
    }

    public static boolean validateCredentials(User user)
    {
        if(user.getName()==null || user.getName().equals("")) return false;
        if(user.getSecondName()==null || user.getSecondName().equals("")) return false;
        return true;
    }

    public static String getLoggedCode()
    {
        return currentUserCode;
    }

    public static void logIn(String loginCode)
    {
        currentUserCode = loginCode;
    }
}
