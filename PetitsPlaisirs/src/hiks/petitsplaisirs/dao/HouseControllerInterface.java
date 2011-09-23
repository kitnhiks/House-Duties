package hiks.petitsplaisirs.dao;

import hiks.petitsplaisirs.model.Container;
import hiks.petitsplaisirs.model.House;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
 
public interface HouseControllerInterface {
 @Put
 long create(House house);
 
 @Get
 Container getAllHouses();
}
