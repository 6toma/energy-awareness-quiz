package server.api.dependencies;

import java.util.Random;


/**
 * Implementation of Random for testing
 * Is not random, counts up from 0
 */
public class TestRandom extends Random {

    long count = 0;

    @Override
    public long nextLong(){
        return count++;
    }
}
