package CWA.Team;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {

    public List<Team> findAll();

    public Team findById(String id);
    
    public int deleteById(String id);

}
