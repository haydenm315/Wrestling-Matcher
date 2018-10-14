package CWA.controllers;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import CWA.Player.PlayerNameComparator;
import CWA.Team.Team;
import CWA.Team.TeamRepository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class TeamController
{
   @Autowired
   private TeamRepository teamRepository;
   
   
   //@Autowired
   //private MongoTemplate mongoTemplate;
   
   @RequestMapping(value="/teams", method=RequestMethod.GET)
   public Team[] teams() {
       return teamRepository.findAll().toArray(new Team[0]);
   }
   
   @RequestMapping(value="/team/update/{teamId}", method=RequestMethod.DELETE)
   public void deleteTeam(@PathVariable String teamId)
   {
      teamRepository.deleteById(teamId);
   }
   
   
   @RequestMapping(value="/team/update", method=RequestMethod.PUT)
   public void updateTeam(@RequestBody Team team)
   {
      teamRepository.save(team);
   }

   @RequestMapping(value="/team/update/{teamId}", method=RequestMethod.GET)
   public Team team(@PathVariable String teamId) {
       return teamRepository.findById(teamId);
   }
   
   @RequestMapping(value="/team/{teamId}", method=RequestMethod.GET)
   public Team teamDetails(@PathVariable String teamId) {
      Team team = teamRepository.findById(teamId);
      team.populateMatrixClasses();
      PlayerNameComparator comparator = new PlayerNameComparator();
      Arrays.sort(team.players,comparator);
      return team;
   }
   
   
   

   @RequestMapping(value="/teams", method=RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   @ResponseBody
   public Long createTeam(@RequestBody Team team)
   {
      teamRepository.save(team);
      return new Long(0);
   }
}
