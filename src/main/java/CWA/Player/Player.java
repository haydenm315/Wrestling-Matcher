package CWA.Player;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import CWA.MatrixClassification.CWAAgeWeightClassificationMatrix;
import CWA.MatrixClassification.WeightClassificationMatrix;

public class Player implements Serializable
{
   
   public Player()
   {
      if (this.id == null)
         id = new ObjectId();
   }

   
   public Player(ObjectId id, String firstName, String lastName, int weightInLbs, Date birthDate)
   {
      this.birthDate = birthDate;
      this.firstName = firstName;
      this.lastName = lastName;
      this.weightInLbs = weightInLbs;
      this.ageInYears = getAgeYears();
      if (this.id == null)
      {
         id = new ObjectId();
      }

   }
   
   @Id
   private ObjectId id;
   
   /**
    * 
    */
   private static final long serialVersionUID = -3130578640276029551L;
   
   
   private String lastName;
   
   
   private String firstName;
   
   private Date birthDate;
   
   
   private int weightInLbs;
   
   private short ageInYears;
   
   private String matrixClass;
   
   public String getId()
   {
      if (id == null)
         id = new ObjectId();
      return id.toString();
   }
   
   public void setId(ObjectId id)
   {
      this.id = id;
   }

   @JsonInclude(Include.NON_NULL)
   public String getMatrixClass()
   {
      if (matrixClass == null)
      {
         WeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
         matrix.initializeMatrix();
         setClass(matrix);
      }
      return matrixClass == null ? "N/A" : matrixClass;
   }
   
   /**
    * @return the m_lastName
    */
   public String getLastName()
   {
      return lastName;
   }


   /**
    * @param m_lastName the m_lastName to set
    */
   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }


   /**
    * @return the m_firstName
    */
   public String getFirstName()
   {
      return firstName;
   }


   /**
    * @param m_firstName the m_firstName to set
    */
   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }


   /**
    * @return the m_weightInLbs
    */
   public int getWeightInLbs()
   {
      return weightInLbs;
   }


   /**
    * @param m_weightInLbs the m_weightInLbs to set
    */
   public void setWeightInLbs(int weightInLbs)
   {
      this.weightInLbs = weightInLbs;
   }


   /**
    * @return the m_birthDate
    */
   public Date getBirthDate()
   {
      return birthDate;
   }


   /**
    * @param m_birthDate the m_birthDate to set
    */
   public void setBirthDate(Date birthDate)
   {
      this.birthDate = birthDate;
   }
   
   /**
    * @return
    */
   @JsonInclude(Include.NON_NULL)
   public short getAgeYears()
   {
      int yearsOld = 0;
      int yearToUse;
      LocalDate today = LocalDate.now();
      
      //if month is a after december and after the season ends use the current year.
      if (today.getMonthValue() <= 12 && 
           today.getMonthValue() >= 4)
      {
         yearToUse = today.getYear();
      }
      //month is during the season and after December 31st, so use last year as date reference.
      else
      {
         yearToUse = today.getYear() - 1;
      }
      LocalDate december31st = LocalDate.of(yearToUse, 12, 31);
      
      if (birthDate != null)
      {
         LocalDate birthday = Instant.ofEpochMilli(birthDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
         Period p = Period.between(birthday, december31st);
         yearsOld = p.getYears();
      }
      return (short) yearsOld;
   }
   
   private void setAgeInYears()
   {
      int yearToUse;
      LocalDate today = LocalDate.now();
      //if month is a after december and after the season ends use the current year.
      if (today.getMonthValue() <= 12 && 
           today.getMonthValue() >= 4)
      {
         yearToUse = today.getYear();
      }
      //month is during the season and after December 31st, so use last year as date reference.
      else
      {
         yearToUse = today.getYear() - 1;
      }
      LocalDate december31st = LocalDate.of(yearToUse, 12, 31);
      LocalDate birthday = Instant.ofEpochMilli(birthDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
       
      Period p = Period.between(birthday, december31st);
   }
   
   public void setClass(WeightClassificationMatrix matrix)
   {
      String matrixValue = "N/A";
      //TODO: figure out why NPE is occuring
      try
      {
         if (ageInYears == 0)
            ageInYears = getAgeYears();
         if (ageInYears > 0)
          
         {
            matrixValue = matrix.getMatrixValue(weightInLbs, ageInYears);
         }
         if (matrixValue != null)
            matrixClass = matrixValue;
         //else
          //  matrixClass = "N/A";         
      }
      catch (NullPointerException e)
      {
         e.printStackTrace();
      }

   }

}
