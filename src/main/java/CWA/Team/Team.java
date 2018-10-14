package CWA.Team;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import CWA.MatrixClassification.CWAAgeWeightClassificationMatrix;
import CWA.MatrixClassification.WeightClassificationMatrix;
import CWA.Player.Player;

public class Team implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   @Id
   public String id;

   public String teamName;
   public String coachName;
   
   public Player[] players;

   public Team() {}

   public Team(String teamName, String coachName, Player[] players) {
       this.id = "1";
       this.teamName = teamName;
       this.coachName = coachName;
       this.players = players;
   }

   @Override
   public String toString() {
       return String.format(
               "Team[id=%s, teamName='%s', coachName='%s']",
               id, teamName, coachName);
   }
   
   public void populateMatrixClasses()
   {
      WeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      for (Player currPlayer: players)
      {
         currPlayer.setClass(matrix);
      }
   }
   
   //public String getContent() {
   //   return teamName;
   //}

}
