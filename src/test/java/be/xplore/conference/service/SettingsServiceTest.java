package be.xplore.conference.service;

import be.xplore.conference.model.Settings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SettingsServiceTest {
    @Autowired
    SettingsService settingsService;


    @Before
    public void setUp() {
        settingsService.save(new Settings("Test", "Test"));
    }

    @Test
    public void testLoadAll() {
        List<Settings> settingsList = settingsService.loadAll();
        Assert.assertNotNull(settingsList);
        Assert.assertEquals(3, settingsList.size());
        Assert.assertTrue(settingsList.get(2).getValue().contains("Test"));
    }

    @Test
    public void testSaveSetting() {
        settingsService.save(new Settings("testSaveSetting","testSaveSetting"));
        List<Settings> settingsList = settingsService.loadAll();
        Assert.assertNotNull(settingsList);
        Assert.assertEquals(4, settingsList.size());
        Assert.assertTrue(settingsList.get(3).getValue().contains("testSaveSetting"));
    }
}
