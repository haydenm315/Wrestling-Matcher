package CWA.MatrixClassification;

import java.util.Comparator;

/**
 * This comparator sorts a weight range string "45 - 49"
 * @author Hayden
 *
 */
public class WeightRangeComparator  implements Comparator
{

   @Override
   public int compare( Object o1, Object o2)
   {
      // TODO Auto-generated method stub
      String range1 = (String)o1;
      String range2 = (String)o2;
      
      String[] range1Split = range1.split("\\s");
      String[] range2Split = range2.split("\\s");
      
      if (range1Split != null && range2Split != null)
      {
         int range1Int = Integer.parseInt(range1Split[0]);
         int range2Int = Integer.parseInt(range2Split[0]);
         if (range1Int > range2Int)
         {
            System.out.println(range1Int + " > " + range2Int);
            return  1;
         }
         else if (range2Int > range1Int)
         {
            return -1;
         }
      }
       return 0;
   }
}
