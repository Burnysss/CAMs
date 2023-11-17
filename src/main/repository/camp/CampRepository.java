package main.repository.camp;

import main.model.camp.Camp;
import main.repository.Repository;

import java.util.List;
import java.util.Map;

import static main.utils.config.Location.RESOURCE_LOCATION;

public class CampRepository extends Repository<Camp> 
{
    private static final String FILE_PATH = "/data/camp/camp.txt";

    CampRepository() 
    {
        super();
        load();
    }

    public static CampRepository getInstance()
    {
        return new CampRepository();
    }

    @Override
    public String getFilePath() 
    {
        return RESOURCE_LOCATION + FILE_PATH;
    }

    @Override
    public void setAll(List<Map<String, String>> listOfMappableObjects) 
    {
        for (Map<String, String> map : listOfMappableObjects) 
        {
            getAll().add(new Camp(map));
        }
    }
}