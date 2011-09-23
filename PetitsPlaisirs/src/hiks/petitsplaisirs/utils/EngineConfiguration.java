package hiks.petitsplaisirs.utils;

import org.restlet.engine.Engine;
import org.restlet.ext.httpclient.HttpClientHelper;
import org.restlet.ext.jackson.JacksonConverter;
 
public class EngineConfiguration
{
    private static EngineConfiguration  ourInstance = new EngineConfiguration();
 
    //public final static String  gae_path = "http://kitnhiks-houseduties.appspot.com/rest/0.1/";
    public final static String  gae_path = "http://127.0.0.1:8888/rest/0.1/";
 
    public static EngineConfiguration getInstance()
    {
        return ourInstance;
    }
 
    public EngineConfiguration()
    {
        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
        Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
    }
 
    public static EngineConfiguration getOurInstance()
    {
        return ourInstance;
    }
 
    public static void setOurInstance(EngineConfiguration ourInstance)
    {
        EngineConfiguration.ourInstance = ourInstance;
    }
}