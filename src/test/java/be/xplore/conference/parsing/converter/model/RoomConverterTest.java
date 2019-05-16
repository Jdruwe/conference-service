package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.consumer.dto.RoomDto;
import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RoomConverterTest {

    private RoomsDto roomsDto;
    private RoomDto roomDto1;
    private RoomDto roomDto2;

    @Before
    public void init() {
        roomDto1 = new RoomDto("id1", "roomDto", 345, "setup");
        roomDto2 = new RoomDto("id2", "roomDto", 345, "setup");
        List<RoomDto> roomDtos = new ArrayList<>();
        roomDtos.add(roomDto1);
        roomDtos.add(roomDto2);
        roomsDto = new RoomsDto("content", roomDtos);
    }

    @Test
    public void testConvertRoom() {
        List<Room> rooms = RoomConverter.toRooms(roomsDto);
        Assert.assertNotNull(rooms);
        Assert.assertEquals(2, rooms.size());
        Assert.assertEquals(roomDto1.getId(), rooms.get(0).getId());
        Assert.assertEquals(roomDto2.getId(), rooms.get(1).getId());
    }
}
