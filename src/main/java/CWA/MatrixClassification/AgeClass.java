package CWA.MatrixClassification;
public class AgeClass implements Comparable<AgeClass>
{
   public AgeClass(int minAge, int maxAge)
   {
      m_minAge = minAge;
      m_maxAge = maxAge;
   }

   int m_minAge;
   int m_maxAge;

   @Override
   public int hashCode()
   {
      return m_minAge;
   }

   @Override
   public boolean equals(Object o)
   {
      AgeClass classIn = (AgeClass) o;
      if (classIn.m_minAge == m_minAge && classIn.m_maxAge == m_maxAge)
         return true;
      else
         return false;
   }

   @Override
   public int compareTo(AgeClass o)
   {
      if (o.m_minAge < m_minAge)
         return 1;
      else if (o.m_minAge == m_minAge)
         return 0;
      else         
         return -1;
   }
   
   public int getMinAge()
   {
      return m_minAge;
   }
   
   public int getMaxAge()
   {
      return m_maxAge;
   }
   
   public String getDescription()
   {
      String description = "";
      if (m_minAge == m_maxAge)
         description = String.valueOf(m_maxAge + " yr");
      else
         description = String.valueOf(m_minAge + " - " + m_maxAge  + " yr");
      
      return description;
   }
}