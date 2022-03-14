package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    /**
     * Gets a number of random activities
     * @param limit how many distinct activities do you want
     * @return Optional of an Activity list
     */
    @Query(
        nativeQuery=true,
        value="SELECT * FROM Activity a ORDER BY random() LIMIT ?1")
    Optional<List<Activity>> getRandomActivities(int limit);
}
