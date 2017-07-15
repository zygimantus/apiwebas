package com.zygimantus.apiwebas.api;

import com.zygimantus.apiwebas.AbstractContextControllerTests;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Chart;
import de.umass.lastfm.User;
import java.text.DateFormat;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Zygimantus
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LastFmTests extends AbstractContextControllerTests {

    @Test
    public void testOne() {

        String key = "b25b959554ed76058ac220b7b2e0a026"; //this is the key used in the Last.fm API examples
        String user = "JRoar";
        Chart<Artist> chart = User.getWeeklyArtistChart(user, 10, key);
        DateFormat format = DateFormat.getDateInstance();
        String from = format.format(chart.getFrom());
        String to = format.format(chart.getTo());
        System.out.printf("Charts for %s for the week from %s to %s:%n", user, from, to);
        Collection<Artist> artists = chart.getEntries();
        artists.forEach((artist) -> {
            System.out.println(artist.getName());
        });
    }
}
