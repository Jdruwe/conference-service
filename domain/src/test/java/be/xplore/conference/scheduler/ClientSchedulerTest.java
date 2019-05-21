package be.xplore.conference.scheduler;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test")
//public class ClientSchedulerTest {
//
//    @Autowired
//    private ClientScheduler clientScheduler;
//
//    @Autowired
//    private RoomService roomService;
//
//    @Autowired
//    private ClientService clientService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Test
//    public void testWasClientOffline(){
//        registerTestClients();
//        List<Client> clients = clientService.loadAll();
//        Assert.assertNotNull(clients);
//        boolean wasClientOffline = clientScheduler.wasClientOffline(clients.get(0));
//        Assert.assertFalse(wasClientOffline);
//    }
//
//    private void registerTestClients(){
//        Room room = Room.builder()
//                .id("testRoom")
//                .name("Test room")
//                .capacity(850)
//                .setup("setup")
//                .build();
//        roomService.save(room);
//        ZonedDateTime registeredDate = ZonedDateTime.now();
//        ClientInfoDto clientInfoDto = new ClientInfoDto(room, registeredDate.toLocalDateTime());
//        clientService.save(modelMapper.map(clientInfoDto, Client.class));
//        clientService.save(modelMapper.map(clientInfoDto, Client.class));
//        clientService.save(modelMapper.map(clientInfoDto, Client.class));
//    }
//}
