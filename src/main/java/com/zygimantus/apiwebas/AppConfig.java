package com.zygimantus.apiwebas;

import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
@Sources("classpath:app.properties")
public interface AppConfig extends Mutable {

    @DefaultValue("")
    String publicKey();

    @DefaultValue("")
    String privateKey();

    @DefaultValue("1b44a5a987b94bd2c30ad42fd1d00846")
    String tmdbMoviesApiKey();

}
