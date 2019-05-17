package be.xplore.conference.service;

import be.xplore.conference.Settings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SettingsServiceTest {
    private static final String TEST = "Test";
    private static final String TEST_SAVE_SETTING = "testSaveSetting";

    @Autowired
    private SettingsService settingsService;

    @Before
    public void setUp() {
        settingsService.save(new Settings(TEST, TEST));
    }

    @Test
    public void testLoadAll() {
        List<Settings> settingsList = settingsService.loadAll();
        Assert.assertNotNull(settingsList);
        Assert.assertEquals(6, settingsList.size());
        Assert.assertTrue(settingsList.contains(new Settings(TEST, TEST)));
    }

    @Test
    public void testSaveSetting() {
        settingsService.save(new Settings(TEST_SAVE_SETTING, TEST_SAVE_SETTING));
        List<Settings> settingsList = settingsService.loadAll();
        Assert.assertNotNull(settingsList);
        Assert.assertEquals(7, settingsList.size());
        Assert.assertTrue(settingsList.contains(new Settings(TEST_SAVE_SETTING, TEST_SAVE_SETTING)));
    }

    @Test
    public void testUpdateSettings() {
        Settings updatedSetting = settingsService.update(TEST, "newTest");
        Assert.assertNotNull(updatedSetting);
        Assert.assertEquals("newTest", updatedSetting.getValue());
    }
}
