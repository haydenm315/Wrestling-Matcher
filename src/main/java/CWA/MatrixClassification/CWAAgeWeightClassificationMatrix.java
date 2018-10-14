package CWA.MatrixClassification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;


public class CWAAgeWeightClassificationMatrix implements WeightClassificationMatrix
{
   private AgeClass[] ageClasses =  { 
                                      new AgeClass(4,4),
                                      new AgeClass(5,5),
                                      new AgeClass(6,7),
                                      new AgeClass(8,8),
                                      new AgeClass(9,9),
                                      new AgeClass(10,10),
                                      new AgeClass(11,11),
                                      new AgeClass(12,12),
                                      new AgeClass(13,14)};
   
   private static String[] matrixClasses = {"A","B","C","D","0","1","2","3","4","5","6","7","8","9","10",
                                     "11","12","13","14","15","16","17","18","19","20","21","22",
                                     "23","24","25","26"};
      
   private TreeMap<AgeClass, ArrayList<WeightClassification>> m_matrix = new TreeMap<AgeClass, ArrayList<WeightClassification>>();
   
   
   
   private short getClassWeightSpread(int classIndex)
   {
      //most likely case
      short classWeightSpread = 4;
      //big boys have 8lbs between classes
      if (classIndex >=28)
         classWeightSpread = 8;
      //tots (a-d) have 5 lbs 
      else if (classIndex <=3 )
         classWeightSpread = 5;      
      return classWeightSpread;
   }

   @Override
   public void addClassification(WeightClassification classification)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public String getMatrixValue(int weight, short age)
   {
      //System.out.println("Weight: " + weight + " Age: " + age);
      AgeClass foundAgeKey = null;
      WeightClassification foundWeightClass = null;
      String foundMatrixValue = "No class match";
      for (AgeClass ageKey :m_matrix.keySet())
      {
         if (age >= ageKey.getMinAge() && age <= ageKey.getMaxAge() )
         {
            foundAgeKey= ageKey;
            break;
         }
      }
      //System.out.println("Found age key: " + foundAgeKey.getMinAge() + " - " + foundAgeKey.getMaxAge());
      
      ArrayList<WeightClassification> weightsList = m_matrix.get(foundAgeKey);
      if (weightsList == null)
         return foundMatrixValue;
      
      for (WeightClassification weightClass: weightsList)
      {
         if (weightClass.weightInClass(weight))
         {
            foundWeightClass = weightClass;
            break;
         }
      }
      
      if (foundWeightClass != null)
      {
         foundMatrixValue = foundWeightClass.getMatrixClass();
      }
      else
      {
         System.out.println("Could not find matrix value! Age: " + age + " weight " + weight);
         //kid didn't fit in a class.  Probably too light for their age.  Classify them as
         //if they're one year younger.
         //foundMatrixValue = getMatrixValue(weight, (short)(age -1));
         
      }
      
      //if (foundMatrixValue == null)
        // foundMatrixValue = "No class match";
      
      return foundMatrixValue;
   }


   public HashMap<String,ArrayList<String>> getMatrixTable()
   {
      HashMap<String,ArrayList<String>> classWeightMap = new HashMap<>();

      for(AgeClass ageClass: ageClasses)
      {
         /*if (ageClass.m_maxAge <= 5)
         {
            continue;
         }*/
         //all weights in age class
         ArrayList<WeightClassification> weightsInClass = m_matrix.get(ageClass);
         
         for (WeightClassification weightClass: weightsInClass)
         {
            String matrixClass = weightClass.getMatrixClass();
            if (!classWeightMap.containsKey(matrixClass))
               classWeightMap.put(matrixClass, new ArrayList<String>());
            ArrayList<String> weightsInClassList = classWeightMap.get(matrixClass);
            //TODO: have to fix sorting here
            weightsInClassList.add(weightClass.getDescription());
            
         }
      } 
      
      String[] keys = classWeightMap.keySet().toArray(new String[0]);
      Arrays.sort(keys, new Comparator<String>() {
         public int compare(String o1, String o2) {
            return extractInt(o1) - extractInt(o2);
        }

        int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    });
      
      for (String key: keys)
      {
        System.out.println(key);
        String[] weightsInClassAry = classWeightMap.get(key).toArray(new String[0]);
        //TODO:// sort here may be wrong
        //array holds strings like xx -xx and needs to sort from highest to lowest.
        Arrays.sort(weightsInClassAry, new WeightRangeComparator());
        System.out.println(Arrays.toString(weightsInClassAry));
      }
      
      return classWeightMap;
   }
   
   public void initializeMatrix()
   {
      int baseWeight = 35;
      int weightOffset = 0;
      
   
      for (int ageClassIdx = 0; ageClassIdx< ageClasses.length; ageClassIdx++)
      {
         int classSpread = 0;
         
         AgeClass currAgeClass = ageClasses[ageClassIdx];
         ArrayList<WeightClassification> classesForAgeGroup = new ArrayList<WeightClassification>();
         
         //little guys A,B
         if (ageClassIdx == 0)
         {
            weightOffset = -1;
            
            for (int currClassIdx = 0; currClassIdx < 2; currClassIdx++)
            {
               if (currClassIdx > 0)
                  classSpread = 1;
               WeightClassification classification = new WeightClassification(classSpread + baseWeight + weightOffset,
                     classSpread + baseWeight + weightOffset + getClassWeightSpread(ageClassIdx), matrixClasses[currClassIdx]);
               
               classesForAgeGroup.add(classification);
               weightOffset += getClassWeightSpread(ageClassIdx);
            }
            m_matrix.put(currAgeClass, classesForAgeGroup);
            classSpread = 0;

            continue;
         }
         //little guys C,D
         else if (ageClassIdx == 1)
         {
            weightOffset = 4;
            for (int currClassIdx = 2; currClassIdx < 4; currClassIdx++)
            {
               if (currClassIdx > 2)
                  classSpread = 1;
               WeightClassification classification = new WeightClassification(classSpread + baseWeight + weightOffset ,
                     classSpread + baseWeight + weightOffset + getClassWeightSpread(ageClassIdx), matrixClasses[currClassIdx]);
               classesForAgeGroup.add(classification);
               weightOffset += getClassWeightSpread(ageClassIdx);
            }
            m_matrix.put(currAgeClass, classesForAgeGroup);
            classSpread = 0;
            
            continue;
         }
         
         weightOffset = 0;
         //everyone else
         for (int currClassIdx = 4; currClassIdx < matrixClasses.length; currClassIdx++)
         {  
            if (currClassIdx >= 5)
            {
               classSpread += 1;
            }
            String currClass = matrixClasses[currClassIdx];
            WeightClassification classification = new WeightClassification(
                  classSpread + baseWeight + weightOffset, 
                  classSpread + baseWeight + weightOffset + getClassWeightSpread(currClassIdx), currClass);
            
            classesForAgeGroup.add(classification);

            weightOffset += getClassWeightSpread(currClassIdx);
         }
         baseWeight-=4;
         weightOffset = 0;
         m_matrix.put(currAgeClass, classesForAgeGroup);
      }
   }
   
   public void displayMatrixData()
   {
      for (AgeClass currAgeClass : m_matrix.keySet())
      {
         System.out.println("Current age Class min age: " + currAgeClass.m_minAge + " Current age class max age: " + currAgeClass.m_maxAge);
         ArrayList<WeightClassification> ageWeightClassifications = m_matrix.get(currAgeClass);

         Iterator<WeightClassification> ageWeightIter =  ageWeightClassifications.iterator();
       
         while (ageWeightIter.hasNext())
         {
            WeightClassification classification = ageWeightIter.next();
            classification.displayAgeWeightClassification();
         } 
      }
   }
   
   public static void main(String args[])
   {
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      matrix.displayMatrixData();
      //matrix.displayMatrixTable();
      
   }

   public static String[] getMatrixClasses()
   {
      return matrixClasses;
   }
   
   public int getIndexOfClass(String classStr)
   {
      for (int i = 0; i < matrixClasses.length; i++)
      {
         if (classStr.equals(matrixClasses[i]))
            return i;
      }

      return -1;
   }
   
   public String getClassStrByIndex(int index)
   {
      System.out.println("index value: " + index);
      if (index < 0)
      {
         return "";
      }
      else
      {
         return matrixClasses[index];
      }
   }

   @Override
   public void displayMatrixTable()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public AgeClass[] getAgeClasses()
   {
      return ageClasses;
   }
   
   
   

}
