/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.BosDIMapping;
import model.CI;
import model.Category;
import model.Discipline;
import model.Faculty;
import model.FacultyStudent;
import model.Fees;
import model.PaymentDetails;
import model.Program;
import model.StudentDetails;
import model.StudentDetailsMapping;
import model.StudentEnrollmentMapping;
import model.StudentQualification;
import model.StudentReport;
import model.StudentReport1;
import model.StudentType;
import model.StudentUser;
import model.User;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import validator.LogData;

/**
 *
 * @author Shekhar
 */
public class studentDaoImpl extends HibernateDaoSupport implements studentDao {
    private LogData logData = new LogData();
    public boolean addPersonalDetails(StudentDetails studentDetails,String loggedInUser) {
        boolean flag=false;
        try{
            logData.writeToLogFile(loggedInUser, "addPersonalDetails", "Inside addPersonalDetails","");
        //    synchronized(this){
                getHibernateTemplate().save(studentDetails);
                flag=true;
                logData.writeToLogFile(loggedInUser, "addPersonalDetails", "Flag :"+flag,"");
        //    }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "addPersonalDetails", "Exception occured",e.getMessage());
            flag=false;
            logData.writeToLogFile(loggedInUser, "addPersonalDetails", "Flag :"+flag,e.getMessage());
        }
        return flag;
    }

    public  int getdisIdByciCode(String ciCode,String loggedInUser){
       int ciId=0;
       try{
           String sql="Select  ciId from cimaster  where ciCode='"+ciCode+ " ' ";
          List cimId= getHibernateTemplate().find(sql);
         ciId=Integer.parseInt(cimId.get(0).toString());
        }
        catch(Exception e){  
            System.out.println("Exception"+e);
        }   
       return ciId;
       
   }
    
    public  int getdisIdBypgmCode(String pgmCode,String loggedInUser){
       int pgmId=0;
       try{
           String sql="Select  programId from programmaster where programCode='"+pgmCode+ " ' ";
          List programId= getHibernateTemplate().find(sql);
         pgmId=Integer.parseInt(programId.get(0).toString());
        }
        catch(Exception e){  
            System.out.println("Exception"+e);
        }   
       return pgmId;
       
   }
   public  int getdisIdBydisCode(String disCode,String loggedInUser){
       int disId=0;
       try{
           String sql="Select  disciplineId from disciplinemaster where disciplineCode='"+disCode+ " ' ";
          List disciplineId= getHibernateTemplate().find(sql);
         disId=Integer.parseInt(disciplineId.get(0).toString());
        }
        catch(Exception e){  
            System.out.println("Exception"+e);
        }   
       return disId;
       
   }
    
    public boolean updateStudentDetails(StudentDetails studentDetails,String loggedInUser) {
        boolean flag=false;
        try{
            logData.writeToLogFile(loggedInUser, "updateStudentDetails", "Inside updateStudentDetails","");
          //  synchronized(this){ 
                getHibernateTemplate().update(studentDetails);
                flag=true;
                logData.writeToLogFile(loggedInUser, "updateStudentDetails", "Flag:"+flag,"");
           // }
        }
        catch(Exception e){            
            logData.writeToLogFile(loggedInUser, "updateStudentDetails", "Exception occured",e.getMessage());
            flag=false;
            logData.writeToLogFile(loggedInUser, "updateStudentDetails", "Flag:"+flag,e.getMessage());
        }    
        return flag;
    }
    
    
    
 public boolean isDuplicateEnrollmentNo(String enrollmentNo,String loggedInUser){
          boolean isDuplicate =true;
          List studenList=getHibernateTemplate().find("from studentmaster where enrolmentNo=?",enrollmentNo);
                  if(studenList.size()==0){
                      isDuplicate=false;
                  }
                  return isDuplicate;
     }
     
     
     
     
    public boolean isDuplicateUser(String userName, String mode, int personalId, String loggedInUser) {
        boolean isDuplicate =true;
        logData.writeToLogFile(loggedInUser, "isDuplicateUser", "Inside isDuplicateUser","");
        try{
            if(mode.equals("add")){
                logData.writeToLogFile(loggedInUser, "isDuplicateUser", "Mode :"+mode,"");
                List userList=getHibernateTemplate().find("from User where userName=?",userName);
                if(userList.size()==0){
                    isDuplicate=false;
                }
            }
            else if(mode.equals("edit")){
                logData.writeToLogFile(loggedInUser, "isDuplicateUser", "Mode :"+mode,"");            
                List userList=getHibernateTemplate().find("from User where userName=?",userName);
                if(userList.size()==0){
                    isDuplicate=false;
                }
                else{
                    StudentUser studentUser=null;
                    List studentUserList=new ArrayList();
                    List studentList=getHibernateTemplate().find("select a.personalId,a.fullName,b.userId,b.userName  from studentmaster a, User b where b.userId=a.userId and b.userName=?",userName);
                    if(studentList.size()>0){
                        Iterator is=studentList.iterator();
                        while(is.hasNext()){
                            Object[] row =(Object[]) is.next();
                            //java.util.Date bDate=(java.util.Date)row[8];
                            //java.util.Date jDate=(java.util.Date)row[9];
                            studentUser=new StudentUser(Integer.parseInt(row[0].toString()), row[1].toString(), Integer.parseInt(row[2].toString()), row[3].toString());
                            //facultyDetails=new FacultyDetails(Integer.parseInt(row[0].toString()), row[1].toString(),Integer.parseInt(row[2].toString()), row[3].toString(),Integer.parseInt(row[4].toString()), row[5].toString(),Integer.parseInt(row[6].toString()), row[7].toString(),row[8].toString(), row[9].toString(),Integer.parseInt(row[10].toString()), row[11].toString(),row[12].toString(), row[13].toString(),Integer.parseInt(row[14].toString()), row[15].toString(), row[16].toString(), row[17].toString(), row[18].toString(), row[19].toString(), row[20].toString(), row[21].toString());
                            studentUserList.add(studentUser);
                        }
                    }
                    if(studentUserList.size()==0){
                        isDuplicate=true;
                    }
                    else{
                        StudentUser student=(StudentUser)studentUserList.get(0);
                        if(student.getPersonalId()==personalId){
                            isDuplicate=false;
                        }
                        else{
                            isDuplicate=true;
                        }
                    }
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "isDuplicateUser", "Exception occured",e.getMessage());
         }
        return isDuplicate; 

    }
    
    public StudentDetails viewStudentDetailsById(int personalId,String loggedInUser) {
        StudentDetails studentDetails=null;
        logData.writeToLogFile(loggedInUser, "viewStudentDetailsById", "Inside viewStudentDetailsById","");
        try{
            List studentList=getHibernateTemplate().find("from studentmaster where personalId=?",personalId);
            if(studentList.size()>0){                
                studentDetails=(StudentDetails)studentList.get(0);
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsById", "Exception occureud",e.getMessage());
        }   
        return studentDetails;
    }
    
 
    public List viewAllStudentDetails(String loggedInUser) {
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentDetails", "Inside viewAllStudentDetails","");
        try{
            studentList=getHibernateTemplate().find("from Student Details where status='Active'");
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudentDetails", "Exception occured",e.getMessage());
        } 
        return studentList;
    }

    public boolean addStudentQualification(StudentQualification sq,String loggedInUser) {
        boolean flag=false;
        logData.writeToLogFile(loggedInUser, "addStudentQualification", "Inside addStudentQualification","");
        try{
          //  synchronized(this){
                getHibernateTemplate().save(sq);
                flag=true;
                logData.writeToLogFile(loggedInUser, "addStudentQualification", "Flag:"+flag,"");
          //  }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "addStudentQualification", "Exception occured",e.getMessage());
            flag=false;
            logData.writeToLogFile(loggedInUser, "addStudentQualification", "Flag:"+flag,"");
        }   
      
      return flag;
    }

    public boolean updateStudentCourse(StudentDetails studentdetails,String loggedInUser) {
         boolean flag=true;
         try
         {
            logData.writeToLogFile(loggedInUser, "updateStudentCourse", "Inside updateStudentCourse","");
            getHibernateTemplate().update(studentdetails);
            logData.writeToLogFile(loggedInUser, "updateStudentCourse", "Student details modified successfully","");
         }
         catch(Exception e){
            flag=false;
            logData.writeToLogFile(loggedInUser, "updateStudentCourse", "Exception occured.Failed to modify student details",e.getMessage());
        }    
          return flag;
    }

    public Fees getFeesById(Program programId, StudentType studentTypeId, Category categoryId,String academicYear,String loggedInUser) {
        Fees fees=null;
        int enrollfee=0;
        int thesisfee=0;
        try
        {
            logData.writeToLogFile(loggedInUser, "getFeesById", "Inside getFeesById","");
            List feeList=getHibernateTemplate().find("from Fees where programId=? and studentTypeId=? and categoryId=? and academicYear=?", programId,studentTypeId,categoryId,academicYear);
            if(feeList.size()>0)
            {
                fees=(Fees)feeList.get(0);
                enrollfee=fees.getEnrollfee();
                thesisfee=fees.getThesisfee();
                logData.writeToLogFile(loggedInUser, "getFeesById", "Retrived Fees for successfully ","");
            }   
                else{
                logData.writeToLogFile(loggedInUser, "getFeesById", "No Fees details found","");
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getFeesById", "Exception occureud. Failed to retrieve Fees",e.getMessage());
        }   
        return fees;
    }

    public List getStudentsByCiAndDiscipline(CI ciId, Discipline disciplineId,Program program, String loggedInUser) {
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentsByCiAndDiscipline", "Inside getStudentsByCiAndDiscipline","");
        try{
            String sql="from studentmaster where ciId="+ciId.getCiId()+" and disId="+disciplineId.getDisciplineId()+" ";
            if(program!=null){
                sql=sql+" and programId="+ program.getProgramId()+" ";
            }
            if(!loggedInUser.equals("")){
                    sql=sql + "and fullName like '%"+loggedInUser+"%'";
                }
            sql=sql+"and status='Active'";  
            studentList=getHibernateTemplate().find(sql);
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentsByCiAndDiscipline", "Exception occured",e.getMessage());
        } 
        return studentList;
    }

    public List viewAllStudentDetailsByCriteria(int ciId, int disId, int programId, String enrolNo, String fullName,String academicYear,String fileNumber ,String status,String loggedInUser,long offset,long maxResult,String subType) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentDetailsByCritera", "Inside viewAllStudentDetailsByCritera","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{String sql="";
                sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from CImaster c,Disciplinemaster d,User u,Programmaster p,";
                sql=sql+"Studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId  and  u.userId=s.userId ";
                if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                } 
                 
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }if(!subType.equals("")){
                    sql=sql + "and s.subType='"+subType+"' ";
                }
                if(status.equalsIgnoreCase("Yes")){
                 sql=sql+" and  ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                 
             }
             
             else if(status.equalsIgnoreCase("No")){
                  sql=sql + "  and   not ((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='TransFerred') ))";
             }
              else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+"  and  (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign')  ) ";
                 
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
         sql=sql+" and (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Transferred')  ) ";
                 
             }
                sql=sql+" order by s.enrolmentNo";
            
//                 if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
//            {
//            sql=sql+" and u.status='"+status+"' order by s.fullName";
//            }
//            else
//            {
//               sql=sql+" order by s.fullName";
//            }
////                   if(!status.equals("")){
//                       sql=sql+" and  u.userId=s.userId and u.status='"+status+"' order by s.fullName";
//                   }
//                   else{
//            /*    if(!fileNumber.equals("")){
//                    sql=sql + "and s.fileNumber='"+fileNumber+ "' ";
//                }*/
//                sql=sql+" and  u.userId=s.userId and u.status='Active' order by s.fullName";
//                   }
                Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
                if(studentList.size()>0){
                    Iterator is=studentList.iterator();
                    while(is.hasNext()){
                        Object[] row =(Object[]) is.next();
                        studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                        studentDetailsList.add(studentdetailsmapping);
                    }
                }
            //}
            
            
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudentDetailsByCritera", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;
 
    }
    
    
    
     public List viewAllStudentDetailsByCriteria(int ciId, int disId, int programId, String enrolNo, String fullName,String academicYear,String fileNumber ,String status,int userId,String loggedInUser,long offset,long maxResult,String subType) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentDetailsByCritera", "Inside viewAllStudentDetailsByCritera","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{String sql="";
                sql="select distinct s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from cimaster  c,disciplinemaster d,User u,programmaster p,";
                sql=sql+"studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  u.userId=s.userId ";
                if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(ciId==0){
                  sql=sql+   "and c.ciId IN (select ciId from ADCIMapping where userId="+userId+")";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                }
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }
                  if(!subType.equals("")){
                    sql=sql + "and s.subType  '"+subType+"'";
                }
          if(status.equalsIgnoreCase("Yes")){
                 sql=sql+" and  ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                 
             }
             
             else if(status.equalsIgnoreCase("No")){
                  sql=sql + "  and   not ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='TransFerred') ))";
             }
              else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+"  and  (s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign')  ) ";
                 
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
         sql=sql+" and (s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Transferred')  ) ";
                 
             }
            
          sql=sql+" order by s.enrolmentNo";
                Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
                
                if(studentList.size()>0){
                    Iterator is=studentList.iterator();
                    while(is.hasNext()){
                        Object[] row =(Object[]) is.next();
                        studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                        studentDetailsList.add(studentdetailsmapping);
                    }
                }
             
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudentDetailsByCritera", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;
    }
    
     public List viewAllStudentsByName(String studentName, String loggedInUser) {
        List facultyList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentsByName", "Inside viewAllStudentsByName","");
        try{
            //String sql="from Faculty where facultyName like '"+facultyName+"%' order by facultyName";
           // String sql="from Faculty order by facultyName";
            //System.out.println("Sql is :"+sql);
           facultyList=getHibernateTemplate().find("from studentmaster where fullName like ? order by fullName","%"+studentName+"%");
            //facultyList=getHibernateTemplate().find(" from Faculty order by facultyName");
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudentsByName", "Exception occured",e.getMessage());
        }
        return facultyList;
    }
    public List viewAllEnrolmentNos(String enrolNo, String loggedInUser) {
        List facultyList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllEnrolmentNos", "Inside viewAllEnrolmentNos","");
        try{
            //String sql="from Faculty where facultyName like '"+facultyName+"%' order by facultyName";
           // String sql="from Faculty order by facultyName";
            //System.out.println("Sql is :"+sql);
           facultyList=getHibernateTemplate().find("from studentmaster where enrolmentNo like ? order by enrolmentNo",enrolNo+"%");
            //facultyList=getHibernateTemplate().find(" from Faculty order by facultyName");
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllEnrolmentNos", "Exception occured",e.getMessage());
        }
        return facultyList;
    }

    public StudentDetails viewStudentDetailsByUserId(int userId, String loggedInUser) {
        StudentDetails studentDetails=null;
        logData.writeToLogFile(loggedInUser, "viewStudentDetailsByUserId", "Inside viewStudentDetailsById","");
        try{
            List studentList=new ArrayList();
            studentList=getHibernateTemplate().find("from studentmaster where userId.userId=?",userId);
            if(studentList.size()>0){                
                studentDetails=(StudentDetails)studentList.get(0);
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByUserId", "Exception occureud",e.getMessage());
        }   
        return studentDetails;
    }
public List viewNewStudentsByName(String studentName, String loggedInUser) {
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentsByName", "Inside viewAllStudentsByName","");
        try{            
           studentList=getHibernateTemplate().find("from studentmaster where enrolNo=null and fullName like ? order by fullName", studentName+"%");            
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudentsByName", "Exception occured",e.getMessage());
        }
        return studentList;
    }

    public StudentDetails viewStudentDetailsByName(String fullName, String loggedInUser) {
        StudentDetails studentDetails=new StudentDetails();
        logData.writeToLogFile(loggedInUser, "viewStudentDetailsByName", "Inside viewStudentDetailsById","");
        try{
            List studentList=getHibernateTemplate().find("from studentmaster where fullName=?",fullName);
            if(studentList.size()>0){                
                studentDetails=(StudentDetails)studentList.get(0);
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByName", "Exception occureud",e.getMessage());
        }   
        return studentDetails;

    }
   
    public boolean isDuplicateEnrollment(String enrolNo, String loggedInUser) {
        List enrollList=new ArrayList();
        boolean isDuplicate=true;
        logData.writeToLogFile(loggedInUser, "isDuplicateEnrollment", "Inside isDuplicateEnrollment","");
        try{                                       
                enrollList=getHibernateTemplate().find("from studentmaster where enrolmentNo=?",enrolNo);
                if(enrollList.size()==0){
                    isDuplicate=false;
                }
                else{
                    isDuplicate=true;
                    /*StudentDetails studentdetails=(StudentDetails)enrollList.get(0);
                    if(studentdetails.getEnrolNo().equals(enrolNo))
                    {
                        isDuplicate=true;
                    }
                    else{
                        isDuplicate=false;
                    }*/
                }
            
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "isDuplicateEnrollment", "Exception occured","Exception");
        }
        return isDuplicate;
    }

    public List getStudentDetailsByUser(User userId,int dsId, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentDetailsByCI", "Inside getStudentDetailsByCI","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="";
            
            
            sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from cimaster  c,disciplinemaster d,User u,programmaster p,";
                sql=sql+"studentmaster s,CIDAMapping z where c.ciId=s.ciId and d.disciplineId=s.disId and ";
                 sql=sql + "p.programId=s.programId and z.ciId=s.ciId  and u.userId=z.userId  and z.userId="+userId.getUserId()+"";
                 
                 if(dsId!=0){
                   sql=sql + "  and z.disId=s.disId";
                 }
                
            
            
            
       /*     sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql+"CIDAMapping a,UserRole b,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,User u,Role r where u.userId=a.userId and c.ciId=s.ciId and ";
            sql=sql+"d.disciplineId=s.disId and p.programId=s.programId and u.userId=b.userId and c.ciId=a.ciId and r.roleId=b.roleId ";
            sql=sql + " and u.userId="+userId.getUserId()+" order by d.disciplineName,p.programName,s.fullName";*/
                
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentDetailsByCI", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;
    }
public List viewStudentDetailsByUser(User userId, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentDetailsByCI", "Inside getStudentDetailsByCI","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="";
            sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from cimaster  c,disciplinemaster d,User u,programmaster p,";
                sql=sql+"studentmaster s,CIDAMapping z where c.ciId=s.ciId and d.disciplineId=s.disId and ";
                 sql=sql + "p.programId=s.programId and z.ciId=s.ciId  and u.userId=z.userId  and z.userId="+userId.getUserId()+"";
                 
          /*  sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql+"CIDAMapping a,UserRole b,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,User u,Role r where u.userId=a.userId and c.ciId=s.ciId and ";
            sql=sql+"d.disciplineId=s.disId and p.programId=s.programId and u.userId=b.userId and c.ciId=a.ciId and r.roleId=b.roleId ";
            sql=sql + " and u.userId="+userId.getUserId()+" order by d.disciplineName,p.programName,s.fullName";*/
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentDetailsByCI", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;
    }

    
    public List getStudentDetailsByUser(User userId, String viewedBy, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentDetailsByUser", "Inside getStudentDetailsByUser","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="";
            sql="select s.personalId,coalesce(s.enrolNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            if(viewedBy.equals("DA")){
                sql=sql+"CIDAMapping a,";
            }
            else if(viewedBy.equals("AD")){
                sql=sql+"ADCIMapping a,";
            }
            sql=sql + "UserRole b,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,User u,Role r where u.userId=a.userId and c.ciId=s.ciId and ";
            sql=sql+"d.disciplineId=s.disId and p.programId=s.programId and u.userId=b.userId and c.ciId=a.ciId and r.roleId=b.roleId ";
            sql=sql + " and u.userId="+userId.getUserId()+" order by s.fullName";
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentDetailsByUser", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

    }

    
    public List getStudentDetailsByFaculty(Faculty facultyId, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentDetailsByFaculty", "Inside getStudentDetailsByFaculty","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql+"FacultyStudent a,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,Faculty f where f.facultyId=a.facultyId.facultyId ";
            sql=sql+"and s.personalId=a.personalId.personalId and c.ciId=s.ciId.ciId and d.disciplineId=s.disId.disciplineId and p.programId=s.programId ";
            sql=sql+" and a.facultyId="+facultyId.getFacultyId()+" order by s.fullName";
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentDetailsByFaculty", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

    }
    

 public  List getAllStudentPrintList(int ciId,int disId,int programId,String enrolNo,String fullName,String academicYear,String fileNumber,String status,String loggedInUser,long offset,long maxResult,String subType){
   
      List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getAllStudentPrintList", "Inside getStudentDetailsByUser","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="";
            sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql + "PhdDetailsmaster  a,cimaster  c,disciplinemaster d,programmaster p,studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId ";
            sql=sql+"and p.programId=s.programId and s.personalId=a.personalId and a.acaProgram='Yes' and (a.printChk = null or a.printChk='No')  ";
             if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                } 
                 
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }if(!subType.equals("")){
                    sql=sql + "and s.subType='"+subType+"' ";
                }
                 if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"' order by s.fullName";
            }
            else
            {
               sql=sql+" order by s.fullName";
            }
             Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getAllStudentPrintList", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

     
     
  } 
    
    
    
   public List getAllStudentPrintList(int ciId,int disId,int programId,String enrolNo,String fullName,String academicYear,String fileNumber,String status,String loggedInUser,String subType){
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getAllStudentPrintList", "Inside getStudentDetailsByUser","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="";
            sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql + "PhdDetailsmaster  a,cimaster  c,disciplinemaster d,programmaster p,studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId ";
            sql=sql+"and p.programId=s.programId and s.personalId=a.personalId and a.acaProgram='Yes' and (a.printChk = null or a.printChk='No')  ";
             if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                } 
                 
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }if(!subType.equals("")){
                    sql=sql + "and s.subType='"+subType+"' ";
                }
                 if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"' order by s.fullName";
            }
            else
            {
               sql=sql+" order by s.fullName";
            }
            studentList=getHibernateTemplate().find(sql);
//            if(studentList.size()>0){
//                Iterator is=studentList.iterator();
//                while(is.hasNext()){
//                    Object[] row =(Object[]) is.next();
//                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
//                    studentDetailsList.add(studentdetailsmapping);
//                }
            //}
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getAllStudentPrintList", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentList;
    }

    public List getStudentListWithoutEnrollmentNo(String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentListWithoutEnrollmentNo", "Inside getStudentListWithoutEnrollmentNo","");
        StudentEnrollmentMapping studentenrollmentmapping=null;
        try{ 
            String sql="select s.personalId,s.fullName,c.ciId,c.ciName,c.ciCode,s.academicYear,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName,p.programCode ";
            sql=sql+"from studentmaster s, ApprovalProcess ap,cimaster  c,disciplinemaster d,programmaster p ";
            sql= sql+"where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s.enrolmentNo=' ' and ";
            sql=sql+"s.personalId=ap.personalId.personalId and";
            sql=sql+" ap.approvalTypeId.approvalTypeId=7  and approvalStatus='Approved' order by s.fullName";
            studentList=getHibernateTemplate().find(sql);  
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentenrollmentmapping = new StudentEnrollmentMapping(Integer.parseInt(row[0].toString()),row[1].toString(),Integer.parseInt(row[2].toString()),row[3].toString(),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),row[11].toString());
                    studentDetailsList.add(studentenrollmentmapping);
                }
            }
        }
           catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentListWithoutEnrollmentNo", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }
    
   /* public List viewStudentReportByCritera(int ciId, int disId, int programId, int studentTypeId, int categoryId, int guideId, String gender, int disabilityId, String feesType, String academicYear, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=null;
        StudentReport studentReport=null;
        try{
            String sql="from a,coalesce(b.facultyName,'--') from (select s.personalId,coalesce(s.enrolNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,";
            sql=sql+"p.programId,p.programName,s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,";
            sql=sql+"d1.disability,s.gender,s.academicYear from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ";
            sql=sql+"where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disabilityId=s.physicalChallanged ";
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            sql=sql+"order by c.ciName,d.disciplineName,p.programName,s.fullName)a ";
            sql=sql+" left outer join ";
            sql=sql+" (select a1.facultyName,b1.personalId from Faculty a1,";
            sql=sql+"GuideStudent b1 where a1.facultyId=b1.guideId group by a1.facultyId)b";
            sql=sql+ "on a.personalId=b.personalId";
            System.out.println("SQL is :"+sql);
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString());
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCritera", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }*/

    public List viewStudentReportByCriteria(int ciId,int disId,int programId,int studentTypeId,int categoryId,String gender,int disabilityId,String feesType,String academicYear,String year,String reportSubmitted,String ogceCompleted,String vivaVoceCompleted,String extensionFor,String thesisSubmitted,String courseCompleted,String certificateIssued,String loggedInUser,String status) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        int approvalTypeId=0;
        try{            
            String sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
            if(year!=null && (programId==2 ||programId==3 || programId==4)){
                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+" and examType='vivavoce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    sql=sql+",app1.approvalType,app.approvalStatus ";
                }
                else{
                    sql=sql+",'--','--'";
                }
            }
            else{
                sql=sql+",'--','--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3 and Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(!courseCompleted.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes' and Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' ";
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }                   
            if(!certificateIssued.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes' and Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' ";
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ,User u  ";            
            if(!feesType.equals("Select")){
                sql=sql+ ",PaymentDetails pd ";
            }
            if(!extensionFor.equals("0")){
                sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
            }
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and u.userId=s.userId   ";
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!feesType.equals("Select")){
                if(feesType.equals("Enrollment")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
                }
                if(feesType.equals("Thesis")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
                }
            }
            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            if(programId==5 || programId==6 || programId==10 || programId==11){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
            }
            if(programId==2 || programId==3 || programId==4){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                }
            }
            if(programId==5 || programId==6){
                if(!ogceCompleted.equals("Select")){
                    if(ogceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(ogceCompleted.equals("Yes")){
                        sql=sql + "and  s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" ";
                      //  sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
                    }
                    if(ogceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+" ";
                    }
                }
                if(!vivaVoceCompleted.equals("Select")){
                    if(vivaVoceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(vivaVoceCompleted.equals("Yes")){
                        sql=sql + "and s.personalId in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" ";
                    }
                    if(vivaVoceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    if(extensionFor.equals("1"))approvalTypeId=2;
                    else if(extensionFor.equals("2"))approvalTypeId=3;
                    else if(extensionFor.equals("3"))approvalTypeId=4;
                    sql=sql+" and s.personalId=ex.personalId and s.personalId=app.personalId and s.academicYear='"+academicYear+"' ";
                    sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                    sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                if(!thesisSubmitted.equals("Select")){
                    if(thesisSubmitted.equals("All")){
                        sql=sql + " and ((s.personalId in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3)and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    else if(thesisSubmitted.equals("Yes")){
                        sql=sql + " and s.personalId in (select distinct sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" ";
                    }
                    else if(thesisSubmitted.equals("No")){
                        sql=sql + " and s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(!courseCompleted.equals("Select")){
                if(courseCompleted.equals("All")){
                    sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"')) or ";
                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+")) ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(courseCompleted.equals("Yes")){
                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.acaProgram='Yes')";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(courseCompleted.equals("No")){
                    //sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
                    sql=sql + " and  ";
                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+" ) ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(!certificateIssued.equals("Select")){
                if(certificateIssued.equals("All")){
                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where s.academicYear='"+academicYear+"' or comDate is null) or ";
                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear="+academicYear+" and)) ";
                    if(programId>0){
                        sql=sql+"and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("Yes")){
                    sql=sql + " and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.printChk='Yes') ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("No")){
                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear="+academicYear+"";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            sql=sql+" and u.status='"+status+"'  order by c.ciName,d.disciplineName,p.programName,s.fullName";
            else
               sql=sql+" order by c.ciName,d.disciplineName,p.programName,s.fullName";
            System.out.println("SQL is :"+sql);
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){           
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString(),row[24].toString(),"--");
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    /*if(!extensionFor.equals("0")){
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString());
                    }
                    else{
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),"--","--");
                    }*/
                    studentDetailsList.add(studentReport);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCriteria", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }
    
    public long viewStudentReportcount(int ciId,int disId,int programId,int studentTypeId,int categoryId,String gender,int disabilityId,String feesType,String academicYear,String year,String reportSubmitted,String ogceCompleted,String vivaVoceCompleted,String extensionFor,String thesisSubmitted,String courseCompleted,String certificateIssued,String loggedInUser,String noReason,String status,int university,String subType,String completeType)
    {
       
        StudentReport studentReport=null;
        long count=0;
        int approvalTypeId=0;
        try{            
            String sql="select count (distinct s.personalId) as count";
//    //        sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
//            if(year!=null && (programId==2 ||programId==3 || programId==4)){
//                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
//                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else{
//               // sql=sql+",'--'";
//            }
//            if(programId==5 || programId==6){
//                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
//            }
//            else{
//                //sql=sql+",'--'";
//            }
//            if(programId==5 || programId==6){
//                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+" and examType='vivavoce')>0 then 'Yes' else 'No' End";
//            }
//            else{
//               // sql=sql+",'--'";
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
//                if(!extensionFor.equals("0")){
//                    sql=sql+",app1.approvalType,app.approvalStatus ";
//                }
//                else{
//                   // sql=sql+",'--','--'";
//                }
//            }
//            else{
//             //   sql=sql+",'--','--'";
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
//                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3 and Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else{cre
//               // sql=sql+",'--'";
//            }
//            if(!courseCompleted.equals("Select")){
//                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes' and Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                if(programId>0){
//                    sql=sql+"and s.programId="+programId+" ";
//                }
//                sql=sql + ")>0 then 'Yes' else 'No' End";
//            }
//            else{
//                //sql=sql+",'--'";
//            }                   
//            if(!certificateIssued.equals("Select")){
//                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes' and Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                if(programId>0){
//                    sql=sql+"and s.programId="+programId+" ";
//                }
//                sql=sql + ")>0 then 'Yes' else 'No' End";
//            }
//            else{
//             //   sql=sql+",'--'";
//            }
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ,User u  ";        
            if(university>0){
                sql=sql+",StudentQualification sq ";
            }
            if(!feesType.equals("Select")){
                sql=sql+ ",PaymentDetails pd ";
            }
            if(!extensionFor.equals("0")){
                //sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
                sql=sql+",Extension ex ";
            }
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and u.userId=s.userId ";
            if(university>0){
                sql=sql+"and s.personalId=sq.personalId  ";
            }
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!feesType.equals("Select")){
                if(feesType.equals("Enrollment")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
                }
                if(feesType.equals("Thesis")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
                }
            }
            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            if(programId==5 || programId==6 || programId==10 || programId==11){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
            }
            if(programId==2 || programId==3 || programId==4){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                }
            }
            if(programId==5 || programId==6){
                if(!ogceCompleted.equals("Select")){
                    if(ogceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(ogceCompleted.equals("Yes")){
                        sql=sql + "and  s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" ";
                      //  sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
                    }
                    if(ogceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+" ";
                    }
                }
                if(!vivaVoceCompleted.equals("Select")){
                    if(vivaVoceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(vivaVoceCompleted.equals("Yes")){
                      sql=sql + "and s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" ";
                    }
                    if(vivaVoceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    if(extensionFor.equals("1"))approvalTypeId=2;
                    else if(extensionFor.equals("2"))approvalTypeId=3;
                    else if(extensionFor.equals("3"))approvalTypeId=4;
                       sql=sql+" and s.personalId=ex.personalId  and s.academicYear='"+academicYear+"' and ex.year='"+extensionFor+"' ";
                sql=sql+"  and s.programId="+programId +" ";
                    //  sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                  //  sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
               
                    //sql=sql+" and s.personalId=ex.personalId and s.personalId=app.personalId and s.academicYear='"+academicYear+"' ";
                  //  sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                  //  sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                if(!thesisSubmitted.equals("Select")){
                    if(thesisSubmitted.equals("All")){
                        sql=sql + " and ((s.personalId in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3)and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    else if(thesisSubmitted.equals("Yes")){
                        sql=sql + " and s.personalId in (select distinct sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" ";
                    }
                    else if(thesisSubmitted.equals("No")){
                        sql=sql + " and s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(!courseCompleted.equals("Select")){
//                if(courseCompleted.equals("All")){
//                    sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"')) or ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+")) ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                 if(courseCompleted.equals("Yes")){
                    
                    if(completeType.equals("Select"))
                    {
                        sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                      if(academicYear!="")
                      {
                            sql=sql+" and s.academicYear='"+academicYear+"'";
                      }
                    if(programId>0)
                    {
                        sql=sql+" and s.programId="+programId+" ";
                     }
                    }
                 else if(completeType.equals("Certificate Issued")){
                     
                   sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
                  if(academicYear!=""){
                      
                      sql=sql+"and s.academicYear='"+academicYear+"'";
                  }
                  if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                   }
               }
                    
                      else if(completeType.equals("PDC Issued")){
                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and p.comDate IS NULL and su.upId=17)";
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                    
                }
//                  else if(courseCompleted.equals("Certificate Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//               else if(courseCompleted.equals("PDC Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17)";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                else if(courseCompleted.equals("No")){
                  
                    //sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
                 
                   sql=sql + " and not ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='TransFerred') ))";
                        
//s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign') 
//   sql=sql+" and (s.personalId not in (select personalId from PhdDetails) or s.personalId in (select  personalId FROM PhdDetailsmaster  where acaProgram IS NULL  ) ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    } 
               
                    
                    
                    
                    
                    
                    
//                    
//                    if(noReason.equals("Select")){
//                    sql=sql + " and  ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                
//                  else if(noReason.equals("Terminated")){
//                       sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Terminated') ) ";
//                     if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//              else if(noReason.equals("Resign")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Resign')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//                  else if(noReason.equals("TransFerred")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='TransFerred')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
                }
                 
                 
                    else if(courseCompleted.equals("Terminated/Resign")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
          else if(courseCompleted.equals("Transferred")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Transferred')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
                 
            }
            if(!certificateIssued.equals("Select")){
                if(certificateIssued.equals("All")){
                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where s.academicYear='"+academicYear+"' or comDate is null) or ";
                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear="+academicYear+" and)) ";
                    if(programId>0){
                        sql=sql+"and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("Yes")){
                    sql=sql + " and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.printChk='Yes') ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("No")){
                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear="+academicYear+"";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(university>0){
                
                sql=sql+" and sq.university="+university+" and year=(select max(year) from StudentQualification where personalId=s.personalId) ";
            }
            if(!subType.equals("")){
                        sql=sql+" and s.subType='"+subType+"' ";
                    }
            if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"'  order by c.ciName,d.disciplineName,p.programName,s.fullName";
            }
            else
            {
               sql=sql+" order by c.ciName,d.disciplineName,p.programName,s.fullName";
            }
           // sql=sql+" order by c.ciName,d.disciplineName,p.programName,s.fullName";
           // System.out.println(getHibernateTemplate().find(sql).get(0));
            count=Long.parseLong(getHibernateTemplate().find(sql).get(0).toString());
     //     List Count=     getHibernateTemplate().find(sql);
      
     
     
      
      
      
      
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCriteria", "Exception occured",e.getMessage());
            System.out.println("Error"+e.getMessage());
        }
        return count;
   
    }
    
    
    
    
    
    
    
    
    
    
    
    /////New
    public List viewStudentReportByCriteria1(int ciId,int disId,int programId,int studentTypeId,int categoryId,String gender,int disabilityId,String feesType,String academicYear,String year,String reportSubmitted,String ogceCompleted,String vivaVoceCompleted,String extensionFor,String thesisSubmitted,String courseCompleted,String certificateIssued,String loggedInUser,String noReason,String status,int university,String subType,String completeType) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport1 studentReport1=null;
        int approvalTypeId=0;
        try{            
            String sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
            if(year!=null && (programId==2 ||programId==3 || programId==4)){
                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+"  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+"  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+" and submissionType='6')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    //sql=sql+",app1.approvalType,app.approvalStatus ";
                    sql=sql+",'--','--'";
                }
                else{
                    sql=sql+",'--','--'";
                }
            }
            else{
                sql=sql+",'--','--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3 ";
                if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(!courseCompleted.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes' ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }                   
            if(!certificateIssued.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes'  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            sql=sql+",s.birthDate,s.cEmail,s.mobileNo,s.nationality,s.pEmail from cimaster  c,disciplinemaster d,programmaster p,Studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ,User u  ";            
          if(university>0){
                sql=sql+",StudentQualification sq ";
            }
            if(!feesType.equals("Select")){
                sql=sql+ ",PaymentDetails pd ";
            }
            if(!extensionFor.equals("0")){
               // sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
                sql=sql+",Extension ex ";
            }
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and u.userId=s.userId  ";
            if(university>0){
                sql=sql+"and s.personalId=sq.personalId  ";
            }
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!feesType.equals("Select")){
                if(feesType.equals("Enrollment")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
                }
                if(feesType.equals("Thesis")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
                }
            }
            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            if(programId==5 || programId==6 || programId==10 || programId==11){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
            }
            if(programId==2 || programId==3 || programId==4){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                }
            }
            if(programId==5 || programId==6){
                if(!ogceCompleted.equals("Select")){
                    if(ogceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(ogceCompleted.equals("Yes")){
                        sql=sql + "and  s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" ";
                      //  sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
                    }
                    if(ogceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+" ";
                    }
                }
                if(!vivaVoceCompleted.equals("Select")){
                    if(vivaVoceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(vivaVoceCompleted.equals("Yes")){
                        sql=sql + "and s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" ";                    }
                    if(vivaVoceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    if(extensionFor.equals("1"))approvalTypeId=2;
                    else if(extensionFor.equals("2"))approvalTypeId=3;
                    else if(extensionFor.equals("3"))approvalTypeId=4;
                    sql=sql+" and s.personalId=ex.personalId and s.academicYear='"+academicYear+"' and ex.year='"+extensionFor+"' ";
                    sql=sql+"  and s.programId="+programId +" ";
                  //  sql=sql+" and s.personalId=ex.personalId and s.personalId=app.personalId and s.academicYear='"+academicYear+"' ";
                    //sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                    //sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                if(!thesisSubmitted.equals("Select")){
                    if(thesisSubmitted.equals("All")){
                        sql=sql + " and ((s.personalId in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3)and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    else if(thesisSubmitted.equals("Yes")){
                        sql=sql + " and s.personalId in (select distinct sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" ";
                    }
                    else if(thesisSubmitted.equals("No")){
                        sql=sql + " and s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
             if(!courseCompleted.equals("Select")){
//                if(courseCompleted.equals("All")){
//                    sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"')) or ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+")) ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                 if(courseCompleted.equals("Yes")){
                    
                    if(completeType.equals("Select"))
                    {
                        sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                      if(academicYear!="")
                      {
                            sql=sql+" and s.academicYear='"+academicYear+"'";
                      }
                    if(programId>0)
                    {
                        sql=sql+" and s.programId="+programId+" ";
                     }
                    }
                 else if(completeType.equals("Certificate Issued")){
                     
                   sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
                  if(academicYear!=""){
                      
                      sql=sql+"and s.academicYear='"+academicYear+"'";
                  }
                  if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                   }
               }
                    
                      else if(completeType.equals("PDC Issued")){
                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and p.comDate IS NULL and su.upId=17)";
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                    
                }
//                  else if(courseCompleted.equals("Certificate Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//               else if(courseCompleted.equals("PDC Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17)";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                else if(courseCompleted.equals("No")){
                  
                    //sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
                   
                     sql=sql + " and not ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign') )  or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='TransFerred') ))";
                   
                    
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    } 
                    
                    
                    
                    
                    
                    
//                    
//                    if(noReason.equals("Select")){
//                    sql=sql + " and  ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                
//                  else if(noReason.equals("Terminated")){
//                       sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Terminated') ) ";
//                     if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//              else if(noReason.equals("Resign")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Resign')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//                  else if(noReason.equals("TransFerred")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='TransFerred')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
                }
                 
                 
                    else if(courseCompleted.equals("Terminated/Resign")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
          else if(courseCompleted.equals("Transferred")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Transferred')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
                 
            }
             if(!certificateIssued.equals("Select")){
                if(certificateIssued.equals("All")){
                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where s.academicYear='"+academicYear+"' or comDate is null) or ";
                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear="+academicYear+" and)) ";
                    if(programId>0){
                        sql=sql+"and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("Yes")){
                    sql=sql + " and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.printChk='Yes') ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("No")){
                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear="+academicYear+"";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(university>0){
                
                sql=sql+" and sq.university="+university+" and year=(select max(year) from StudentQualification where personalId=s.personalId) ";
            }
            if(!subType.equals("")){
                        sql=sql+" and s.subType='"+subType+"' ";
                    }
             
            if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"'  order by s.enrolmentNo";
            }
            else
            {
               sql=sql+" order by s.enrolmentNo";
            }
            System.out.println("SQL is :"+sql);
           studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){           
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentReport1=new StudentReport1(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString(),row[24].toString(),"--",row[25].toString(),row[26].toString(),row[27].toString(),row[28].toString(),row[29].toString());
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    /*if(!extensionFor.equals("0")){
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString());
                    }
                    else{
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),"--","--");
                    }*/
                    studentDetailsList.add(studentReport1);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCriteria", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }
    
    
    
    
    
    
    
    
     public List viewStudentReportByCriteria1(int ciId,int disId,int programId,int studentTypeId,int categoryId,String gender,int disabilityId,String feesType,String academicYear,String year,String reportSubmitted,String ogceCompleted,String vivaVoceCompleted,String extensionFor,String thesisSubmitted,String courseCompleted,String certificateIssued,String loggedInUser,String noReason,String status,int university,long offset,long maxResult,String subType,String completeType) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        int approvalTypeId=0;
        try{            
            String sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
            if(year!=null && (programId==2 ||programId==3 || programId==4)){
                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+"  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+"  ";
                if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
               sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
               sql=sql+"and s.programId="+programId+" and submissionType='6')>0 then 'Yes' else 'No' End";
           }
            else{
                sql=sql+",'--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                   // sql=sql+",app1.approvalType,app.approvalStatus ";
                     sql=sql+",'--','--'";
                }
                else{
                    sql=sql+",'--','--'";
                }
            }
            else{
                sql=sql+",'--','--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3  ";
               if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(!courseCompleted.equals("Select")){
               
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes'  ";
                if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }                   
            if(!certificateIssued.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes'  ";
              if(academicYear!=""){
                  sql=sql+"  and s.academicYear='"+academicYear+"' ";
                }
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ,User u  ";            
            if(!feesType.equals("Select")){
                sql=sql+ ",PaymentDetails pd ";
            }
            if(!extensionFor.equals("0")){
                //sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
                sql=sql+",Extension ex ";
            }
            if(university>0){
                sql=sql+",StudentQualification sq ";
            }
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and u.userId=s.userId   ";
            if(university>0){
                sql=sql+"and s.personalId=sq.personalId  ";
            }
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!feesType.equals("Select")){
                if(feesType.equals("Enrollment")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
                }
                if(feesType.equals("Thesis")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
                }
            }
            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            if(programId==5 || programId==6 || programId==10 || programId==11){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
            }
            if(programId==2 || programId==3 || programId==4){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                }
            }
            if(programId==5 || programId==6){
                if(!ogceCompleted.equals("Select")){
                    if(ogceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(ogceCompleted.equals("Yes")){
                        sql=sql + "and  s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" ";
                      //  sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
                    }
                    if(ogceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+" ";
                    }
                }
                if(!vivaVoceCompleted.equals("Select")){
                    if(vivaVoceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(vivaVoceCompleted.equals("Yes")){
                        sql=sql + "and s.personalId in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" ";
                    }
                    if(vivaVoceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct sub.personalId from Submission sub where s.academicYear='"+academicYear+"' and sub.submissionType='6') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    if(extensionFor.equals("1"))approvalTypeId=2;
                    else if(extensionFor.equals("2"))approvalTypeId=3;
                    else if(extensionFor.equals("3"))approvalTypeId=4;
                    sql=sql+" and s.personalId=ex.personalId  and s.academicYear='"+academicYear+"' and ex.year='"+extensionFor+"' ";
                sql=sql+"  and s.programId="+programId +" ";
                    //  sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                  //  sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                if(!thesisSubmitted.equals("Select")){
                    if(thesisSubmitted.equals("All")){
                        sql=sql + " and ((s.personalId in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3)and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    else if(thesisSubmitted.equals("Yes")){
                        sql=sql + " and s.personalId in (select distinct sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" ";
                    }
                    else if(thesisSubmitted.equals("No")){
                        sql=sql + " and s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(!courseCompleted.equals("Select")){
//                if(courseCompleted.equals("All")){
//                    sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"')) or ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+")) ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                 if(courseCompleted.equals("Yes")){
                    
                    if(completeType.equals("Select"))
                    {
                        sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                      if(academicYear!="")
                      {
                            sql=sql+" and s.academicYear='"+academicYear+"'";
                      }
                    if(programId>0)
                    {
                        sql=sql+" and s.programId="+programId+" ";
                     }
                    }
                 else if(completeType.equals("Certificate Issued")){
                     
                   sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
                  if(academicYear!=""){
                      
                      sql=sql+"and s.academicYear='"+academicYear+"'";
                  }
                  if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                   }
               }
                    
                      else if(completeType.equals("PDC Issued")){
                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and p.comDate IS NULL and su.upId=17)";
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                    
                }
//                  else if(courseCompleted.equals("Certificate Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//               else if(courseCompleted.equals("PDC Issued")){
//                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17)";
//                    if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
                else if(courseCompleted.equals("No")){
                  
                    //sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
                
                     
//                    sql=sql+" and (s.personalId not in (select personalId from PhdDetails) or s.personalId in (select  personalId FROM PhdDetailsmaster  where acaProgram IS NULL  ) ) ";
                    
                     sql=sql + " and not ((s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster  p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign') )  or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='TransFerred') ))";
                   
                    
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    } 
               
                    
                    
                    
                    
                    
                    
//                    
//                    if(noReason.equals("Select")){
//                    sql=sql + " and  ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                
//                  else if(noReason.equals("Terminated")){
//                       sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Terminated') ) ";
//                     if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//              else if(noReason.equals("Resign")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Resign')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
//                  else if(noReason.equals("TransFerred")){
//                         sql=sql + " and  ";
//                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='TransFerred')  ) ";
//                      if(academicYear!=""){
//                   sql=sql+"and s.academicYear='"+academicYear+"'";
//                    }
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                      
//                  }
                }
                 
                 
                    else if(courseCompleted.equals("Terminated/Resign")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Terminated/Resign')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
                     else if(courseCompleted.equals("Transferred")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster  where acaProgram='Transferred')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                 
                 
          
                 
                 
                 
            }
            if(!certificateIssued.equals("Select")){
                if(certificateIssued.equals("All")){
                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where s.academicYear='"+academicYear+"' or comDate is null) or ";
                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear="+academicYear+" and)) ";
                    if(programId>0){
                        sql=sql+"and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("Yes")){
                    sql=sql + " and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.printChk='Yes') ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("No")){
                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear="+academicYear+"";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(university>0){
                
                sql=sql+" and sq.university="+university+" and year=(select max(year) from StudentQualification where personalId=s.personalId) ";
            }
             if(!subType.equals("")){
                        sql=sql+" and s.subType='"+subType+"' ";
                    }
             
            if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"'  order by s.enrolmentNo";
            }
            else
            {
               sql=sql+" order by s.enrolmentNo";
            }
            System.out.println("SQL is :"+sql);
            Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
            if(studentList.size()>0){           
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString(),row[24].toString(),"--");
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    /*if(!extensionFor.equals("0")){
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString());
                    }
                    else{
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),"--","--");
                    }*/
                    studentDetailsList.add(studentReport);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCriteria", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }
    
   public String getStudentReportQuery(int ciId,int disId,int programId,int studentTypeId,int categoryId,String gender,int disabilityId,String feesType,String academicYear,String year,String reportSubmitted,String ogceCompleted,String vivaVoceCompleted,String extensionFor,String thesisSubmitted,String courseCompleted,String certificateIssued,String loggedInUser,String noReason,String status,int university,String subType) {
        
        int approvalTypeId=0;
        String sql="";
        try{            
             sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
            if(year!=null && (programId==2 ||programId==3 || programId==4)){
                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==5 || programId==6){
                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+" and examType='vivavoce')>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    sql=sql+",app1.approvalType,app.approvalStatus ";
                }
                else{
                    sql=sql+",'--','--'";
                }
            }
            else{
                sql=sql+",'--','--'";
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3 and Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' ";
                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            if(!courseCompleted.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes' and Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' ";
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }                   
            if(!certificateIssued.equals("Select")){
                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes' and Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' ";
                if(programId>0){
                    sql=sql+"and s.programId="+programId+" ";
                }
                sql=sql + ")>0 then 'Yes' else 'No' End";
            }
            else{
                sql=sql+",'--'";
            }
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ,User u ,StudentQualification sq ";            
            if(!feesType.equals("Select")){
                sql=sql+ ",PaymentDetails pd ";
            }
            if(!extensionFor.equals("0")){
                sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
            }
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and u.userId=s.userId  and s.personalId=sq.personalId ";
            if(ciId>0){
                sql=sql + " and c.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and d.disciplineId="+disId+" ";
            }
            if(programId>0){
                sql=sql + "and p.programId="+programId+" ";
            }
            if(studentTypeId>0){
                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
            }
            if(categoryId>0){
                sql=sql + "and c1.categoryId="+categoryId+" ";
            }
            if(disabilityId>0){
                sql=sql + "and d1.disabilityId="+disabilityId+" ";
            }
            if(!gender.equals("Select")){
                sql=sql + "and s.gender='"+gender+ "' ";
            }
            if(!feesType.equals("Select")){
                if(feesType.equals("Enrollment")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
                }
                if(feesType.equals("Thesis")){
                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
                }
            }
            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            if(programId==5 || programId==6 || programId==10 || programId==11){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" ";
                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select  a.personalId from AnnualReview a where a.year="+year+" and s.academicYear='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
                }
            }
            if(programId==2 || programId==3 || programId==4){
                if(!year.equals("0") && reportSubmitted.equals("All")){
                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+") or ";
                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+")) ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" ";
                }
                else if(!year.equals("0") && reportSubmitted.equals("No")){
                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                }
            }
            if(programId==5 || programId==6){
                if(!ogceCompleted.equals("Select")){
                    if(ogceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(ogceCompleted.equals("Yes")){
                        sql=sql + "and  s.personalId in (select distinct  v.personalId from VivaVoceReport v  where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" ";
                      //  sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
                    }
                    if(ogceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='ogce') and s.programId="+programId+" and s.academicYear="+academicYear+" ";
                    }
                }
                if(!vivaVoceCompleted.equals("Select")){
                    if(vivaVoceCompleted.equals("All")){
                        sql=sql + "and ((s.personalId in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    if(vivaVoceCompleted.equals("Yes")){
                        sql=sql + "and s.personalId in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" ";
                    }
                    if(vivaVoceCompleted.equals("No")){
                        sql=sql + "and s.personalId not in (select distinct v.personalId from VivaVoceReport v where s.academicYear='"+academicYear+"' and v.examType='vivavoce') and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
                if(!extensionFor.equals("0")){
                    if(extensionFor.equals("1"))approvalTypeId=2;
                    else if(extensionFor.equals("2"))approvalTypeId=3;
                    else if(extensionFor.equals("3"))approvalTypeId=4;
                    sql=sql+" and s.personalId=ex.personalId and s.personalId=app.personalId and s.academicYear='"+academicYear+"' ";
                    sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
                    sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
                }
            }
            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
                if(!thesisSubmitted.equals("Select")){
                    if(thesisSubmitted.equals("All")){
                        sql=sql + " and ((s.personalId in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+") or ";
                        sql=sql+"(s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3)and s.programId="+programId+" and s.academicYear="+academicYear+")) ";
                    }
                    else if(thesisSubmitted.equals("Yes")){
                        sql=sql + " and s.personalId in (select distinct sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" ";
                    }
                    else if(thesisSubmitted.equals("No")){
                        sql=sql + " and s.personalId not in (select sb.personalId from Submission sb where s.academicYear='"+academicYear+"' and sb.submissionType=3) and s.programId="+programId+" and s.academicYear="+academicYear+"";
                    }
                }
            }
            if(!courseCompleted.equals("Select")){
                if(courseCompleted.equals("All")){
                    sql=sql + "and ((s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"')) or ";
                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear="+academicYear+")) ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(courseCompleted.equals("Yes")){
                    sql=sql + "and s.personalId in (select p.personalId from PhdDetailsmaster  p where  p.acaProgram='Yes')";
                    if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(courseCompleted.equals("No")){
                  
                    //sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
                  if(noReason.equals("Select")){
                    sql=sql + " and  ";
                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                
                  else if(noReason.equals("Terminated")){
                       sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Terminated') ) ";
                     if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                  else if(noReason.equals("Resign")){
                         sql=sql + " and  ";
                    sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster   where noReason='Resign')  ) ";
                      if(academicYear!=""){
                   sql=sql+"and s.academicYear='"+academicYear+"'";
                    }
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                      
                  }
                }
            }
            if(!certificateIssued.equals("Select")){
                if(certificateIssued.equals("All")){
                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where s.academicYear='"+academicYear+"' or comDate is null) or ";
                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear="+academicYear+" and)) ";
                    if(programId>0){
                        sql=sql+"and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("Yes")){
                    sql=sql + " and s.personalId in (select p.personalId from PhdDetailsmaster  p where s.academicYear='"+academicYear+"' and p.printChk='Yes') ";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
                else if(certificateIssued.equals("No")){
                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear="+academicYear+"";
                    if(programId>0){
                        sql=sql+" and s.programId="+programId+" ";
                    }
                }
            }
            if(university>0){
                
                sql=sql+" and sq.university="+university+" and year=(select max(year) from StudentQualification where personalId=s.personalId) ";
            }
            if(!subType.equals("")){
                        sql=sql+" and s.subType='"+subType+"' ";
                    }
             
            if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
            {
            sql=sql+" and u.status='"+status+"'  order by c.ciName,d.disciplineName,p.programName,s.fullName";
            }
            else
            {
               sql=sql+" order by c.ciName,d.disciplineName,p.programName,s.fullName";
            }
            System.out.println("SQL is :"+sql);
          
      
//        String sql="";
//        int approvalTypeId=0;
//        try{  
//            sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
//            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,d1.disabilityId,d1.disability,s.gender,s.academicYear";
//            if(year!=null && (programId==2 ||programId==3 || programId==4)){
//                sql=sql+",Case when (select count(*) from MidTermReview where personalId=s.personalId and semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else if(year!=null && (programId==5 || programId==6 || programId==10 || programId==11)){
//                sql=sql+",Case when (select count(*) from AnnualReview where personalId=s.personalId and year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }
//            if(programId==5 || programId==6){
//                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+" and examType='ogce')>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }
//            if(programId==5 || programId==6){
//                sql=sql+",Case when (select count(*) from VivaVoceReport where personalId=s.personalId and Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+" and examType='vivavoce')>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
//                if(!extensionFor.equals("0")){
//                    sql=sql+",app1.approvalType,app.approvalStatus ";
//                }
//                else{
//                    sql=sql+",'--','--'";
//                }
//            }
//            else{
//                sql=sql+",'--','--'";
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
//                sql=sql+",Case when (select count(*) from Submission where personalId=s.personalId and submissionType=3 and Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                sql=sql+"and s.programId="+programId+")>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }
//            if(!courseCompleted.equals("Select")){
//                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and acaProgram='Yes' and Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                if(programId>0){
//                    sql=sql+"and s.programId="+programId+" ";
//                }
//                sql=sql + ")>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }                   
//            if(!certificateIssued.equals("Select")){
//                sql=sql+",Case when (select count(*) from PhdDetailsmaster  where personalId=s.personalId and printChk='Yes' and Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                if(programId>0){
//                    sql=sql+"and s.programId="+programId+" ";
//                }
//                sql=sql + ")>0 then 'Yes' else 'No' End";
//            }
//            else{
//                sql=sql+",'--'";
//            }
//            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1 ";            
//            if(!feesType.equals("Select")){
//                sql=sql+ ",PaymentDetails pd ";
//            }
//            if(!extensionFor.equals("0")){
//                sql=sql+",Extension ex,ApprovalProcess app,ApprovalType app1 ";
//            }
//            sql=sql+"where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
//            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged ";
//            if(ciId>0){
//                sql=sql + " and c.ciId="+ciId+" ";
//            }
//            if(disId>0){
//                sql=sql + "and d.disciplineId="+disId+" ";
//            }
//            if(programId>0){
//                sql=sql + "and p.programId="+programId+" ";
//            }
//            if(studentTypeId>0){
//                sql=sql + "and s1.studentTypeId="+studentTypeId+" ";
//            }
//            if(categoryId>0){
//                sql=sql + "and c1.categoryId="+categoryId+" ";
//            }
//            if(disabilityId>0){
//                sql=sql + "and d1.disabilityId="+disabilityId+" ";
//            }
//            if(!gender.equals("Select")){
//                sql=sql + "and s.gender='"+gender+ "' ";
//            }
//            if(!feesType.equals("Select")){
//                if(feesType.equals("Enrollment")){
//                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Enrollment Fee' and pd.amount=0 ";
//                }
//                if(feesType.equals("Thesis")){
//                    sql=sql+ "and s.personalId=pd.personalId and pd.payType='Thesis Evaluation Fees' and pd.amount=0 ";
//                }
//            }
//            if(year.equals("0") && reportSubmitted.equals("Select") && ogceCompleted.equals("Select") && vivaVoceCompleted.equals("Select") && extensionFor.equals("0") &&  thesisSubmitted.equals("Select") && courseCompleted.equals("Select") && certificateIssued.equals("Select") && !academicYear.equals("")){
//                sql=sql + "and s.academicYear='"+academicYear+ "' ";
//            }
//            if(programId==5 || programId==6 || programId==10 || programId==11){
//                if(!year.equals("0") && reportSubmitted.equals("All")){
//                    sql=sql + "and ((s.personalId in (select personalId from AnnualReview where year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+") or ";
//                    sql=sql + "(s.personalId not in (select personalId from AnnualReview where year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+" and s.status='Active')) ";
//                }
//                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
//                    sql=sql + "and s.personalId in (select personalId from AnnualReview where year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" ";
//                    //sql=sql+"and s.personalId=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
//                }
//                else if(!year.equals("0") && reportSubmitted.equals("No")){
//                    sql=sql + "and s.personalId not in (select personalId from AnnualReview where year="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+" and s.status='Active' ";
//                    //sql=sql+"and s.personalId!=ar.personalId and ar.year="+year+" and Year(DATE_FORMAT(ar.aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"' and s.programId="+programId+" ";
//                }
//            }
//            if(programId==2 || programId==3 || programId==4){
//                if(!year.equals("0") && reportSubmitted.equals("All")){
//                    sql=sql + "and ((s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+") or ";
//                    sql=sql + "(s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+"  and s.academicYear="+academicYear+" and s.status='Active')) ";
//                }
//                else if(!year.equals("0") && reportSubmitted.equals("Yes")){
//                    sql=sql + "and s.personalId in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" ";
//                }
//                else if(!year.equals("0") && reportSubmitted.equals("No")){
//                    sql=sql + "and s.personalId not in (select personalId from MidTermReview where semister="+year+" and Year(DATE_FORMAT(aprReceiptDate,'%Y-%m-%d'))='"+academicYear+"') and s.programId="+programId+" and s.academicYear="+academicYear+"  and s.status='Active'";
//                }
//            }
//            if(programId==5 || programId==6){
//                if(!ogceCompleted.equals("Select")){
//                    if(ogceCompleted.equals("All")){
//                        sql=sql + "and ((s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+") or ";
//                        sql=sql+"(s.personalId not in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" and s.academicYear<="+academicYear+" and s.status='Active')) ";
//                    }
//                    if(ogceCompleted.equals("Yes")){
//                        sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" ";
//                    }
//                    if(ogceCompleted.equals("No")){
//                        sql=sql + "and s.personalId not in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='ogce') and s.programId="+programId+" and s.academicYear<="+academicYear+" and s.status='Active' ";
//                    }
//                }
//                if(!vivaVoceCompleted.equals("Select")){
//                    if(vivaVoceCompleted.equals("All")){
//                        sql=sql + "and ((s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='vivavoce') and s.programId="+programId+") or ";
//                        sql=sql+"(s.personalId not in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='vivavoce') and s.programId="+programId+" and s.academicYear<="+academicYear+" and s.status='Active')) ";
//                    }
//                    if(vivaVoceCompleted.equals("Yes")){
//                        sql=sql + "and s.personalId in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='vivavoce') and s.programId="+programId+" ";
//                    }
//                    if(vivaVoceCompleted.equals("No")){
//                        sql=sql + "and s.personalId not in (select distinct personalId from VivaVoceReport where Year(DATE_FORMAT(vivaDate,'%Y-%m-%d'))='"+academicYear+"' and examType='vivavoce') and s.programId="+programId+" and s.academicYear<="+academicYear+" and s.status='Active' ";
//                    }
//                }
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6){
//                if(!extensionFor.equals("0")){
//                    if(extensionFor.equals("1"))approvalTypeId=2;
//                    else if(extensionFor.equals("2"))approvalTypeId=3;
//                    else if(extensionFor.equals("3"))approvalTypeId=4;
//                    sql=sql+" and s.personalId=ex.personalId and s.personalId=app.personalId and Year(DATE_FORMAT(ex.fromDate,'%Y-%m-%d'))='"+academicYear+"' ";
//                    sql=sql+" and app.approvalTypeId="+approvalTypeId+" and s.programId="+programId +" ";
//                    sql=sql+" and app1.approvalTypeId=app.approvalTypeId ";
//                }
//            }
//            if(programId==2 ||programId==3 || programId==4 || programId==5 || programId==6 || programId==16){
//                if(!thesisSubmitted.equals("Select")){
//                    if(thesisSubmitted.equals("All")){
//                        sql=sql + " and ((s.personalId in (select personalId from Submission where Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' and submissionType=3) and s.programId="+programId+") or ";
//                        sql=sql+"(s.personalId not in (select personalId from Submission where Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' and submissionType=3)and s.programId="+programId+" and s.academicYear<="+academicYear+") and s.status='Active') ";
//                    }
//                    else if(thesisSubmitted.equals("Yes")){
//                        sql=sql + " and s.personalId in (select personalId from Submission where Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' and submissionType=3) and s.programId="+programId+" ";
//                    }
//                    else if(thesisSubmitted.equals("No")){
//                        sql=sql + " and s.personalId not in (select personalId from Submission where Year(DATE_FORMAT(submissionDate,'%Y-%m-%d'))='"+academicYear+"' and submissionType=3) and s.programId="+programId+" and s.academicYear<="+academicYear+" and s.status='Active' ";
//                    }
//                }
//            }
//            if(!courseCompleted.equals("Select")){
//                if(courseCompleted.equals("All")){
//                    sql=sql + "and (s.personalId in (select personalId from PhdDetailsmaster  where Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' or certiDate is null)) or ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear<="+academicYear+" and s.status='Active') ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                else if(courseCompleted.equals("Yes")){
//                    sql=sql + "and s.personalId in (select personalId from PhdDetailsmaster  where Year(DATE_FORMAT(certiDate,'%Y-%m-%d'))='"+academicYear+"' and acaProgram='Yes')";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                else if(courseCompleted.equals("No")){
//                   // sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where acaProgram='No' and certiDate is null) and s.academicYear<="+academicYear+" and s.status='Active' ";
//                    sql=sql + "and (s.personalId in (select personalId from PhdDetailsmaster  where certiDate is null) or ";
//                    sql=sql+"(s.personalId not in (select personalId from PhdDetails) and s.academicYear<="+academicYear+" and s.status='Active')) ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//            }
//            if(!certificateIssued.equals("Select")){
//                if(certificateIssued.equals("All")){
//                    sql=sql + " and (s.personalId in (select personalId from PhdDetailsmaster  where Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' or comDate is null) or ";
//                    sql=sql+"(s.personalId in (select personalId from PhdDetails) and s.academicYear<="+academicYear+" and s.status='Active')) ";
//                    if(programId>0){
//                        sql=sql+"and s.programId="+programId+" ";
//                    }
//                }
//                else if(certificateIssued.equals("Yes")){
//                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where Year(DATE_FORMAT(comDate,'%Y-%m-%d'))='"+academicYear+"' and printChk='Yes') ";
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//                else if(certificateIssued.equals("No")){
//                    sql=sql + " and s.personalId in (select personalId from PhdDetailsmaster  where printChk='No') and s.academicYear<="+academicYear+" and s.status='Active' ";                    
//                    if(programId>0){
//                        sql=sql+" and s.programId="+programId+" ";
//                    }
//                }
//            }
//            sql=sql+" order by c.ciName,d.disciplineName,p.programName,s.fullName";
//            System.out.println("SQL is :"+sql);
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentReportQuery", "Exception occured",e.getMessage());
        }
        
        return sql;
    }

   

    public List getStudentListByQuery(String sql, String reportType,String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        try{            
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    if(reportType.equals("AllStudentReport")){
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),0,"--",Integer.parseInt(row[13].toString()),row[14].toString(),row[15].toString(),"--",row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString(),row[20].toString(),row[21].toString(),row[22].toString(),row[23].toString(),row[24].toString(),"--");
                    }
                    else if(reportType.equals("FacultyStudentReport") || reportType.equals("GuideStudentReport")){
                        studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString(),"--","--","--","--","--","--","--","--",row[19].toString());
                    }
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCritera", "Exception occured",e.getMessage());
        }
        
        return studentDetailsList;
    }

    public String getStudentsByFacultyQuery(int facultyId, String academicYear, String loggedInUser) {
        String sql="";
        try{
            sql="select s.personalId,coalesce(s.enrolNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,";
            sql=sql+"p.programId,p.programName,s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,";
            sql=sql+"d1.disability,s.gender,s.academicYear from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1, ";
            sql=sql+"FacultyStudent g,Faculty f where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyId=g.facultyId and s.personalId=g.personalId and g.facultyId="+facultyId+" ";
            if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            sql=sql+"order by c.ciName,d.disciplineName,p.programName,s.fullName";
            System.out.println("SQL is :"+sql);
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCritera", "Exception occured",e.getMessage());
        }
        
        return sql;
    }

    public String getStudentsByGuideQuery(int guideId,String studentStatus,int ciId,int disId,String loggedInUser) {
        String sql=" ";
        try{
            sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,d1.disability,s.gender,s.academicYear,s.status";
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1,DoctoralCommittee g,Faculty f ";
            sql=sql+"where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyName=g.cname and s.personalId=g.personalId  ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            
            
             if(ciId>0){
                sql=sql + " and s.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and s.disId="+disId+" ";
            }
            if(guideId>0){
           sql=sql + "  and f.facultyId="+guideId+" ";
                    }
            
            if(studentStatus.equals("Persuing")){
                sql=sql + "and s.status='Active' ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+"and s.status='Inactive'  ";
            }
            sql=sql+"   and   g.composition='Guide/Convener' order by g.cname";
            System.out.println("SQL is :"+sql);
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCritera", "Exception occured",e.getMessage());
        }
        
        return sql;
    }

    public List viewExaminersApprovedByRole(String roleName, String loggedInUser) {         
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudentDetailsByCritera", "Inside viewAllStudentDetailsByCritera","");
        StudentDetailsMapping studentdetailsmapping=null;        
        try{
            String sql="select a.personalId,coalesce(a.enrolmentNo,'--'),a.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,e.programId,e.programName from studentmaster a,SuggestedExaminer b,cimaster  c,disciplinemaster d,programmaster e where a.personalId=b.personalId  ";
            sql=sql+" and c.ciId=a.ciId and d.disciplineId=a.disId and e.programId=a.programId ";
            if(roleName.equals("Admin")){
                sql=sql+" and b.approvedByAdmin='Yes' ";
            }
            if(roleName.equals("AD")){
                sql=sql+" and b.approvedByAD='Yes' ";
            }
            if(roleName.equals("Dean")){
                sql=sql+" and b.approvedByDean='Yes' ";
            }
            if(roleName.equals("Bos")){
                sql=sql+" and b.approvedByBOS='Yes' ";
            }
            sql=sql+" group by b.personalId";
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentReportByCritera", "Exception occured",e.getMessage());
        }
        return studentDetailsList;
    }
    public boolean addPaymentDetails(PaymentDetails paymentDetails, String loggedInUser) {
        boolean flag=false;
        try{
            logData.writeToLogFile(loggedInUser, "addPaymentDetails", "Inside addPaymentDetails","");
      //      synchronized(this){
                getHibernateTemplate().save(paymentDetails);
                flag=true;
                logData.writeToLogFile(loggedInUser, "addPaymentDetails", "Flag :"+flag,"");
        //    }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "addPaymentDetails", "Exception occured",e.getMessage());
            flag=false;
            logData.writeToLogFile(loggedInUser, "addPaymentDetails", "Flag :"+flag,e.getMessage());
        }
        return flag;
    }
    
    public List getPaymentDetailsById(StudentDetails personalId, String loggedInUser) {
        //PaymentDetails paymentDetails=null;
        List paymentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Inside getPaymentDetailsById","");
        try{
            paymentList=getHibernateTemplate().find("from PaymentDetails where personalId=?",personalId);
            /*if(paymentList.size()>0){                
                paymentDetails=(PaymentDetails)paymentList.get(0);
            }*/
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Exception occureud",e.getMessage());
        }   
        return paymentList;
    }
    
    public boolean updatePaymentDetails(PaymentDetails paymentDetails, String loggedInUser) {
        boolean flag=false;
        try{
            logData.writeToLogFile(loggedInUser, "updatePaymentDetails", "Inside updatePaymentDetails","");
      //      synchronized(this){ 
                getHibernateTemplate().update(paymentDetails);
                flag=true;
                logData.writeToLogFile(loggedInUser, "updatePaymentDetails", "Flag:"+flag,"");
       //     }
        }
        catch(Exception e){            
            logData.writeToLogFile(loggedInUser, "updatePaymentDetails", "Exception occured",e.getMessage());
            flag=false;
            logData.writeToLogFile(loggedInUser, "updatePaymentDetails", "Flag:"+flag,e.getMessage());
        }    
        return flag;
    }

    public boolean isDuplicateApplicationNo(String fileNo,String loggedInUser) {
        boolean isDuplicate =true;
        logData.writeToLogFile(loggedInUser, "isDuplicateApplicationNo", "Inside isDuplicateApplicationNo","");
        try{           
                
                List spList=getHibernateTemplate().find("from studentmaster where fileNo=?",fileNo);
                if(spList.size()==0){
                    isDuplicate=false;
                }
            
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "isDuplicateCiName", "Exception occured","Exception");
        }
        return isDuplicate;        
    }
    
    public List viewStudentByStudentUnderFaculty(Faculty facultyId, int personalId, String loggedInUser) {
       /* List studentList=new ArrayList();
        try{            
            String sql="select s.fullName,s.enrolmentNo,p.programId,p.programName,c.ciId,c.ciName,d.disciplineId,d.disciplineName ";
            sql=sql+"from StudentDetails s,FacultyStudent f,cimaster  c,disciplinemaster d,programmaster p ";
            sql=sql+"where s.personalId=f.personalId and c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and f.facultyId="+facultyId.getFacultyId()+" ";            
            sql=sql+"order by s.fullName";        
            studentList=getHibernateTemplate().find(sql);                                    
            
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentByStudentUnderFaculty", "Exception occured",e.getMessage());
        }        
        return studentList;*/
        
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewStudentByStudentUnderFaculty", "Inside getStudentDetailsByFaculty","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName from ";
            sql=sql+"FacultyStudent a,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,Faculty f where f.facultyId=a.facultyId.facultyId ";
            sql=sql+"and s.personalId=a.personalId.personalId and c.ciId=s.ciId.ciId and d.disciplineId=s.disId.disciplineId and p.programId=s.programId ";
            sql=sql+" and a.facultyId="+facultyId.getFacultyId()+" order by s.fullName";
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentByStudentUnderFaculty", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;
    }
    
    public FacultyStudent getFacultyIdByPersonalId(StudentDetails studentdetails, String loggedInUser) {
        FacultyStudent facultyDetails=new FacultyStudent();
        logData.writeToLogFile(loggedInUser, "getFacultyIdByPersonalId", "Inside getFacultyIdByPersonalId","");
        try{
            List facultyList=getHibernateTemplate().find("from FacultyStudent where personalId=?",studentdetails);
            if(facultyList.size()>0){                
                facultyDetails=(FacultyStudent)facultyList.get(0);
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getFacultyIdByPersonalId", "Exception occureud",e.getMessage());
        }   
        return facultyDetails;
    }
    
     public List viewStudentDetailsByGuide(int guideId, String studentStatus, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        try{
            String sql="select s.personalId,coalesce(s.enrolNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,d1.disability,s.gender,s.academicYear,s.status";
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1,GuideStudent g,Faculty f ";
            sql=sql+"where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyName=g.guideName and s.personalId=g.personalId and f.facultyId="+guideId+" ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            
            if(studentStatus.equals("Persuing")){
                sql=sql + "and s.status='Active' ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+"and s.status='Inactive'  ";
            }
            sql=sql+"order by c.ciName,d.disciplineName,p.programName,s.fullName";
            System.out.println("SQL is :"+sql);
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString(),"--","--","--","--","--","--","--","--",row[19].toString());
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByGuide", "Exception occured",e.getMessage());
        }
        
        return studentDetailsList;
    }     
     public List viewStudentDetailsByGuide(int guideId, String studentStatus,int ciId,int disId, String loggedInUser,long offset,long maxResult) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        try{
            String sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,d1.disability,s.gender,s.academicYear,s.status";
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1,DoctoralCommittee g,Faculty f ";
            
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyName=g.cname and s.personalId=g.personalId  ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            
            
             if(ciId>0){
                sql=sql + " and s.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and s.disId="+disId+" ";
            }
            if(guideId>0){
           sql=sql + "  and f.facultyId="+guideId+" ";
                    }

            if(studentStatus.equals("Persuing")){
                sql=sql + " and s.personalId not in (select personalId from PhdDetailsmaster where acaProgram='Yes')  ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+" and s.personalId in (select p.personalId from PhdDetailsmaster p where  p.acaProgram='Yes') ";
            }
            sql=sql+"   and   g.composition='Guide/Convener' order by g.cname";
            System.out.println("SQL is :"+sql);
              Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
         //   studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString(),"--","--","--","--","--","--","--","--",row[19].toString());
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByGuide", "Exception occured",e.getMessage());
        }
        
        return studentDetailsList;
    }
     
     
     
     
     
          public List viewStudentDetailsByGuide(int guideId, String studentStatus,int ciId,int disId, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;
        try{
        String sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,d1.disability,s.gender,s.academicYear,s.status";
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1,DoctoralCommittee g,Faculty f ";
            
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyName=g.cname and s.personalId=g.personalId  ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            
            
             if(ciId>0){
                sql=sql + " and s.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and s.disId="+disId+" ";
            }
            if(guideId>0){
           sql=sql + "  and f.facultyId="+guideId+" ";
                    }
            
            if(studentStatus.equals("Persuing")){
                sql=sql + "and s.status='Active' ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+"and s.status='Inactive'  ";
            }
            sql=sql+"   and   g.composition='Guide/Convener' order by g.cname";
            System.out.println("SQL is :"+sql);
             
           studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString(),"--","--","--","--","--","--","--","--",row[19].toString());
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByGuide", "Exception occured",e.getMessage());
        }
        
        return studentDetailsList;
    }
     
     
     
     
     
     
       public long viewStudentDetailsByGuideCount(int guideId, String studentStatus,int ciId,int disId) {
        int count=0;
        StudentReport studentReport=null;
        try{
          String sql="select distinct  s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,p.programId,p.programName,";
            sql=sql+"s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,d1.disability,s.gender,s.academicYear,s.status";
            sql=sql+" from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1,DoctoralCommittee g,Faculty f ";
            
            sql=sql+" where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyName=g.cname and s.personalId=g.personalId  ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            
            
             if(ciId>0){
                sql=sql + " and s.ciId="+ciId+" ";
            }
            if(disId>0){
                sql=sql + "and s.disId="+disId+" ";
            }
            if(guideId>0){
           sql=sql + "  and f.facultyId="+guideId+" ";
                    }
            
            if(studentStatus.equals("Persuing")){
                sql=sql + " and s.personalId not in (select personalId from PhdDetailsmaster where acaProgram='Yes')  ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+" and s.personalId in (select p.personalId from PhdDetailsmaster p where  p.acaProgram='Yes') ";
            }
            sql=sql+"   and   g.composition='Guide/Convener' order by g.cname";
            System.out.println("SQL is :"+sql);
            List studentList=getHibernateTemplate().find(sql);
            count= studentList.size();
        }catch(Exception e){
            System.out.println("Exception"+e);
        }
        
        return count;
    }
     
     
     
     
     
     
     
     
     
     
     

     public List viewStudentDetailsByFaculty(int facultyId, String studentStatus, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        StudentReport studentReport=null;        
        try{
            String sql="select s.personalId,coalesce(s.enrolNo,'--'),s.fullName ,c.ciId,c.ciName,d.disciplineId,d.disciplineName,";
            sql=sql+"p.programId,p.programName,s1.studentTypeId,s1.studentType,c1.categoryId,c1.categoryName,f.facultyId,f.facultyName,d1.disabilityId,";
            sql=sql+"d1.disability,s.gender,s.academicYear,s.status from cimaster  c,disciplinemaster d,programmaster p,studentmaster s,StudentType s1,Category c1,PhysicalDisability d1, ";
            sql=sql+"FacultyStudent g,Faculty f where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  s1.studentTypeId=s.studentTypeId and ";
            sql=sql+"c1.categoryId=s.categoryId and d1.disability=s.physicalChallanged and f.facultyId=g.facultyId and s.personalId=g.personalId and g.facultyId="+facultyId+" ";
            /*if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }*/
            if(studentStatus.equals("Persuing")){
                sql=sql + "and s.status='Active' ";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+"and s.status='Inactive'  ";
            }
            sql=sql+"order by c.ciName,d.disciplineName,p.programName,s.fullName";
            System.out.println("SQL is :"+sql);
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    //studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),row[18].toString(),row[19].toString());
                    studentReport=new StudentReport(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),Integer.parseInt(row[5].toString()),row[6].toString(),Integer.parseInt(row[7].toString()),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),Integer.parseInt(row[11].toString()),row[12].toString(),Integer.parseInt(row[13].toString()),row[14].toString(),Integer.parseInt(row[15].toString()),row[16].toString(),row[17].toString(),"--",row[18].toString(),"--","--","--","--","--","--","--","--","--");
                    studentDetailsList.add(studentReport);
                }
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByFaculty", "Exception occured",e.getMessage());
            System.out.println(e.getMessage());
        }        
        return studentDetailsList;
    }

    public List viewStudentDetailsByFacultyMapping(int facultyId, String academicYear, String loggedInUser) {        
        List studentList=new ArrayList();   
        try{
            String sql="select s.fullName as fullName,s.personalId as personalId from studentmaster s,Faculty f,FacultyStudent g where f.facultyId=g.facultyId and s.personalId=g.personalId and g.facultyId="+facultyId+" ";
            if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            sql=sql+"order by s.fullName";        
            studentList=getHibernateTemplate().find(sql);                        
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByFacultyMapping", "Exception occured",e.getMessage());
            System.out.println(e.getMessage());
        }        
        return studentList;
    }

    public List viewStudentDetailsByGuideMapping(int guideId, String academicYear, String loggedInUser) {        
        List studentList=new ArrayList();
        try{            
            String sql="select s.fullName from studentmaster s,Faculty f,GuideStudent g where f.facultyId=g.facultyId and s.personalId=g.personalId and g.facultyId="+guideId+" ";
            if(!academicYear.equals("")){
                sql=sql + "and s.academicYear='"+academicYear+ "' ";
            }
            sql=sql+"order by s.fullName";        
            studentList=getHibernateTemplate().find(sql);                                    
            
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByGuideMapping", "Exception occured",e.getMessage());
        }        
        return studentList;
    }
        
    public List getStudentsByCiAndDiscipline(CI ciId, Discipline disciplineId,Program program,String academicYear, String loggedInUser) {
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentsByCiAndDiscipline", "Inside getStudentsByCiAndDiscipline","");
        try{
            String sql="from studentmaster where ciId="+ciId.getCiId()+" and disId="+disciplineId.getDisciplineId()+" ";
            if(program!=null){
                sql=sql+" and programId="+ program.getProgramId()+" ";
            }
            if(!academicYear.equals(""))
            {
                sql=sql+" and academicYear="+academicYear+" ";
            }
            sql=sql+"and status='Active'";  
            studentList=getHibernateTemplate().find(sql);
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentsByCiAndDiscipline", "Exception occured",e.getMessage());
        } 
        return studentList;
    }

    public int getStudentByEnrollment(String enrolNo, String loggedInUser) {
        List studentEnrolList=new ArrayList();
        Object studentdetails=null;
        int maxSeq=0;        
        try{                        
            studentEnrolList=getHibernateTemplate().find("select max(s.enrolSequence) from studentmaster s where enrolNo=?",enrolNo);                                    
            if(studentEnrolList.size()>0){
                studentdetails=studentEnrolList.get(0);
                maxSeq=Integer.parseInt(studentdetails.toString());
            }
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewStudentDetailsByGuide", "Exception occured",e.getMessage());
        }        
        return maxSeq;
    }
    
    public PaymentDetails getPaymentDetailsByIdAndType(StudentDetails personalId, String loggedInUser) {
        PaymentDetails paymentDetails=new PaymentDetails();
        List paymentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Inside getPaymentDetailsById","");
        try{
            paymentList=getHibernateTemplate().find("from PaymentDetails where payType='Enrollment Fee' and personalId=?",personalId);
            if(paymentList.size()>0){                
                paymentDetails=(PaymentDetails)paymentList.get(0);
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Exception occureud",e.getMessage());
        }   
        return paymentDetails;
    }
    
    
    
    
    public List viewAllStudents(String loggedInUser,int userId,long offset,long maxResult) {
       
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudents", "Inside viewAllStudents","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            int personal_Id=0;
            String enrolment_No="";
            String full_Name="";
            int ci_Id=0;
            String ci_Name="";
            String ciName_Code="";
            int dis_Id=0;
            String dis_Name="";
            String disName_Code="";
            int program_Id=0;
            String program_Name="";
            StudentDetails student=null;
            String sql="from studentmaster where ciId IN (select ciId from ADCIMapping where userId="+userId+") order by enrolmentNo";
            System.out.println("SQL is:"+sql);
            
            
             Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
            
            
            
           // studentList=getHibernateTemplate().find(sql);
         for(int i=0;i<studentList.size();i++){
                    student=(StudentDetails)studentList.get(i);
                    personal_Id=student.getPersonalId();
                    enrolment_No=student.getEnrolmentNo();
                    full_Name=student.getFullName();
                    if(student.getCiId()==null) {
                        ci_Id=0;
                        ci_Name="--";
                        ciName_Code="--";
                    }
                    else{
                        ci_Id=student.getCiId().getCiId();
                        ci_Name=student.getCiId().getCiName();
                        ciName_Code=student.getCiId().getCiNameCode();
                    }
                    if(student.getDisId()==null){
                        dis_Id=0;
                        dis_Name="--";
                        disName_Code="--";
                    }
                    else{
                        dis_Id=student.getDisId().getDisciplineId();
                        dis_Name=student.getDisId().getDisciplineName();
                        disName_Code=student.getDisId().getDisciplineCode();
                    }
                    if(student.getProgramId()==null){
                        program_Id=0;
                        program_Name="--";
                    }
                    else{
                        program_Id=student.getProgramId().getProgramId();
                        program_Name=student.getProgramId().getProgramName();
                    }
                    studentdetailsmapping = new StudentDetailsMapping(personal_Id,enrolment_No,full_Name,ci_Id,ci_Name,ciName_Code,dis_Id,dis_Name,disName_Code,program_Id,program_Name,"");
                    studentDetailsList.add(studentdetailsmapping);
                }
        //}
             studentdetailsmapping = new StudentDetailsMapping(personal_Id,enrolment_No,full_Name,ci_Id,ci_Name,ciName_Code,dis_Id,dis_Name,disName_Code,program_Id,program_Name,"");
                    studentList.add(studentdetailsmapping);
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudents", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

    }
   

    public List viewAllStudents(String status,String loggedInUser,long offset,long maxResult) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "viewAllStudents", "Inside viewAllStudents","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            int personal_Id=0;
            String enrolment_No="";
            String full_Name="";
            int ci_Id=0;
            String ci_Name="";
            String ciName_Code="";
            int dis_Id=0;
            String dis_Name="";
            String disName_Code="";
            int program_Id=0;
            String program_Name="";
            StudentDetails student=null;
            String sql="";
             if(status.equalsIgnoreCase("Yes")){
                 sql=sql+"from studentmaster s where ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                         sql=sql+" Order by enrolmentNo";
             }
             
             else if(status.equalsIgnoreCase("No")){
                  sql=sql + "  from studentmaster s where  not ((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmasterp,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmasterwhere acaProgram='TransFerred') ))";
                         sql=sql+" order by enrolmentNo";
             }
              else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+"  from studentmaster s where (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign')  ) ";
                 sql=sql+" order by enrolmentNo";
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
                 sql=sql+" from studentmaster s where (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Transferred')  ) ";
               sql=sql+" order by enrolmentNo";  
             }
              else           
             {
               sql=sql+" from studentmaster order by enrolmentNo";
          }
            
//                    if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
//            {
//            sql=sql+"from studentmaster where  userId.status='"+status+"' order by enrolmentNo";
//            }
//            else
//            {
//               sql=sql+" from studentmaster order by enrolmentNo";
//            }
            Query query=getSession().createQuery(sql);
                query.setFirstResult((int) (offset!=0?offset:0));
                query.setMaxResults((int) (maxResult!=0?maxResult:25));
                studentList=query.list();
         for(int i=0;i<studentList.size();i++){
                    student=(StudentDetails)studentList.get(i);
                    personal_Id=student.getPersonalId();
                    enrolment_No=student.getEnrolmentNo();
                    full_Name=student.getFullName();
                    if(student.getCiId()==null) {
                        ci_Id=0;
                        ci_Name="--";
                        ciName_Code="--";
                    }
                    else{
                        ci_Id=student.getCiId().getCiId();
                        ci_Name=student.getCiId().getCiName();
                        ciName_Code=student.getCiId().getCiNameCode();
                    }
                    if(student.getDisId()==null){
                        dis_Id=0;
                        dis_Name="--";
                        disName_Code="--";
                    }
                    else{
                        dis_Id=student.getDisId().getDisciplineId();
                        dis_Name=student.getDisId().getDisciplineName();
                        disName_Code=student.getDisId().getDisciplineCode();
                    }
                    if(student.getProgramId()==null){
                        program_Id=0;
                        program_Name="--";
                    }
                    else{
                        program_Id=student.getProgramId().getProgramId();
                        program_Name=student.getProgramId().getProgramName();
                    }
                    studentdetailsmapping = new StudentDetailsMapping(personal_Id,enrolment_No,full_Name,ci_Id,ci_Name,ciName_Code,dis_Id,dis_Name,disName_Code,program_Id,program_Name,"");
                    studentDetailsList.add(studentdetailsmapping);
                }
        //}
             studentdetailsmapping = new StudentDetailsMapping(personal_Id,enrolment_No,full_Name,ci_Id,ci_Name,ciName_Code,dis_Id,dis_Name,disName_Code,program_Id,program_Name,"");
                    studentList.add(studentdetailsmapping);
        }catch(Exception e){
            logData.writeToLogFile(loggedInUser, "viewAllStudents", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

    }
   
    @Override
    public List getStudentDetailsByFacultyAndStatus(Faculty faculty, String studentStatus, String loggedInUser) {
        List studentDetailsList=new ArrayList();
        List studentList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getStudentDetailsByFacultyAndStatus", "Inside getStudentDetailsByFaculty","");
        StudentDetailsMapping studentdetailsmapping=null;
        try{
            String sql="select s.personalId,coalesce(s.enrolmentNo,'--'),s.fullName ,c.ciId,c.ciName,c.ciNameCode,d.disciplineId,d.disciplineName,d.disciplineCode,p.programId,p.programName  ";
            if(!studentStatus.equals("Select") && studentStatus.equals("Completed")){
                sql=sql+",pd.acaprogrammaster ";
            }
            sql=sql+"from GuideStudent a,cimaster  c,disciplinemaster d,programmaster p,studentmaster s,Faculty f ";
            if(!studentStatus.equals("Select")&& studentStatus.equals("Completed")){
                sql=sql+ ",PhdDetailsmaster pd ";
            }
            sql=sql+ "where f.facultyName=a.guideName ";
            sql=sql+"and s.personalId=a.personalId.personalId and c.ciId=s.ciId.ciId and d.disciplineId=s.disId.disciplineId and p.programId=s.programId and a.guideName='"+faculty.getFacultyName()+"' ";
            if(studentStatus.equals("Persuing")){
               // sql=sql+"and s.personalId=pd.personalId and (pd.acaProgram='No' or pd.acaProgram='' or pd.acaProgram='Select')";
                sql=sql+"and (s.personalId in(select personalId from PhdDetailsmaster where acaProgram='No' or acaProgram='' or acaProgram='Select')";
                sql=sql+"or s.personalId in(select a1.personalId.personalId from GuideStudent a1 where a1.personalId.personalId not in";
                sql=sql+"(select personalId from PhdDetailsmaster)))";
            }
            else if(studentStatus.equals("Completed")){
                sql=sql+"and s.personalId=pd.personalId and pd.acaProgram='Yes' ";
            }
            sql=sql+"order by s.enrolmentNo";
            System.out.println("Query :"+sql);
            studentList=getHibernateTemplate().find(sql);
            if(studentList.size()>0){
                Iterator is=studentList.iterator();
                while(is.hasNext()){
                    Object[] row =(Object[]) is.next();
                    if(!studentStatus.equals("Select")  && studentStatus.equals("Completed")){
                        studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),row[11].toString());
                    }
                    else{
                        studentdetailsmapping = new StudentDetailsMapping(Integer.parseInt(row[0].toString()),row[1].toString(),row[2].toString(),Integer.parseInt(row[3].toString()),row[4].toString(),row[5].toString(),Integer.parseInt(row[6].toString()),row[7].toString(),row[8].toString(),Integer.parseInt(row[9].toString()),row[10].toString(),"");
                    }
                    studentDetailsList.add(studentdetailsmapping);
                }
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getStudentDetailsByFacultyAndStatus", "Exception occured",e.getMessage());
            System.out.println("Exception :"+e.getMessage());
        }
        return studentDetailsList;

    }

    public int getDisciplineByUser(User userId, String loggedInUser) {
        BosDIMapping bos=null;
        int disciplineId=0;
        List recList=new ArrayList();
        logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Inside getPaymentDetailsById","");
        try{
            recList=getHibernateTemplate().find("from BosDIMapping where userId=?",userId);
            if(recList.size()>0){                
                bos=(BosDIMapping)recList.get(0);
                disciplineId=bos.getDisciplineId().getDisciplineId();
            }
        }
        catch(Exception e){
            logData.writeToLogFile(loggedInUser, "getPaymentDetailsById", "Exception occureud",e.getMessage());
        }
        return disciplineId;
    }

    @Override
    public long getStudentCount(int ciId, int disId, int programId, String enrolNo, String fullName, String academicYear, String fileNumber, String status, String loggedInUser,String subType) {
       long count = 0;
        try{
                String sql="";
                sql="select count(*) from cimaster  c,disciplinemaster d,User u,programmaster p,";
                sql=sql+"studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId  and  u.userId=s.userId ";
                if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                }
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }
                if(!subType.equals("")){
                    sql=sql + "and s.subType='"+subType+"' ";
                }
//                    if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive"))
//            {
//            sql=sql+" and u.status='"+status+"' order by s.fullName";
//            }
//            else
//            {
//               sql=sql+" order by s.fullName";
//            }
                if(status.equalsIgnoreCase("Yes")){
                 sql=sql+" and  ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                 
             }
             
             else if(status.equalsIgnoreCase("No")){
                  sql=sql + " and   not ((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='TransFerred') ))";
             }
              else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+"  and  (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign')  ) ";
                 
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
         sql=sql+" and  (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Transferred')  ) ";
                 
             }
            
                count= (Long)getHibernateTemplate().find(sql).get(0);
        }catch(Exception e){
            System.out.println("Exception :"+e.getMessage());
        }
      return count;
    }
    public long getStudentCount(String loggedInUser,int userId){
            String sql="from studentmaster where ciId IN (select ciId from ADCIMapping where userId="+userId+") order by fullName";
            return (long)getHibernateTemplate().find(sql).size();
    }
   public long getStudentCount(String status,String loggedInUser){
            String sql="";
             if(status.equalsIgnoreCase("Yes")){
                 sql=sql+"select count(*) from studentmaster s where ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                 
             }
             else if(status.equalsIgnoreCase("No")){
                 
                 
                sql=sql + " select count(*) from studentmaster s where  not ((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='TransFerred') ))";
                 
                 
             }
             else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+" select count(*) from studentmaster s where (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign')  ) ";
                 
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
         sql=sql+"select count(*) from studentmaster s where (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Transferred')  ) ";
                 
             }
             else{
                   sql="select count(*) from studentmaster order by enrolmentNo";
             }
//             if(status.equalsIgnoreCase("active")||status.equalsIgnoreCase("inactive")){
//               sql="select count(*) from studentmaster where  userId.status='"+status+"' order by fullName";
//            }
//            else{
//             sql="select count(*) from studentmaster order by enrolmentNo";
//            }
            return (Long)getHibernateTemplate().find(sql).get(0);
    }
   public long getStudentCount(int ciId,int disId,int programId,String enrolNo,String fullName,String academicYear,String fileNumber,String status,int userId,String loggedInUser,String subType){
          long count = 0;
        try{
                String sql="";
                 sql="select  count(*) from cimaster  c,disciplinemaster d,User u,programmaster p,";
                sql=sql+"studentmaster s where c.ciId=s.ciId and d.disciplineId=s.disId and p.programId=s.programId and  u.userId=s.userId ";
                if(ciId>0){
                    sql=sql + " and c.ciId="+ciId+" ";
                }
                if(ciId==0){
                  sql=sql+   "and c.ciId IN (select ciId from ADCIMapping where userId="+userId+")";
                }
                if(disId>0){
                    sql=sql + "and d.disciplineId="+disId+" ";
                }
                if(programId>0){
                    sql=sql + "and p.programId="+programId+" ";
                }
                if(!enrolNo.equals("")){    
                    sql=sql + "and s.enrolmentNo like '%"+enrolNo+"%'" ;
                }
                
                if(!academicYear.equals("")){
                    sql=sql + "and s.academicYear='"+academicYear+ "' ";
                }
                   if(!fileNumber.equals("")){
                    sql=sql + "and s.fileNo='"+fileNumber+ "' ";
                }
                   if(!fullName.equals("")){
                    sql=sql + "and s.fullName like '%"+fullName+"%'";
                }
                  if(!subType.equals("")){
                    sql=sql + "and s.subType  '"+subType+"'";
                }
          if(status.equalsIgnoreCase("Yes")){
                 sql=sql+" and  ";
                 sql=sql + "((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18)) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17))) ";
                 
             }
             
             else if(status.equalsIgnoreCase("No")){
                  sql=sql + "  and   not ((s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su WHERE p.personalId=su.personalId and p.comDate IS NOT NULL and su.upId=18 )) or ";
                        sql=sql+"(s.personalId in (select p.personalId from PhdDetailsmaster p,StudentUpload su  WHERE p.personalId=su.personalId and p.certiDate IS NOT NULL and su.upId=17 )) or ";
                        sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign') ) or ";
                         sql=sql+"(s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='TransFerred') ))";
             }
              else if(status.equalsIgnoreCase("Terminated/Resign")){
                  sql=sql+"  and  (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Terminated/Resign')  ) ";
                 
             }
             else if(status.equalsIgnoreCase(("Transferred"))){
         sql=sql+" and (s.personalId  in (select personalId from PhdDetailsmaster where acaProgram='Transferred')  ) ";
                 
             }
            
                count= (Long)getHibernateTemplate().find(sql).get(0);
        }catch(Exception e){
            System.out.println("Exception :"+e.getMessage());
        }
      return count;
   }
}

