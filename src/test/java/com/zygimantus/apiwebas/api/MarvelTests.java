package com.zygimantus.apiwebas.api;

import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharacterDto;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.zygimantus.apiwebas.AbstractContextControllerTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Zygimantus
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MarvelTests extends AbstractContextControllerTests {

    private static final String CHAR_ID = "1011334";

    @Autowired
    private MarvelApiConsumer marvelApiConsumer;

    @Test
    public void testId() throws MarvelApiException {

        MarvelResponse<CharacterDto> marvelResponse = marvelApiConsumer.getCharacter(CHAR_ID);
        CharacterDto character = marvelResponse.getResponse();

        Assert.assertEquals(CHAR_ID, character.getId());
    }

}
