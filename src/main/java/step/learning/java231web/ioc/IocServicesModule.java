package step.learning.java231web.ioc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import step.learning.java231web.services.db.IDbService;
import step.learning.java231web.services.db.MySqlDbService;
import step.learning.java231web.services.hash.IHashService;
import step.learning.java231web.services.hash.Md5HashService;

/**
 * Модуль конфігурації сервісів (служб) інжектора
 * @author Lector
 */
public class IocServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind( IHashService.class )
                .to( Md5HashService.class );
        bind( IDbService.class )
                .to( MySqlDbService.class );
        bind( Gson.class ).toInstance( 
                new GsonBuilder()
                    .serializeNulls()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create() 
        );
    }
    
}
