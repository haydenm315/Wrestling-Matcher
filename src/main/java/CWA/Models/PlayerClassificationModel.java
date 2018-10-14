package CWA.Models;

import java.util.ArrayList;
import java.util.HashMap;

import CWA.MatrixClassification.CWAAgeWeightClassificationMatrix;
import CWA.Player.Player;
import CWA.Team.Team;

public class PlayerClassificationModel
{
   
   public static final HashMap<String, ArrayList<Player>> getTeamPlayerClassifications(Team team)
   {
      HashMap<String, ArrayList<Player>> playersByClassMapTeam = new HashMap<>();
      
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      
      for(Player teamPlayer: team.players)
      {
         String playerMatrixValue = matrix.getMatrixValue(teamPlayer.getWeightInLbs(), teamPlayer.getAgeYears());
         System.out.println(teamPlayer.getFirstName() + " " + teamPlayer.getLastName() + ": " + playerMatrixValue);
         ArrayList<Player> currMapList = playersByClassMapTeam.get(playerMatrixValue);
         if (currMapList == null)
         {
            currMapList = new ArrayList<>();
            currMapList.add(teamPlayer);
            playersByClassMapTeam.put(playerMatrixValue, currMapList);
         }
         else
         {
            currMapList.add(teamPlayer);
         }            
      }
      return playersByClassMapTeam;
   }

   

}
