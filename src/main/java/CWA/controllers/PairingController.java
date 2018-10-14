package CWA.controllers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import CWA.Pairing;
import CWA.PairingResult;
import CWA.Models.PairingModel;
import CWA.Player.Player;
import CWA.Team.Team;
import CWA.Team.TeamRepository;

@RestController
public class PairingController
{
   @Autowired
   private TeamRepository teamRepository;
   
   
   @RequestMapping(value="/pairing", method=RequestMethod.POST)
   public PairingResult createPairing(@RequestBody PairingRequest pairingRequest)
   {
      Team team1 = teamRepository.findById(pairingRequest.teamId1);
      Team team2 = teamRepository.findById(pairingRequest.teamId2);
      ArrayList<String> team1PlayerIds = new ArrayList<String>(Arrays.asList(pairingRequest.team1Players));
      ArrayList<String> team2PlayerIds = new ArrayList<String>(Arrays.asList(pairingRequest.team2Players));
      
      List<Player> team1PlayerList = new ArrayList<Player>();
      List<Player> team2PlayerList = new ArrayList<Player>();

      for (Player player : team1.players)
      {
         if (team1PlayerIds.contains(player.getId()))
         {
            team1PlayerList.add(player);
         }
      }
      
      for (Player player : team2.players)
      {
         if (team2PlayerIds.contains(player.getId()))
         {
            team2PlayerList.add(player);
         }
      }
      team1.players = team1PlayerList.toArray(new Player[0]);
      team2.players = team2PlayerList.toArray(new Player[0]);
      PairingResult pairings = PairingModel.pairTeams(team1, team2);

      return pairings;        
   }
}
