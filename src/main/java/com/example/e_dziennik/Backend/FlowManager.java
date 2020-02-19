package com.example.e_dziennik.Backend;

import android.content.Context;

import com.example.e_dziennik.Backend.Database.*;
import com.example.e_dziennik.Backend.Database.TableControllers.*;
import com.example.e_dziennik.Backend.Persistence.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**Klasa udostępnia podstawowe operacje związane z logiką aplikacji
 * poprzez wykonywanie działań na bazie
 * @author Michał*/
public class FlowManager{

    private static Context appContext;
    private static FlowManager manager;
    private static final String SEP = " | ";

    private TableUsers tableUsers;
    private TableSubjects tableSubjects;
    private TableGrades tableGrades;
    private TableStudentSubject tableStudentSubject;

    private FlowManager(){
        if(appContext == null) throw new RuntimeException();
        tableUsers = new TableUsers(appContext);
        tableSubjects = new TableSubjects(appContext);
        tableGrades = new TableGrades(appContext);
        tableStudentSubject = new TableStudentSubject(appContext);
    }

    /**Metoda zwraca instancję klasy będącą singletonem.*/
    public static FlowManager getInstance(){
        if(manager == null) manager = new FlowManager();
        return manager;
    }

    /**
     * Zapisuje kontekst uruchomionej aplikacji. Powinna być zastosowana
     * przed pierwszym użyciem managera
     * @param ctx kontekst aplikacji
     */
    public static void setContext(Context ctx)
    {
        appContext = ctx;
    }

    //GENERAL
    /**Zwraca wszystkich użytkowników z bazy.*/
    public User[] getUsers()
    {
        return tableUsers.getRow("");
    }

    /**
     * Zwraca instancje aktualnie zalogowanego użytkownika
     * @return zalogowany użytkownik
     */
    public User getCurrentUser()
    {
        return tableUsers.getRow(QueryBuilder.eq(User.COL_USER_ID,User.getLoggedCode()))[0];
    }

    /**Zwraca wszystkie istniejące przedmioty.*/
    public Subject[] getSubjects()
    {
        return tableSubjects.getRow("");
    }

    /**
     * Zwraca przedmiot z bazy o danym <code>id</code>
     * @param id id rekordu w bazie
     * @return odpowiadający przedmiot
     */
    public Subject getSubjectById(int id){
        Subject[] results = tableSubjects.getRow(QueryBuilder.eq(Subject.COL_ID,id));
        if(results==null||results.length==0) return null;
        return results[0];
    }

    /**
     * Na podstawie nazwy przedmiotu zwraca jego instancję z bazy
     * @param subjectName nazwa przedmiotu
     * @return instancja przedmiotu
     */
    public Subject getSubjectByName(String subjectName)
    {
        Subject[] results = tableSubjects.getRow(QueryBuilder.eq(Subject.COL_NAME,subjectName));
        if(results==null||results.length==0) return null;
        return results[0];
    }

    /**
     * Zwraca użytkownika z bazy o danym <code>id</code>
     * @param id id rekordu w bazie
     * @return istniejący użytkownik
     */
    public User getUserById(int id)
    {
        User[] results = tableUsers.getRow(QueryBuilder.eq(User.COL_ID, id));
        if(results==null||results.length==0) return null;
        return results[0];
    }

    /**
     * Na podstawie kodu logowania zwraca instancję istniejącego użytkownika
     * @param userCode kod do logowania
     * @return istniejący użytkownik
     */
    public User getUserByCode(String userCode)
    {
        User[] results = tableUsers.getRow(QueryBuilder.eq(User.COL_USER_ID, userCode));
        if(results==null||results.length==0) return null;
        return results[0];
    }

    /**
     * Zwraca rolę dla określonego użytkownika po jego kodzie logowania
     * @param userCode kod logowania
     * @return jedna z możliwych ról
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public String getRoleForUser(String userCode) throws Exception
    {
        User user = getUserByCode(userCode);
        if(user == null) throw new Exception("Bad login");
        else if(user.getRole()==null) throw new Exception("Użytkownik nie ma roli");
        return user.getRole();
    }

    /**Zwraca wszystkie stworzone konta dla nauczycieli*/
    public User[] getTeachers()
    {
        return tableUsers.getRow(QueryBuilder.eq(User.COL_ROLE, User.ROLES[1]));
    }

    /**Zwraca wszystkie stworzone konta dla uczniów*/
    public User[] getStudents()
    {
        return tableUsers.getRow(QueryBuilder.eq(User.COL_ROLE, User.ROLES[2]));
    }

    //STUDENT

    /**
     * Zwraca wszystkie stworzone oceny dla ucznia
     * @param studentId id ucznia
     * @param subjectId id przedmiotu
     * @return oceny wstawione uczniowi
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public Grade[] getGradesForStudent(int studentId, int subjectId) throws Exception
    {
        StudentSubject[] studentSubjects =
                tableStudentSubject.getRow(QueryBuilder.eq(StudentSubject.COL_STUDENT,studentId));
        if(studentSubjects==null || studentSubjects.length==0) throw new Exception("No subjects for student");
        int id = -1;
        for(StudentSubject element: studentSubjects)
            if(element.getSubjectId()==subjectId) id = element.getId();
        if(id==-1) throw new Exception("Student is to assigned to given subject");

        Grade[] studentGrades = tableGrades.getRow(QueryBuilder.eq(Grade.COL_SUBJ,id));
        if(studentGrades==null||studentGrades.length==0) throw new Exception("No grades");
        return studentGrades;
    }

    /**
     * Zwraca wszystkie przedmiotu na które jest zapisany dany uczeń
     * @param studentId id ucznia
     * @return przedmioty
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public Subject[] getSubjectsForStudent(int studentId) throws Exception{
        StudentSubject[] studentSubjects = tableStudentSubject
                .getRow(QueryBuilder.eq(StudentSubject.COL_STUDENT,studentId));
        if(studentSubjects.length==0) throw new Exception("No subject for student");
        Subject[] subjects = new Subject[studentSubjects.length];
        for(int i=0; i<subjects.length; i++)
        {
            subjects[i] = getSubjectById(studentSubjects[i].getSubjectId());
        }
        return subjects;
    }

    //TEACHER

    /**
     * Zwraca wszystkie przedmioty z których uczy ustalony nauczyciel
     * @param teacherId id nauczyciela
     * @return przedmioty
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public Subject[] getSubjectsForTeacher(int teacherId) throws Exception
    {
        Subject[] subjects = tableSubjects.getRow(QueryBuilder.eq(Subject.COL_TEACHER,teacherId));
        if(subjects==null || subjects.length==0) throw new Exception("Teacher has no subjects");
        return subjects;
    }

    /**
     * Zwraca wszystkich uczniów zapisanych na dany przedmiot
     * @param subjectId id przedmiotu
     * @return uczniowie
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public User[] getStudentsForSubject(int subjectId) throws Exception
    {
        Subject subject = getSubjectById(subjectId);
        StudentSubject[] studentsForSubject = tableStudentSubject.getRow(
                QueryBuilder.eq(StudentSubject.COL_SUBJECT, subjectId));
        if(studentsForSubject.length==0) throw new Exception("Subject has no students");
        User[] students = new User[studentsForSubject.length];
        for(int i=0; i<studentsForSubject.length; i++)
        {
            students[i]= getUserById(studentsForSubject[i].getStudentId());
        }
        return students;
    }


    private StudentSubject getPairwiseStudentSubject(int studentId, int subjectId) throws Exception
    {
        StudentSubject[] pairs = tableStudentSubject
                .getRow(QueryBuilder.eq(StudentSubject.COL_STUDENT,studentId));
        if(pairs==null||pairs.length==0) throw new Exception("No such student for subject");
        for(StudentSubject element: pairs)
            if(element.getSubjectId()==subjectId) return element;
        return null;
    }

    /**
     * Zwraca ocenę o danym <code>id</code>
     * @param gradeId id oceny
     * @return ocena
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public Grade getGradeById(int gradeId) throws Exception
    {
        Grade[] grades = tableGrades.getRow(QueryBuilder.eq(Grade.COL_ID, gradeId));
        if(grades==null||grades.length==0) throw new Exception("No grade for given id");
        return grades[0];
    }

    /**
     * Dodaje ocenę zgodnie z podanymi danymi
     * @param studentId id ucznia któremu wstawiamy ocenę
     * @param teacherId id nauczyciela który wstawia ocenę
     * @param subjectId id przedmiotu z którego dodana jest ocena
     * @param gradeValue znak oceny zgodnie z przyjętymi ocenami
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public void addGrade(int studentId, int teacherId, int subjectId, String gradeValue) throws Exception
    {
        addGrade(studentId,teacherId,subjectId,gradeValue,"");
    }

    /**
     * Dadaje ocenę zgodnie z podanymi danymi
     * @param studentId id ucznia któremu dodajemy ocenę
     * @param teacherId id nauczyciela który wstawia ocenę
     * @param subjectId id przedmiotu z którego dodajemy ocenę
     * @param gradeValue wartość oceny zgodnie z przyjętymi ocenami
     * @param description dodatkowy opis dotyczący wstawionej oceny
     * @return wartość logiczna opisująca powodzenie działania
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public boolean addGrade(int studentId, int teacherId, int subjectId, String gradeValue, String description)
            throws Exception
    {
        if(!Grade.validateGradeValue(gradeValue)) throw new Exception("Improper grade!");
        StudentSubject subjectForStudent = getPairwiseStudentSubject(studentId, subjectId);
        if(subjectForStudent==null) throw new Exception("Student is not assigned to subject");
        int subId = subjectForStudent.getId();

        Grade toAdd = new Grade();
        toAdd.setTeacherId(teacherId);
        toAdd.setStudentSubject(subId);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        toAdd.setAdded(dateFormat.format(date));

        toAdd.setModified("");
        toAdd.setDescription(description);
        toAdd.setGrade(gradeValue);

        Grade.validateGrade(toAdd);
        return tableGrades.createRow(toAdd);
    }

    /**
     * Zmienia istniejącą w bazie ocenę
     * @param gradeId id istniejącej oceny
     * @param newGradeValue nowa wartość dla istniejącej oceny
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public void changeGrade(int gradeId, String newGradeValue) throws Exception
    {
        changeGrade(gradeId, newGradeValue, null);
    }

    /**
     * Zmienia istniejącą oceną, dodatkowo dopisując dla tej zmiany komentarz
     * @param gradeId id oceny w bazie
     * @param newGradeValue nowa wartość oceny
     * @param comment komentarz zmiany
     * @return wartość logiczna opisująca powodzenie działania
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public boolean changeGrade(int gradeId, String newGradeValue, String comment) throws Exception
    {
        Grade toChange = getGradeById(gradeId);
        if(!Grade.validateGradeValue(newGradeValue)) throw new Exception("Improper grade value!");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        toChange.setGrade(newGradeValue);
        toChange.setModified(dateFormat.format(date));
        toChange.setDescription(comment);
        return tableGrades.updateRow(toChange);
    }

    /**
     * Usuwą ocenę o zadanym id
     * @param gradeId id oceny do usunięcia
     * @return wartość logiczna opsująca sukces działania
     */
    public boolean removeGrade(int gradeId)
    {
        return tableGrades.deleteRow(QueryBuilder.eq(Grade.COL_ID, gradeId));
    }

    /**
     * Przypisuje ucznia o zadanym id do istniejącego przedmiotu
     * @param studentId id studenta do przypisania
     * @param subjectId id przedmiotu do którego zostanie przypisany
     * @return wartość logiczna opisująca sukces operacji
     */
    public boolean assignStudentToSubject(int studentId, int subjectId)
    {
        StudentSubject pair = new StudentSubject();
        pair.setStudentId(studentId);
        pair.setSubjectId(subjectId);
        return tableStudentSubject.createRow(pair);
    }

    /**
     * Usuwa ucznia z istniejącego przedmiotu
     * @param studentId id ucznia do usunięcia
     * @param subjectId id przedmiotu z którego ma zostać usunięty
     * @return wartość logiczna opisująca sukces operacji
     * @throws Exception wyjątek spowodowany niepoprawnymi danymi
     */
    public boolean expelStudentFromSubject(int studentId, int subjectId) throws Exception
    {
        StudentSubject pair = getPairwiseStudentSubject(studentId, subjectId);
        if(pair==null) throw new Exception("Student is not assigned to the subject!");
        return tableStudentSubject.deleteRow(QueryBuilder.eq(StudentSubject.COL_ID, pair.getId()));
    }

    //ADMIN

    public String createAccount(String name, String secondName, String role) throws Exception
    {
        if(!User.validateRole(role)) throw new Exception("BAD ROLE");

        User account = new User();
        account.setName(name);
        account.setSecondName(secondName);
        account.setRole(role);
        account.setDescription("");

        if(!User.validateCredentials(account)) throw new Exception("EMPTY INFO");
        account.setCode(User.generateCode());
        tableUsers.createRow(account);
        return account.getCode();
    }

    public boolean removeAccount(String userCode)
    {
        return tableUsers.deleteRow(QueryBuilder.eq(User.COL_USER_ID, userCode));
    }

    public boolean assignTeacherToSubject(int teacherId, String subjectName)
    {
        Subject subject = tableSubjects.getRow(QueryBuilder.eq(Subject.COL_NAME,subjectName))[0];
        subject.setTeacher(teacherId);
        return tableSubjects.updateRow(subject);
    }

    public boolean createSubject(String subjectName)
    {
        Subject subject = new Subject();
        subject.setName(subjectName);
        subject.setTeacher(-1);//stworzony bez przypisanego nauczycela
        return tableSubjects.createRow(subject);

    }

    public boolean removeSubject(String subjectName){
        return tableSubjects.deleteRow(QueryBuilder.eq(Subject.COL_NAME, subjectName));
    }

    public int getNumberOfAccounts()
    {
        return tableUsers.getNumberOfRows();
    }

    public int getNumberOfSubjects()
    {
        return tableSubjects.getNumberOfRows();
    }

    //TABLES DRAWING

    public String getTableUsersLegend()
    {
        return User.COL_ID+","+User.COL_NAME+","+
                User.COL_SEC_NAME+","+User.COL_ROLE+","+
                User.COL_USER_ID+" Rows:"+getNumberOfAccounts();
    }

    public String drawTableUsers()
    {
        StringBuilder table = new StringBuilder();
        User[] users = getUsers();
        for(int i=0;i< users.length; i++)
            table.append(users[i].getId()+ SEP +users[i].getName()+ SEP
                    +users[i].getSecondName()+ SEP + users[i].getRole()+ SEP
                    +users[i].getCode()+"\n");
        return table.toString();
    }

    public String getTableSubjectsLegend()
    {
        return Subject.COL_ID+","+Subject.COL_NAME+","+Subject.COL_TEACHER
                +" Rows:"+getNumberOfSubjects();
    }

    public String drawTableSubjects()
    {
        StringBuilder table = new StringBuilder();
        Subject[] subjects = getSubjects();
        for(int i=0;i<subjects.length; i++)
        {
            table.append(subjects[i].getId()+ SEP +subjects[i].getName()+
                    SEP +subjects[i].getTeacher()+"\n");
        }
        return table.toString();
    }

    public String drawGrades(Grade[] grades)
    {
        if(grades==null||grades.length==0) return "";
        StringBuilder builder = new StringBuilder();
        double avg = 0.0;
        for(int i=0; i<grades.length; i++){
            builder.append(grades[i].getGrade());
            if(i<grades.length-1) builder.append(",");
            avg += Integer.valueOf(grades[i].getGrade());
        }
        avg /= grades.length;
        builder.append("\nśrednia: ").append(avg);
        return builder.toString();
    }
}
