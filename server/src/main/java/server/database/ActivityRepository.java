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
        value="SELECT * FROM Activity ORDER BY random() LIMIT ?1")
    Optional<List<Activity>> getRandomActivities(int limit);

    /**
     * Gets a random activity
     * @return Optional of an Activity
     */
    @Query(
            nativeQuery=true,
            value="SELECT * FROM Activity ORDER BY random() LIMIT 1")
    Optional<Activity> getRandomActivity();

    /**
     * Calculates the total number of distinct values all the Activities have for Consumption
     * @return The number of distinct values for Consumption
     */
    @Query(
            nativeQuery=true,
            value="SELECT COUNT(DISTINCT CONSUMPTION_IN_WH) FROM Activity")
    int numberDistinctConsumptions();
}
