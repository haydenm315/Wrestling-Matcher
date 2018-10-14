package CWA.MatrixClassification;

public class WeightClassification implements Comparable<WeightClassification>
{
   
   private int m_minimumWeight;
   
   private int m_maximumWeight;
   
   private String m_class;
   
   public WeightClassification(int minWeight, int maxWeight, String matrixClass)
   {
      m_minimumWeight = minWeight;
      m_maximumWeight = maxWeight;
      m_class = matrixClass;

   }
   
   public String getMatrixClass()
   {
      return m_class;
   }
   public void setMinimumWeight(int weight)
   {
      m_minimumWeight = weight;
   }
   
   public void setMaximumWeight(int weight)
   {
      m_maximumWeight = weight;
   }
   
   public void displayAgeWeightClassification()
   {
      System.out.print("Matrix Class: " + m_class + ", ");
      System.out.print("Min weight: " + m_minimumWeight + ", ");
      System.out.print("Max weight: " + m_maximumWeight + "\n");
   }

   @Override
   public int compareTo(WeightClassification arg0)
   {
      if (arg0.m_minimumWeight < m_minimumWeight)
         return 1;
      else
         return -1;         
   }
   

   public boolean weightInClass(int weight)
   {
      boolean ret = false;
      if (weight >= m_minimumWeight && weight <=m_maximumWeight)
      {
         ret = true;
      }
      return ret;
   }
   
   public String getDescription()
   {
      return m_minimumWeight + " - " + m_maximumWeight;
   }


}
