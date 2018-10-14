package CWA.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import CWA.MatrixClassification.AgeClass;
import CWA.MatrixClassification.CWAAgeWeightClassificationMatrix;

@RestController
public class MatrixController
{
   @RequestMapping(value="/matrix", method=RequestMethod.GET)
   public ClassWithWeights[] getMatrixTable()
   {
      ArrayList<ClassWithWeights> classWithWeightsLst = new ArrayList<>();
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      HashMap<String,ArrayList<String>> matrixMap = matrix.getMatrixTable();
      
      for (String key: CWAAgeWeightClassificationMatrix.getMatrixClasses())
      {
        System.out.println(key);
        ArrayList<String> weightsInClassAryLst = matrixMap.get(key);
        
        
        //weightsInClassAryLst.sort(Collections.reverseOrder());
        
        //empty column insert
        if (key.compareToIgnoreCase("A") != 0 && key.compareTo("B") != 0)
        {
           weightsInClassAryLst.add(0, "");
        }
        if (key.compareToIgnoreCase("C") != 0 && key.compareTo("D") != 0 &&
            key.compareToIgnoreCase("A") != 0 && key.compareTo("B") != 0)
        {
           weightsInClassAryLst.add(0, "");
        }
        
        String[] weightsInClassAry = weightsInClassAryLst.toArray(new String[0]);
        
        System.out.println(Arrays.toString(weightsInClassAry));
        ClassWithWeights currRecord = new ClassWithWeights(key, weightsInClassAry);
        classWithWeightsLst.add(currRecord);
      }
      return classWithWeightsLst.toArray(new ClassWithWeights[0]);
   }
   
   @RequestMapping(value="/matrixAgeClasses", method=RequestMethod.GET)
   public String[] getMatrixTableAgeClasses()
   {
      ArrayList<String> ageClassDescriptions = new ArrayList<>();
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      AgeClass[] ageClasses = matrix.getAgeClasses();
      for (AgeClass ageClass: ageClasses)
      {
         ageClassDescriptions.add(ageClass.getDescription());
      }
            
      return ageClassDescriptions.toArray(new String[0]);
   }
   

   class ClassWithWeights implements Serializable
   {
      /**
       * 
       */
      private static final long serialVersionUID = 1L;
      public ClassWithWeights(String className, String[] weightRanges)
      {
         m_className = className;
         m_weightRanges = weightRanges;
      }
      String m_className;
      public String[] m_weightRanges;   
      
      public String getClassName()
      {
         return m_className;
      }
      
      public String[] getWeightRanges()
      {
         return m_weightRanges;
      }
   }
}
