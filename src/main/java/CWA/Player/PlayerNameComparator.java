package CWA.Player;

import java.util.Comparator;

public class PlayerNameComparator implements Comparator<Player>
{
   @Override
   public int compare(Player o1, Player o2)
   {
      if (o1.getLastName().compareTo(o2.getLastName()) > 0)
      {
         return 1;
      }
      else
      {
         return -1;
      }
   }

}
