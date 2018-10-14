package CWA.Player;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * This comparator uses a combination of matrix class, age in days and weight to order a player for
 * determining their fit for a match
 * @author Hayden
 *
 */
public class PlayerAgeWeightComparator implements Comparator<Player>
{

   @Override
  /**
   * Sort players evenly by age and weight. Determine the difference in lbs vs days to determine how to weight the
   * multiplier to make them an even criteria.
   */
   public int compare(Player player1, Player player2)
   {
      //score is 80% weight + 20% days
      LocalDate today = LocalDate.now();
      LocalDate birthdayPlayer1 = Instant.ofEpochMilli(player1.getBirthDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

      LocalDate birthdayPlayer2 = Instant.ofEpochMilli(player2.getBirthDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
     
      double differenceInWeightLbs = Math.abs(player1.getWeightInLbs() -player2.getWeightInLbs());
      double differenceInAgeDays = Math.abs((int) ChronoUnit.DAYS.between(birthdayPlayer1, birthdayPlayer2));
      long player1DaysOld = Math.abs((int) ChronoUnit.DAYS.between(birthdayPlayer1, today));
      long player2DaysOld = Math.abs((int) ChronoUnit.DAYS.between(birthdayPlayer2, today));

      double percentDifferentAgeMultiplier= .2;
      double percentDifferentWeightMultiplier = .8;
      
      if (differenceInWeightLbs > differenceInAgeDays ||
          differenceInWeightLbs < differenceInAgeDays)
      {
         percentDifferentAgeMultiplier = differenceInWeightLbs / (differenceInWeightLbs + differenceInAgeDays);
         percentDifferentWeightMultiplier = differenceInAgeDays / (differenceInWeightLbs + differenceInAgeDays);
      }
        
      double player1Score = (player1.getWeightInLbs() * percentDifferentWeightMultiplier) + (player1DaysOld * percentDifferentAgeMultiplier);
      double player2Score = (player2.getWeightInLbs() * percentDifferentWeightMultiplier) + (player2DaysOld * percentDifferentAgeMultiplier);
      /*System.out.println("Scores\n");
      System.out.println(player1.getFirstName() + " " + player1.getLastName() + ": " + player1Score);
      System.out.println(player2.getFirstName() + " " + player2.getLastName() + ": " + player2Score);
      System.out.println("-----\n");*/
      
      if (player1Score > player2Score)
      {
         return 1;
      }
      else 
      {
         return -1;
      }
   }
}
