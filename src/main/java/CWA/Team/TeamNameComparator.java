package CWA.Team;

import java.util.Comparator;

public class TeamNameComparator implements Comparator<Team>

{
   @Override
   public int compare(Team o1, Team o2)
   {
      if (o1.teamName.compareToIgnoreCase(o2.teamName) > 0)
      {
         return 1;
      }
      else
      {
         return -1;
      }
   }
}
